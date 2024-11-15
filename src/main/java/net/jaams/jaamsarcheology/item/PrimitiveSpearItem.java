
package net.jaams.jaamsarcheology.item;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ForgeMod;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModSounds;
import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;
import net.jaams.jaamsarcheology.entity.SpearProjectileEntity;

import java.util.UUID;
import java.util.List;

import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap;

public class PrimitiveSpearItem extends SwordItem {
	public PrimitiveSpearItem() {
		super(new Tier() {
			public int getUses() {
				return 120;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return -0.5f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 10;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(JaamsArcheologyModItems.SPEAR_FRAGMENT.get()));
			}
		}, 3, -2f, new Item.Properties());
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
	}

	private static final UUID BASE_ATTACK_RANGE_UUID = UUID.randomUUID();

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
		if (equipmentSlot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
			if (!ModList.get().isLoaded("bettercombat")) {
				builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ATTACK_RANGE_UUID, "Weapon modifier", 1, AttributeModifier.Operation.ADDITION));
			}
			return builder.build();
		}
		return super.getDefaultAttributeModifiers(equipmentSlot);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return toolAction.equals(ToolActions.SWORD_DIG);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.SPEAR;
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1) {
			return InteractionResultHolder.fail(itemStack);
		}
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(itemStack);
	}

	@Override
	public void releaseUsing(ItemStack weaponItemStack, Level level, LivingEntity entity, int durationUsed) {
		if (entity instanceof Player player) {
			int remainingDuration = this.getUseDuration(weaponItemStack) - durationUsed;
			if (remainingDuration >= 10) {
				if (!level.isClientSide) {
					weaponItemStack.hurtAndBreak(1, player, (brokenItemStack) -> {
						brokenItemStack.broadcastBreakEvent(entity.getUsedItemHand());
					});
					SpearProjectileEntity thrownWeapon = new SpearProjectileEntity(level, player, weaponItemStack);
					thrownWeapon.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F + (float) 0.0F * 0.5F, 1.0F);
					thrownWeapon.weaponDamage = 3.5F;
					thrownWeapon.weaponItem = weaponItemStack.copy();
					if (player.getAbilities().instabuild) {
						thrownWeapon.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					}
					level.addFreshEntity(thrownWeapon);
					level.playSound((Player) null, thrownWeapon, JaamsArcheologyModSounds.PRIMITIVE_SPEAR_FIRED.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
					if (!player.getAbilities().instabuild) {
						weaponItemStack.shrink(1);
					}
					player.awardStat(Stats.ITEM_USED.get(this));
				}
			}
		}
	}
}
