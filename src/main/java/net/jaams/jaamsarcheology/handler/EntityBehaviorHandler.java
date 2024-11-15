
package net.jaams.jaamsarcheology.handler;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.tags.ItemTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import net.jaams.jaamsarcheology.item.SwordOfUndyingItem;
import net.jaams.jaamsarcheology.init.JaamsArcheologyModSounds;
import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;
import net.jaams.jaamsarcheology.init.JaamsArcheologyModEntities;
import net.jaams.jaamsarcheology.entity.SpearProjectileEntity;
import net.jaams.jaamsarcheology.entity.SpearFragmentProjectileEntity;
import net.jaams.jaamsarcheology.entity.AmmoniteProjectileEntity;
import net.jaams.jaamsarcheology.dyeable.IDyeableItem;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyCommonConfiguration;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

@Mod.EventBusSubscriber
public class EntityBehaviorHandler {
	private static final double AMMONITE_THROW_PROBABILITY = 0.5;
	private static final Map<LivingEntity, Long> lastAmmoniteThrowTime = new HashMap<>();
	private static final long AMMONITE_THROW_COOLDOWN = 100;
	private static final long AMMONITE_PAUSE_AFTER_THROW = 20;

	@SubscribeEvent
	public static void onAmmoniteEntityUpdate(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (!entity.level().isClientSide && entity instanceof Mob mob) {
			ItemStack ammoniteItem = entity.getMainHandItem().is(ItemTags.create(new ResourceLocation("jaams_archeology", "ammonites")))
					? entity.getMainHandItem()
					: entity.getOffhandItem().is(ItemTags.create(new ResourceLocation("jaams_archeology", "ammonites"))) ? entity.getOffhandItem() : ItemStack.EMPTY;
			if (!ammoniteItem.isEmpty() && entity.level().random.nextDouble() < AMMONITE_THROW_PROBABILITY && entity.level().getGameTime() - lastAmmoniteThrowTime.getOrDefault(entity, 0L) >= AMMONITE_THROW_COOLDOWN) {
				LivingEntity target = mob.getTarget();
				if (target != null) {
					double distanceToTarget = target.distanceTo(entity);
					Vec3 directionToTarget = new Vec3(target.getX() - entity.getX(), target.getEyeY() - entity.getEyeY(), target.getZ() - entity.getZ()).normalize();
					Vec3 entityLookDirection = entity.getLookAngle().normalize();
					double angle = directionToTarget.dot(entityLookDirection);
					if (distanceToTarget <= 16.0 && distanceToTarget >= 4.0 && angle > 0.5) {
						InteractionHand hand = ammoniteItem == entity.getMainHandItem() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
						entity.swing(hand, true);
						AmmoniteProjectileEntity ammoniteProjectile = new AmmoniteProjectileEntity(JaamsArcheologyModEntities.AMMONITE_PROJECTILE.get(), entity, entity.level());
						ammoniteProjectile.setOwner(entity);
						ammoniteProjectile.ammoniteItem = ammoniteItem.copy();
						ammoniteProjectile.setPos(entity.getX() + directionToTarget.x, entity.getEyeY() - 0.1, entity.getZ() + directionToTarget.z);
						ammoniteProjectile.shoot(directionToTarget.x, directionToTarget.y, directionToTarget.z, 1.0F, 0.5F);
						entity.level().addFreshEntity(ammoniteProjectile);
						ammoniteItem.shrink(1);
						entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), JaamsArcheologyModSounds.AMMONITE_FIRED.get(), SoundSource.HOSTILE, 1.0F,
								1.0F + (entity.level().random.nextFloat() - entity.level().random.nextFloat()) * 0.2F);
						lastAmmoniteThrowTime.put(entity, entity.level().getGameTime() + AMMONITE_PAUSE_AFTER_THROW);
						mob.getNavigation().stop();
					}
				}
			}
			if (entity.level().getGameTime() > lastAmmoniteThrowTime.getOrDefault(entity, 0L)) {
				mob.getNavigation().recomputePath();
			}
		}
	}

	private static final double SPEAR_FRAGMENT_THROW_PROBABILITY = 0.5;
	private static final Map<LivingEntity, Long> lastSpearFragmentThrowTime = new HashMap<>();
	private static final long SPEAR_FRAGMENT_THROW_COOLDOWN = 60;
	private static final long SPEAR_FRAGMENT_PAUSE_AFTER_THROW = 20;

	@SubscribeEvent
	public static void onSpearFragmentEntityUpdate(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (!entity.level().isClientSide && entity instanceof Mob mob) {
			ItemStack spearFragmentItem = entity.getMainHandItem().is(JaamsArcheologyModItems.SPEAR_FRAGMENT.get())
					? entity.getMainHandItem()
					: entity.getOffhandItem().is(JaamsArcheologyModItems.SPEAR_FRAGMENT.get()) ? entity.getOffhandItem() : ItemStack.EMPTY;
			if (!spearFragmentItem.isEmpty() && entity.level().random.nextDouble() < SPEAR_FRAGMENT_THROW_PROBABILITY && entity.level().getGameTime() - lastSpearFragmentThrowTime.getOrDefault(entity, 0L) >= SPEAR_FRAGMENT_THROW_COOLDOWN) {
				LivingEntity target = mob.getTarget();
				if (target != null) {
					double distanceToTarget = target.distanceTo(entity);
					Vec3 directionToTarget = new Vec3(target.getX() - entity.getX(), target.getEyeY() - entity.getEyeY(), target.getZ() - entity.getZ()).normalize();
					Vec3 entityLookDirection = entity.getLookAngle().normalize();
					double angle = directionToTarget.dot(entityLookDirection);
					if (distanceToTarget <= 16.0 && distanceToTarget >= 4.0 && angle > 0.5) {
						InteractionHand hand = spearFragmentItem == entity.getMainHandItem() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
						entity.swing(hand, true);
						SpearFragmentProjectileEntity spearFragmentProjectile = new SpearFragmentProjectileEntity(JaamsArcheologyModEntities.SPEAR_FRAGMENT_PROJECTILE.get(), entity, entity.level());
						spearFragmentProjectile.setOwner(entity);
						spearFragmentProjectile.weaponItem = spearFragmentItem.copy();
						spearFragmentProjectile.setPos(entity.getX() + directionToTarget.x, entity.getEyeY() - 0.1, entity.getZ() + directionToTarget.z);
						spearFragmentProjectile.shoot(directionToTarget.x, directionToTarget.y, directionToTarget.z, 1.0F, 0.5F);
						entity.level().addFreshEntity(spearFragmentProjectile);
						spearFragmentItem.shrink(1);
						entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), JaamsArcheologyModSounds.SPEAR_FRAGMENT_FIRED.get(), SoundSource.HOSTILE, 1.0F,
								1.0F + (entity.level().random.nextFloat() - entity.level().random.nextFloat()) * 0.2F);
						lastSpearFragmentThrowTime.put(entity, entity.level().getGameTime() + SPEAR_FRAGMENT_PAUSE_AFTER_THROW);
						mob.getNavigation().stop();
					}
				}
			}
			if (entity.level().getGameTime() > lastSpearFragmentThrowTime.getOrDefault(entity, 0L)) {
				mob.getNavigation().recomputePath();
			}
		}
	}

	private static final double PRIMITIVE_SPEAR_THROW_PROBABILITY = 0.5;
	private static final Map<LivingEntity, Long> lastPrimitiveSpearThrowTime = new HashMap<>();
	private static final long PRIMITIVE_SPEAR_THROW_COOLDOWN = 100;
	private static final long PRIMITIVE_SPEAR_PAUSE_AFTER_THROW = 20;

	@SubscribeEvent
	public static void onPrimitiveSpearEntityUpdate(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (!entity.level().isClientSide && entity instanceof Mob mob) {
			ItemStack primitiveSpearItem = entity.getMainHandItem().is(JaamsArcheologyModItems.PRIMITIVE_SPEAR.get())
					? entity.getMainHandItem()
					: entity.getOffhandItem().is(JaamsArcheologyModItems.PRIMITIVE_SPEAR.get()) ? entity.getOffhandItem() : ItemStack.EMPTY;
			if (!primitiveSpearItem.isEmpty() && entity.level().random.nextDouble() < PRIMITIVE_SPEAR_THROW_PROBABILITY && entity.level().getGameTime() - lastPrimitiveSpearThrowTime.getOrDefault(entity, 0L) >= PRIMITIVE_SPEAR_THROW_COOLDOWN) {
				LivingEntity target = mob.getTarget();
				if (target != null) {
					double distanceToTarget = target.distanceTo(entity);
					Vec3 directionToTarget = new Vec3(target.getX() - entity.getX(), target.getEyeY() - entity.getEyeY(), target.getZ() - entity.getZ()).normalize();
					Vec3 entityLookDirection = entity.getLookAngle().normalize();
					double angle = directionToTarget.dot(entityLookDirection);
					if (distanceToTarget <= 16.0 && distanceToTarget >= 6.0 && angle > 0.5) {
						InteractionHand hand = primitiveSpearItem == entity.getMainHandItem() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
						entity.swing(hand, true);
						SpearProjectileEntity primitiveSpearProjectile = new SpearProjectileEntity(JaamsArcheologyModEntities.SPEAR_PROJECTILE.get(), entity, entity.level());
						primitiveSpearProjectile.setOwner(entity);
						primitiveSpearProjectile.weaponItem = primitiveSpearItem.copy();
						primitiveSpearProjectile.setPos(entity.getX() + directionToTarget.x, entity.getEyeY() - 0.1, entity.getZ() + directionToTarget.z);
						primitiveSpearProjectile.shoot(directionToTarget.x, directionToTarget.y, directionToTarget.z, 1.0F, 0.5F);
						entity.level().addFreshEntity(primitiveSpearProjectile);
						primitiveSpearItem.shrink(1);
						entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), JaamsArcheologyModSounds.PRIMITIVE_SPEAR_FIRED.get(), SoundSource.HOSTILE, 1.0F,
								1.0F + (entity.level().random.nextFloat() - entity.level().random.nextFloat()) * 0.2F);
						lastPrimitiveSpearThrowTime.put(entity, entity.level().getGameTime() + PRIMITIVE_SPEAR_PAUSE_AFTER_THROW);
						mob.getNavigation().stop();
					}
				}
			}
			if (entity.level().getGameTime() > lastPrimitiveSpearThrowTime.getOrDefault(entity, 0L)) {
				mob.getNavigation().recomputePath();
			}
		}
	}

	private static final long ABILITY_COOLDOWN = 200;
	private static final int DURABILITY_COST = 60;
	private static final Map<LivingEntity, Long> lastAbilityUseTime = new HashMap<>();

	@SubscribeEvent
	public static void SwordOfUndyingUpdate(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (!(entity instanceof Player) && !entity.level().isClientSide && JaamsArcheologyCommonConfiguration.SWORDOFUNDYING.get()) {
			ItemStack mainHandItem = entity.getMainHandItem();
			ItemStack offHandItem = entity.getOffhandItem();
			if (mainHandItem.getItem() instanceof SwordOfUndyingItem || offHandItem.getItem() instanceof SwordOfUndyingItem) {
				ItemStack itemstack = mainHandItem.getItem() instanceof SwordOfUndyingItem ? mainHandItem : offHandItem;
				long currentTime = entity.level().getGameTime();
				long lastUseTime = lastAbilityUseTime.getOrDefault(entity, 0L);
				boolean isLowHealth = entity.getHealth() <= entity.getMaxHealth() * 0.3;
				boolean isOnFire = entity.getRemainingFireTicks() > 0;
				boolean isFalling = entity.fallDistance > 3.0F;
				if ((isLowHealth || isOnFire || isFalling) && currentTime - lastUseTime >= ABILITY_COOLDOWN) {
					activateAbility(entity, itemstack);
					lastAbilityUseTime.put(entity, currentTime);
				}
			}
		}
	}

	private static void activateAbility(LivingEntity entity, ItemStack itemstack) {
		applyEffects(entity);
		InteractionHand hand = itemstack == entity.getMainHandItem() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		playTotemSound(entity.level(), entity.getX(), entity.getY(), entity.getZ());
		damageNearbyEntities(entity.level(), entity, entity.position().x(), entity.position().y(), entity.position().z(), itemstack);
		entity.swing(hand, true);
		itemstack.hurt(DURABILITY_COST, entity.getRandom(), null);
	}

	private static void applyEffects(LivingEntity entity) {
		if (entity.level() instanceof ServerLevel level) {
			Vec3 position = entity.position().add(0, 1, 0);
			level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, position.x(), position.y(), position.z(), 100, 0.2, 0.2, 0.2, 0.5);
			entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 0));
			entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 3));
			entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 4));
			if (entity.getRemainingFireTicks() > 0) {
				entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0));
			}
			if (entity.fallDistance > 3.0F || (entity.fallDistance > 0.5F && entity.fallDistance > entity.getMaxFallDistance())) {
				entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0));
			}
			if (entity.isInWaterOrBubble()) {
				entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0));
			}
		}
	}

	private static void playTotemSound(LevelAccessor world, double x, double y, double z) {
		if (world instanceof Level level) {
			BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
			level.playSound(null, pos, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.totem.use")), SoundSource.PLAYERS, 1, 1);
		}
	}

	private static void damageNearbyEntities(LevelAccessor world, Entity entity, double x, double y, double z, ItemStack itemstack) {
		Vec3 center = new Vec3(x, y, z);
		List<Entity> nearbyEntities = world.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(4)).stream().filter(e -> e != entity && (e instanceof LivingEntity || e instanceof Player))
				.filter(e -> !(e instanceof TamableAnimal tam && tam.isOwnedBy((LivingEntity) entity))).filter(e -> !isAlly(entity, e)).toList();
		int maxDurability = itemstack.getMaxDamage();
		int currentDurability = itemstack.getDamageValue();
		float damage = (maxDurability - currentDurability) * 0.01f;
		for (Entity target : nearbyEntities) {
			target.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.PLAYER_ATTACK), entity), damage);
		}
	}

	private static boolean isAlly(Entity sourceEntity, Entity targetEntity) {
		if (sourceEntity instanceof LivingEntity livingSource && targetEntity instanceof LivingEntity livingTarget) {
			return livingSource.isAlliedTo(livingTarget);
		}
		return false;
	}

	private static final double MAX_DEFLECT_AMOUNT = 70.0;

	@SubscribeEvent
	public static void onBrokenSwordFragmentEntityDamaged(LivingAttackEvent event) {
		if (event != null && event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof Player)) {
			LivingEntity livingEntity = (LivingEntity) event.getEntity();
			ItemStack mainHandItem = livingEntity.getMainHandItem();
			ItemStack offHandItem = livingEntity.getOffhandItem();
			ItemStack deflectingItem = ItemStack.EMPTY;
			if (mainHandItem.getItem() == JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()) {
				deflectingItem = mainHandItem;
			} else if (offHandItem.getItem() == JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()) {
				deflectingItem = offHandItem;
			}
			if (!deflectingItem.isEmpty()) {
				double deflectAmount = Math.min(event.getAmount(), MAX_DEFLECT_AMOUNT);
				deflectingItem.getOrCreateTag().putDouble("DeflectAmount", deflectAmount);
				DamageSource damageSource = event.getSource();
				Entity directEntity = damageSource.getDirectEntity();
				if (directEntity instanceof LivingEntity attacker) {
					deflectingItem.getOrCreateTag().putString("AttackerName", attacker.getName().getString());
				} else if (directEntity instanceof Projectile projectile) {
					if (projectile.getOwner() instanceof LivingEntity projectileOwner) {
						deflectingItem.getOrCreateTag().putString("AttackerName", projectileOwner.getName().getString());
					} else {
						deflectingItem.getOrCreateTag().putString("AttackerName", "Projectile");
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBrokenSwordFragmentEntityReflectDamage(LivingHurtEvent event) {
		Entity attacker = event.getSource().getEntity();
		if (attacker instanceof LivingEntity attackingEntity && !(attackingEntity instanceof Player)) {
			ItemStack mainHandItem = attackingEntity.getMainHandItem();
			ItemStack offHandItem = attackingEntity.getOffhandItem();
			processDeflectAttack(event, attackingEntity, mainHandItem);
			processDeflectAttack(event, attackingEntity, offHandItem);
		}
	}

	@SubscribeEvent
	public static void onBrokenSwordFragmentEntityTickUpdate(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (!(entity instanceof Player)) {
			updateItemColor(entity.getMainHandItem());
			updateItemColor(entity.getOffhandItem());
		}
	}

	private static void updateItemColor(ItemStack itemstack) {
		if (itemstack.getItem() instanceof IDyeableItem) {
			double deflectAmount = itemstack.getOrCreateTag().getDouble("DeflectAmount");
			if (deflectAmount > 20) {
				((IDyeableItem) itemstack.getItem()).setColor(itemstack, 0xFF0000); // Rojo
			} else if (deflectAmount > 10) {
				((IDyeableItem) itemstack.getItem()).setColor(itemstack, 0xFFA500); // Naranja
			} else if (deflectAmount > 1) {
				((IDyeableItem) itemstack.getItem()).setColor(itemstack, 0xFFFF00); // Amarillo
			} else {
				((IDyeableItem) itemstack.getItem()).setColor(itemstack, 0x44CC44); // Color original
			}
		}
	}

	private static void processDeflectAttack(LivingHurtEvent event, LivingEntity attacker, ItemStack deflectingItem) {
		if (deflectingItem.getItem() == JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()) {
			double deflectAmount = deflectingItem.getOrCreateTag().getDouble("DeflectAmount");
			if (deflectAmount > 0) {
				event.setAmount((float) (event.getAmount() + deflectAmount));
				int durabilityDamage = (int) (deflectAmount * 0.5);
				handleItemDurability(deflectingItem, attacker, durabilityDamage);
				deflectingItem.getOrCreateTag().putDouble("DeflectAmount", 0);
			}
		}
	}

	private static void handleItemDurability(ItemStack itemstack, LivingEntity entity, int damage) {
		itemstack.hurtAndBreak(damage, entity, (e) -> e.broadcastBreakEvent(entity.getUsedItemHand()));
	}
}
