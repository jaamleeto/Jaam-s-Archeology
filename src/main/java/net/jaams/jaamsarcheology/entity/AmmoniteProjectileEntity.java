
package net.jaams.jaamsarcheology.entity;

import org.jetbrains.annotations.NotNull;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ItemParticleOption;

import net.jaams.jaamsarcheology.util.WeightedItem;
import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;
import net.jaams.jaamsarcheology.init.JaamsArcheologyModEntities;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyCommonConfiguration;

import javax.annotation.Nullable;

import java.util.stream.Collectors;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class AmmoniteProjectileEntity extends AbstractArrow implements IEntityAdditionalSpawnData {
	public ItemStack ammoniteItem;
	private boolean hasImpacted;
	private int bubbleTime = 0;
	@Nullable
	private BlockState lastState;
	SoundEvent breaks = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:ammonite_breaks"));

	public AmmoniteProjectileEntity(PlayMessages.SpawnEntity packet, Level world) {
		super(JaamsArcheologyModEntities.AMMONITE_PROJECTILE.get(), world);
		this.ammoniteItem = new ItemStack(JaamsArcheologyModItems.PETRIFIED_AMMONITE.get());
	}

	public AmmoniteProjectileEntity(EntityType<? extends AmmoniteProjectileEntity> type, Level world) {
		super(type, world);
		this.ammoniteItem = new ItemStack(JaamsArcheologyModItems.PETRIFIED_AMMONITE.get());
	}

	public AmmoniteProjectileEntity(EntityType<? extends AmmoniteProjectileEntity> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world);
		this.ammoniteItem = new ItemStack(JaamsArcheologyModItems.PETRIFIED_AMMONITE.get());
	}

	public AmmoniteProjectileEntity(EntityType<? extends AmmoniteProjectileEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world);
		this.ammoniteItem = new ItemStack(JaamsArcheologyModItems.PETRIFIED_AMMONITE.get());
	}

	public AmmoniteProjectileEntity(Level level, LivingEntity shooter, ItemStack ammoniteItem) {
		super(JaamsArcheologyModEntities.AMMONITE_PROJECTILE.get(), shooter, level);
		this.ammoniteItem = ammoniteItem.copy();
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Weapon", 10)) {
			this.ammoniteItem = ItemStack.of(tag.getCompound("Weapon"));
		}
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Weapon", this.ammoniteItem.save(new CompoundTag()));
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeItem(this.ammoniteItem);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		this.ammoniteItem = additionalData.readItem();
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected @NotNull ItemStack getPickupItem() {
		ItemStack itemStack = this.ammoniteItem.copy();
		itemStack.setCount(1);
		return itemStack;
	}

	@Override
	public void onHitBlock(BlockHitResult blockHitResult) {
		this.lastState = this.level().getBlockState(blockHitResult.getBlockPos());
		Vec3 vec3 = blockHitResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
		this.setDeltaMovement(vec3);
		Vec3 vec31 = vec3.normalize().scale((double) 0.05F);
		this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
		this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
		this.inGround = true;
		this.shakeTime = 7;
		this.setCritArrow(false);
		this.setPierceLevel((byte) 0);
		this.setShotFromCrossbow(false);
		if (blockHitResult.getType() == BlockHitResult.Type.BLOCK) {
			if (!level().isClientSide) {
				double posX = blockHitResult.getLocation().x();
				double posY = blockHitResult.getLocation().y();
				double posZ = blockHitResult.getLocation().z();
				ItemStack itemStack = this.ammoniteItem;
				((ServerLevel) level()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemStack), posX, posY, posZ, 15, 0.1d, 0.1d, 0.1d, 0.05d);
			}
		}
		this.playSound(breaks, 0.5F, 1.0F);
		if (ammoniteItem.getItem() == JaamsArcheologyModItems.SHINY_AMMONITE.get()) {
			if (Math.random() < 0.7) {
				ShinyRandomItemFromList();
			}
		}
		if (ammoniteItem.getItem() == JaamsArcheologyModItems.PETRIFIED_AMMONITE.get()) {
			if (Math.random() < 0.7) {
				PetrifiedRandomItemFromList();
			}
		}
		if (ammoniteItem.getItem() == JaamsArcheologyModItems.OSSIFIED_AMMONITE.get()) {
			if (Math.random() < 0.5) {
				OssifiedRandomItemFromList();
			}
		}
		this.discard();
	}

	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return SoundEvents.EMPTY;
	}

	@Override
	public void playerTouch(Player entity) {
		super.playerTouch(entity);
	}

	@Override
	public void onHitEntity(EntityHitResult entityHitResult) {
		Entity entity = entityHitResult.getEntity();
		boolean isEnderman = entity.getType() == EntityType.ENDERMAN;
		Entity owner = this.getOwner();
		if (owner instanceof Player && ((Player) owner).isCreative()) {
			this.discard();
		}
		if (this.isOnFire() && !isEnderman) {
			entity.setSecondsOnFire(5);
		}
		if (entity instanceof LivingEntity livingEntity) {
			if (livingEntity.isBlocking()) {
				applyAmmoniteLogic();
				this.discard();
				return;
			}
			super.onHitEntity(entityHitResult);
		} else {
			entity.hurt(this.damageSources().generic(), (float) 10);
			applyAmmoniteLogic();
			this.discard();
		}
		applyAmmoniteLogic();
	}

	private void applyAmmoniteLogic() {
		this.playSound(breaks, 0.5F, 1.0F);
		if (!level().isClientSide && !this.ammoniteItem.isEmpty()) {
			ItemStack itemStack = this.ammoniteItem;
			if (!itemStack.isEmpty()) {
				((ServerLevel) level()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemStack), getX(), getY(), getZ(), 5, 0.1d, 0.1d, 0.1d, 0.05d);
			}
		}
		if (ammoniteItem.getItem() == JaamsArcheologyModItems.SHINY_AMMONITE.get()) {
			if (Math.random() < 0.7) {
				ShinyRandomItemFromList();
			}
		}
		if (ammoniteItem.getItem() == JaamsArcheologyModItems.PETRIFIED_AMMONITE.get()) {
			if (Math.random() < 0.7) {
				PetrifiedRandomItemFromList();
			}
		}
		if (ammoniteItem.getItem() == JaamsArcheologyModItems.OSSIFIED_AMMONITE.get()) {
			if (Math.random() < 0.5) {
				OssifiedRandomItemFromList();
			}
		}
	}

	int ticksSpinning = 0;

	public int GetSpinTicks() {
		return ticksSpinning;
	}

	@Override
	public void tick() {
		if (this.level().isClientSide && this.isInWater() && !this.inGround) {
			bubbleTime++;
			if (bubbleTime <= 200) {
				this.level().addParticle(ParticleTypes.BUBBLE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		} else {
			bubbleTime = 0;
		}
		if (this.inGroundTime > 1 && !hasImpacted) {
			this.hasImpacted = true;
			this.ticksSpinning = 0;
		}
		if (!hasImpacted) {
			ticksSpinning++;
		}
		if (this.isInLava()) {
			this.discard();
		}
		List<Entity> collidingEntities = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox());
		List<Entity> collidingProjectiles = collidingEntities.stream().filter(entity -> entity instanceof Projectile && entity != this).collect(Collectors.toList());
		for (Entity projectile : collidingProjectiles) {
			if (ammoniteItem.getItem() == JaamsArcheologyModItems.SHINY_AMMONITE.get()) {
				if (Math.random() < 0.9) {
					ShinyRandomItemFromList();
				}
			}
			if (ammoniteItem.getItem() == JaamsArcheologyModItems.PETRIFIED_AMMONITE.get()) {
				if (Math.random() < 0.9) {
					PetrifiedRandomItemFromList();
				}
			}
			if (ammoniteItem.getItem() == JaamsArcheologyModItems.OSSIFIED_AMMONITE.get()) {
				if (Math.random() < 0.7) {
					OssifiedRandomItemFromList();
				}
			}
			breakProjectile();
			return;
		}
		super.tick();
	}

	private void breakProjectile() {
		if (!level().isClientSide) {
			ItemStack itemStack = this.ammoniteItem;
			((ServerLevel) level()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemStack), getX(), getY(), getZ(), 15, 0.1d, 0.1d, 0.1d, 0.05d);
		}
		this.playSound(breaks, 0.5F, 1.0F);
		this.discard();
	}

	@Override
	public int getPortalWaitTime() {
		return 72000;
	}

	public boolean isFoil() {
		return false;
	}

	public boolean hasImpacted() {
		return this.hasImpacted;
	}

	public int getSpinTicks() {
		return this.ticksSpinning;
	}

	private void ShinyRandomItemFromList() {
		if (JaamsArcheologyCommonConfiguration.AMMONITEDROPS.get()) {
			if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
				List<WeightedItem> items = parseWeightedItems(JaamsArcheologyCommonConfiguration.SHINY_AMMONITE_ITEMS.get());
				if (!items.isEmpty()) {
					ItemStack randomItem = getRandomWeightedItem(items);
					ItemEntity itemEntity = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), randomItem);
					serverLevel.addFreshEntity(itemEntity);
				}
			}
		}
	}

	private void PetrifiedRandomItemFromList() {
		if (JaamsArcheologyCommonConfiguration.AMMONITEDROPS.get()) {
			if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
				List<WeightedItem> items = parseWeightedItems(JaamsArcheologyCommonConfiguration.PETRIFIED_AMMONITE_ITEMS.get());
				if (!items.isEmpty()) {
					ItemStack randomItem = getRandomWeightedItem(items);
					ItemEntity itemEntity = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), randomItem);
					serverLevel.addFreshEntity(itemEntity);
				}
			}
		}
	}

	private void OssifiedRandomItemFromList() {
		if (JaamsArcheologyCommonConfiguration.AMMONITEDROPS.get()) {
			if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
				List<WeightedItem> items = parseWeightedItems(JaamsArcheologyCommonConfiguration.OSSIFIED_AMMONITE_ITEMS.get());
				if (!items.isEmpty()) {
					ItemStack randomItem = getRandomWeightedItem(items);
					ItemEntity itemEntity = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), randomItem);
					serverLevel.addFreshEntity(itemEntity);
				}
			}
		}
	}

	private List<WeightedItem> parseWeightedItems(List<? extends String> configList) {
		List<WeightedItem> items = new ArrayList<>();
		for (String entry : configList) {
			String[] parts = entry.split(",\\s*weight:");
			String itemId = parts[0].trim();
			float weight = 1.0f;
			if (parts.length > 1) {
				try {
					weight = Float.parseFloat(parts[1].trim());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
			if (item != null) {
				items.add(new WeightedItem(new ItemStack(item), weight));
			}
		}
		return items;
	}

	private ItemStack getRandomWeightedItem(List<WeightedItem> items) {
		float totalWeight = (float) items.stream().mapToDouble(WeightedItem::getWeight).sum();
		float randomValue = new Random().nextFloat() * totalWeight;
		for (WeightedItem item : items) {
			randomValue -= item.getWeight();
			if (randomValue <= 0) {
				return item.getItem().copy();
			}
		}
		return ItemStack.EMPTY;
	}

	public static AmmoniteProjectileEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 1f, 5, 5);
	}

	public static AmmoniteProjectileEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		AmmoniteProjectileEntity entityarrow = new AmmoniteProjectileEntity(JaamsArcheologyModEntities.AMMONITE_PROJECTILE.get(), entity, world);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static AmmoniteProjectileEntity shoot(LivingEntity entity, LivingEntity target) {
		AmmoniteProjectileEntity entityarrow = new AmmoniteProjectileEntity(JaamsArcheologyModEntities.AMMONITE_PROJECTILE.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(5);
		entityarrow.setKnockback(5);
		entityarrow.setCritArrow(false);
		entity.level().addFreshEntity(entityarrow);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
