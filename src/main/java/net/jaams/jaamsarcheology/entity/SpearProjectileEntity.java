
package net.jaams.jaamsarcheology.entity;

import org.jetbrains.annotations.NotNull;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.BlockParticleOption;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModEntities;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyServerConfiguration;

import javax.annotation.Nullable;

public class SpearProjectileEntity extends AbstractArrow implements IEntityAdditionalSpawnData {
	public ItemStack weaponItem;
	public float weaponDamage;
	SoundEvent loyalty = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:primitive_spear_return"));
	SoundEvent hit = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:primitive_spear_hit"));
	SoundEvent ground = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:primitive_spear_ground"));
	private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(SpearProjectileEntity.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(SpearProjectileEntity.class, EntityDataSerializers.BOOLEAN);
	private boolean dealtDamage;
	private boolean hasImpacted;
	private int bubbleTime = 0;
	public int clientSideReturnTridentTickCount;
	@Nullable
	private BlockState lastState;

	public SpearProjectileEntity(PlayMessages.SpawnEntity packet, Level world) {
		super(JaamsArcheologyModEntities.SPEAR_PROJECTILE.get(), world);
	}

	public SpearProjectileEntity(EntityType<? extends SpearProjectileEntity> type, Level world) {
		super(type, world);
	}

	public SpearProjectileEntity(EntityType<? extends SpearProjectileEntity> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world);
	}

	public SpearProjectileEntity(EntityType<? extends SpearProjectileEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world);
	}

	public SpearProjectileEntity(Level world, Player player, ItemStack item) {
		super(JaamsArcheologyModEntities.SPEAR_PROJECTILE.get(), player, world);
		this.weaponItem = item.copy();
		this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(item));
		this.entityData.set(ID_FOIL, item.hasFoil());
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ID_LOYALTY, (byte) 0);
		this.entityData.define(ID_FOIL, false);
	}

	private void applyItemData(ItemStack stack) {
		this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(stack));
		this.entityData.set(ID_FOIL, stack.hasFoil());
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Weapon", 10)) {
			this.weaponItem = ItemStack.of(tag.getCompound("Trident"));
		}
		this.dealtDamage = tag.getBoolean("DealtDamage");
		this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.weaponItem));
		if (tag.contains("ProjectileItem", 10)) {
			this.weaponItem = ItemStack.of(tag.getCompound("ProjectileItem"));
		} else {
			this.weaponItem = ItemStack.EMPTY;
		}
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Weapon", this.weaponItem.save(new CompoundTag()));
		tag.putBoolean("DealtDamage", this.dealtDamage);
		if (!this.weaponItem.isEmpty()) {
			tag.put("ProjectileItem", this.weaponItem.save(new CompoundTag()));
		}
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

	protected EntityHitResult findHitEntity(Vec3 startPos, Vec3 endPos) {
		return this.dealtDamage ? null : super.findHitEntity(startPos, endPos);
	}

	@Override
	protected @NotNull ItemStack getPickupItem() {
		return this.weaponItem.copy();
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
				((ServerLevel) level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state).setPos(blockHitResult.getBlockPos()), getX(), getY(), getZ(), 6, 0.1d, 0.1d, 0.1d, 0.05d);
			}
		}
		this.playSound(ground, 1.0F, 1.0F);
	}

	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return SoundEvents.EMPTY;
	}

	@Override
	public void playerTouch(Player player) {
		if (this.ownedBy(player) || this.getOwner() == null) {
			super.playerTouch(player);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		Entity entity = entityHitResult.getEntity();
		float f = 4.0F + this.weaponDamage;
		if (entity instanceof LivingEntity livingentity) {
			f += EnchantmentHelper.getDamageBonus(this.weaponItem, livingentity.getMobType());
		}
		Entity entity1 = this.getOwner();
		DamageSource damagesource = this.damageSources().trident(this, (Entity) (entity1 == null ? this : entity1));
		this.dealtDamage = true;
		if (entity.hurt(damagesource, f)) {
			if (entity.getType() == EntityType.ENDERMAN) {
				return;
			}
			if (entity instanceof LivingEntity) {
				LivingEntity livingentity1 = (LivingEntity) entity;
				if (entity1 instanceof LivingEntity) {
					EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
					EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity1);
				}
				this.doPostHurtEffects(livingentity1);
			}
		}
		this.hasImpacted = true;
		this.setDeltaMovement(this.getDeltaMovement().multiply(-0.02D, -0.2D, -0.02D));
		this.playSound(hit, 1.0F, 1.0F);
	}

	@Override
	protected void doPostHurtEffects(LivingEntity livingentity) {
		super.doPostHurtEffects(livingentity);
		// Fire Aspect enchantment effect
		int fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, this.weaponItem);
		if (fireAspectLevel > 0) {
			livingentity.setSecondsOnFire(fireAspectLevel * 4);
		}
		// Knockback enchantment effect
		int knockbackLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK, this.weaponItem);
		if (knockbackLevel > 0) {
			double knockbackStrength = knockbackLevel * 1; // Adjust as necessary
			double motionX = livingentity.getX() - this.getX();
			double motionY = livingentity.getY() - this.getY();
			double motionZ = livingentity.getZ() - this.getZ();
			double distance = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
			livingentity.push(motionX / distance * knockbackStrength, 0.1D, motionZ / distance * knockbackStrength);
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
			if (bubbleTime <= 200) { // 5 seconds * 20 ticks per second
				this.level().addParticle(ParticleTypes.BUBBLE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		} else {
			bubbleTime = 0; // reset bubble time if not in water
		}
		if (this.inGroundTime > 4) {
			this.dealtDamage = true;
		}
		if (this.inGroundTime > 1 && !hasImpacted) {
			this.hasImpacted = true;
		}
		if (!hasImpacted) {
			ticksSpinning++;
		}
		if (this.isInLava()) {
			dropAsItem();
			this.discard();
		}
		Entity entity = this.getOwner();
		int i = this.entityData.get(ID_LOYALTY);
		if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
			if (!this.isAcceptibleReturnOwner()) {
				if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
					this.spawnAtLocation(this.getPickupItem(), 0.1F);
				}
				this.discard();
			} else {
				this.setNoPhysics(true);
				Vec3 vec3 = entity.getEyePosition().subtract(this.position());
				this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double) i, this.getZ());
				if (this.level().isClientSide) {
					this.yOld = this.getY();
				}
				double d0 = 0.05D * (double) i;
				this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
				if (this.clientSideReturnTridentTickCount == 0) {
					this.playSound(loyalty, 10.0F, 1.0F);
				}
				++this.clientSideReturnTridentTickCount;
			}
		}
		super.tick();
	}

	int life = 0;

	@Override
	protected void tickDespawn() {
		life++;
		if (life >= JaamsArcheologyServerConfiguration.PRIMITIVESPEARDESPAWN.get()) {
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

	private boolean isAcceptibleReturnOwner() {
		Entity entity = this.getOwner();
		if (entity != null && entity.isAlive()) {
			return !(entity instanceof ServerPlayer) || !entity.isSpectator();
		} else {
			return false;
		}
	}

	public boolean isFoil() {
		return this.entityData.get(ID_FOIL);
	}

	protected boolean tryPickup(Player player) {
		return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
	}

	public boolean hasImpacted() {
		return this.hasImpacted;
	}

	public int getSpinTicks() {
		return this.ticksSpinning;
	}

	public float weaponDamage() {
		return this.weaponDamage;
	}

	protected float getWaterInertia() {
		return 0.99F;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean shouldRender(double x, double y, double z) {
		return true;
	}
}
