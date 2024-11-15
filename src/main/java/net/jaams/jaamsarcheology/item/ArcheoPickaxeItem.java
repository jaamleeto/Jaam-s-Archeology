package net.jaams.jaamsarcheology.item;

import org.joml.Math;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;

import java.util.function.Consumer;
import java.util.List;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

public class ArcheoPickaxeItem extends PickaxeItem {
	private static final double USE_DISTANCE = 4.0; // Adjust this value to change the distance
	private static final int PARTICLE_TICK_INTERVAL = 10; // Generate particles every 20 ticks (1 second)
	private int particleTickCounter = 0;

	public ArcheoPickaxeItem() {
		super(new Tier() {
			public int getUses() {
				return 200;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 0.5f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 2;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.COPPER_INGOT));
			}
		}, 1, -3f, new Item.Properties());
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private static final HumanoidModel.ArmPose CHOP_POSE = HumanoidModel.ArmPose.create("CHOP_POSE", false, (model, entity, arm) -> {
				ModelPart armModel = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
				float tickCount = entity.tickCount;
				float swingSpeed = 0.8F; // Adjust this value to modulate the speed of the animation
				float swingAmount = 0.5F; // Adjust this value to modulate the amount of swing
				armModel.xRot = -1.0F + swingAmount * (float) Math.sin(tickCount * swingSpeed);
				armModel.yRot = 0.1F;
				armModel.zRot = 0.0F;
			});

			@Override
			public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
				if (!itemStack.isEmpty()) {
					if (entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0) {
						return CHOP_POSE;
					}
				}
				return HumanoidModel.ArmPose.ITEM;
			}

			@Override
			public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
				if (player.getUseItem() == itemInHand && player.isUsingItem()) {
					int i = arm == HumanoidArm.RIGHT ? 1 : -1;
					// Adjust this value to increase or decrease the speed of the animation
					float speedFactor = 0.7F; // Higher values = faster animation, lower values = slower animation
					// Define a hammering animation using a gentle downward arc motion
					float swingProgress = (player.tickCount + partialTick) * speedFactor;
					float arcMotion = (float) Math.sin(swingProgress) * 0.5F;
					float impactMotion = (float) Math.abs(Math.cos(swingProgress)) * 0.05F;
					// Apply translations to create the hammering motion
					poseStack.translate(i * 0.7F, -0.9F + impactMotion, -1.0F + arcMotion); // Adjusted translations to lower the overall position
					poseStack.scale(1.5F, 1.5F, 1.5F);
					// Apply rotations to simulate the hammering action
					poseStack.mulPose(Axis.XP.rotationDegrees(-45.0F + arcMotion * 60.0F)); // Increased downward rotation when moving forward
					poseStack.mulPose(Axis.YP.rotationDegrees(i * 5.0F)); // Slight side-to-side motion
					poseStack.mulPose(Axis.ZP.rotationDegrees(i * arcMotion * 2.5F)); // Minimal tilt along Z-axis
					return true; // Apply custom transformation
				}
				return false; // Use default transformation when not in use
			}
		});
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player != null) {
			ItemStack itemStack = player.getItemInHand(hand); // Obtener el ItemStack correspondiente
			HitResult hitResult = this.calculateHitResult(player);
			if (hitResult.getType() == HitResult.Type.BLOCK) {
				player.startUsingItem(hand);
				return InteractionResultHolder.consume(itemStack);
			} else {
				return InteractionResultHolder.fail(itemStack);
			}
		}
		return InteractionResultHolder.pass(ItemStack.EMPTY); // Añadir un retorno en caso de que el player sea null
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.CUSTOM;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
	}

	@Override
	public void onUseTick(Level world, LivingEntity entityLiving, ItemStack stack, int count) {
		if (entityLiving instanceof Player) {
			if (entityLiving.isDeadOrDying() || entityLiving.isSpectator()) {
				return;
			}
			Player player = (Player) entityLiving;
			HitResult hitResult = calculateHitResult(entityLiving);
			if (hitResult.getType() != HitResult.Type.BLOCK) {
				player.stopUsingItem();
			} else {
				if (hitResult instanceof BlockHitResult) {
					BlockHitResult blockHitResult = (BlockHitResult) hitResult;
					if (!world.isClientSide) {
						if (particleTickCounter++ % PARTICLE_TICK_INTERVAL == 0) {
							BlockPos blockPos = blockHitResult.getBlockPos();
							BlockState state = world.getBlockState(blockPos);
							Vec3 hitVec = blockHitResult.getLocation(); // Get the exact hit location
							// Calculate the offset to ensure particles are outside the block
							Direction direction = blockHitResult.getDirection();
							double offsetX = direction.getStepX() * 0.1;
							double offsetY = direction.getStepY() * 0.1;
							double offsetZ = direction.getStepZ() * 0.1;
							// Apply the offset to the particle position
							double particleX = hitVec.x + offsetX;
							double particleY = hitVec.y + offsetY;
							double particleZ = hitVec.z + offsetZ;
							((ServerLevel) world).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), particleX, particleY, particleZ, 4, // Reduced number of particles
									0.01D, 0.01D, 0.01D, // Smaller offsets for more control over particle spread
									0.002D // Slower particle speed
							);
						}
					}
				}
			}
		}
	}

	private HitResult calculateHitResult(LivingEntity p_281264_) {
		return ProjectileUtil.getHitResultOnViewVector(p_281264_, (p_281111_) -> {
			return !p_281111_.isSpectator() && p_281111_.isPickable();
		}, USE_DISTANCE);
	}
}
