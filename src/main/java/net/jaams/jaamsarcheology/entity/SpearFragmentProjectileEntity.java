
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
import net.minecraft.core.particles.BlockParticleOption;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;
import net.jaams.jaamsarcheology.init.JaamsArcheologyModEntities;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyServerConfiguration;

import javax.annotation.Nullable;

public class SpearFragmentProjectileEntity extends AbstractArrow implements IEntityAdditionalSpawnData {
	public ItemStack weaponItem;
	private boolean hasImpacted;
	private int bubbleTime = 0;
	@Nullable
	private BlockState lastState;
	SoundEvent hit = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:spear_fragment_hit"));
	SoundEvent ground = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:spear_fragment_ground"));

	public SpearFragmentProjectileEntity(PlayMessages.SpawnEntity packet, Level world) {
		super(JaamsArcheologyModEntities.SPEAR_FRAGMENT_PROJECTILE.get(), world);
		this.weaponItem = new ItemStack(JaamsArcheologyModItems.SPEAR_FRAGMENT.get());
	}

	public SpearFragmentProjectileEntity(EntityType<? extends SpearFragmentProjectileEntity> type, Level world) {
		super(type, world);
		this.weaponItem = new ItemStack(JaamsArcheologyModItems.SPEAR_FRAGMENT.get());
	}

	public SpearFragmentProjectileEntity(EntityType<? extends SpearFragmentProjectileEntity> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world);
		this.weaponItem = new ItemStack(JaamsArcheologyModItems.SPEAR_FRAGMENT.get());
	}

	public SpearFragmentProjectileEntity(EntityType<? extends SpearFragmentProjectileEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world);
		this.weaponItem = new ItemStack(JaamsArcheologyModItems.SPEAR_FRAGMENT.get());
	}

	public SpearFragmentProjectileEntity(Level level, LivingEntity shooter, ItemStack weaponItem) {
		super(JaamsArcheologyModEntities.SPEAR_FRAGMENT_PROJECTILE.get(), shooter, level);
		this.weaponItem = weaponItem.copy();
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Weapon", 10)) {
			this.weaponItem = ItemStack.of(tag.getCompound("Weapon"));
		}
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Weapon", this.weaponItem.save(new CompoundTag()));
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeItem(this.weaponItem);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		this.weaponItem = additionalData.readItem();
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected @NotNull ItemStack getPickupItem() {
		return new ItemStack(this.weaponItem.getItem(), 1);
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	@Override
	public void onHitBlock(BlockHitResult blockHitResult) {
		this.lastState = this.level().getBlockState(blockHitResult.getBlockPos()); // Corrected variable
		Vec3 vec3 = blockHitResult.getLocation().subtract(this.getX(), this.getY(), this.getZ()); // Corrected variable
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
				BlockState state = level().getBlockState(blockHitResult.getBlockPos());
				((ServerLevel) level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state).setPos(blockHitResult.getBlockPos()), getX(), getY(), getZ(), 5, 0.1d, 0.1d, 0.1d, 0.05d);
			}
		}
		this.playSound(ground, 1.0F, 1.0F);
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
		if (this.isOnFire() && !isEnderman) {
			entity.setSecondsOnFire(5);
		}
		if (entity instanceof LivingEntity) {
			super.onHitEntity(entityHitResult);
		} else {
			this.setDeltaMovement(this.getDeltaMovement().multiply(-0.02D, -0.2D, -0.02D));
			entity.hurt(this.damageSources().generic(), (float) 10);
		}
		if (!level().isClientSide) {
			if ((entity instanceof LivingEntity)) {
				ItemStack itemStack = this.weaponItem; // Create ItemStack using the constructor
				if (!itemStack.isEmpty()) { // Ensure itemStack is not empty
					((ServerLevel) level()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemStack), getX(), getY(), getZ(), 5, 0.1d, 0.1d, 0.1d, 0.05d);
				}
			}
		}
		this.playSound(hit, 1.0F, 1.0F);
	}

	@Override
	public void tick() {
		if (this.level().isClientSide && this.isInWater() && !this.inGround) {
			bubbleTime++;
			if (bubbleTime <= 200) { // 5 seconds * 20 ticks per second
				this.level().addParticle(ParticleTypes.BUBBLE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		} else {
			bubbleTime = 0; // reset bubble time if not in water
		}
		if (this.inGroundTime > 1 && !hasImpacted) {
			this.hasImpacted = true;
		}
		if (this.isInLava()) {
			dropAsItem();
			this.discard();
		}
		super.tick();
	}

	int life = 0;

	@Override
	protected void tickDespawn() {
		life++;
		if (life >= JaamsArcheologyServerConfiguration.SPEARFRAGMENTDESPAWN.get()) {
			if (JaamsArcheologyServerConfiguration.DROPASITEM.get() == true) {
				dropAsItem();
			}
			discard();
		}
	}

	protected void dropAsItem() {
		ItemStack itemToSpawn = this.weaponItem.copy();
		itemToSpawn.setCount(1); // Correct way to set the count to 1
		ItemEntity entityToSpawn = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), itemToSpawn);
		entityToSpawn.setPickUpDelay(10);
		this.level().addFreshEntity(entityToSpawn);
	}

	@Override
	public int getPortalWaitTime() {
		return 72000;
	}

	public boolean isFoil() {
		return true;
	}

	public boolean hasImpacted() {
		return this.hasImpacted;
	}

	public static SpearFragmentProjectileEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 1f, 5, 5);
	}

	public static SpearFragmentProjectileEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		SpearFragmentProjectileEntity entityarrow = new SpearFragmentProjectileEntity(JaamsArcheologyModEntities.SPEAR_FRAGMENT_PROJECTILE.get(), entity, world);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static SpearFragmentProjectileEntity shoot(LivingEntity entity, LivingEntity target) {
		SpearFragmentProjectileEntity entityarrow = new SpearFragmentProjectileEntity(JaamsArcheologyModEntities.SPEAR_FRAGMENT_PROJECTILE.get(), entity, entity.level());
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
