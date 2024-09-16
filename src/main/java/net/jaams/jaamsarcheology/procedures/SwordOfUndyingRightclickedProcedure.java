package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

import net.jaams.jaamsarcheology.network.JaamsArcheologyModVariables;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyServerConfiguration;
import net.jaams.jaamsarcheology.JaamsArcheologyMod;

import java.util.List;

public class SwordOfUndyingRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null || !JaamsArcheologyServerConfiguration.SWORDOFUNDYING.get())
			return;
		if (entity.isShiftKeyDown()) {
			if (world.isClientSide()) {
				Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
			} else if (entity instanceof Player player) {
				player.getCooldowns().addCooldown(itemstack.getItem(), 300);
				applyEffects((LivingEntity) entity);
				playTotemSound(world, x, y, z);
				damageNearbyEntities(world, entity, x, y, z, itemstack);
				shakeEntity(entity);
			}
		}
	}

	private static void applyEffects(LivingEntity entity) {
		ServerLevel level = (ServerLevel) entity.level();
		Vec3 position = entity.position().add(0, 1, 0);
		level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, position.x(), position.y(), position.z(), 100, 0.2, 0.2, 0.2, 0.5);
		// Efectos principales
		entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 0));
		entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 3));
		entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 4));
		// Aplicar resistencia al fuego si el jugador está en llamas
		if (entity.getRemainingFireTicks() > 0) {
			entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0));
		}
		// Lógica más robusta para la caída
		if (entity.fallDistance > 3.0F || (entity.fallDistance > 0.5F && entity.fallDistance > entity.getMaxFallDistance())) {
			entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0));
		}
		// Aplicar respiración bajo el agua si el jugador está bajo el agua
		if (entity.isInWaterOrBubble()) {
			entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0));
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
				.filter(e -> !(e instanceof TamableAnimal tam && tam.isOwnedBy((LivingEntity) entity))).toList();
		// Calcula el daño como 1% de la durabilidad del arma
		int maxDurability = itemstack.getMaxDamage();
		int currentDurability = itemstack.getDamageValue();
		float damage = (maxDurability - currentDurability) * 0.01f;
		for (Entity target : nearbyEntities) {
			target.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.PLAYER_ATTACK), entity), damage);
		}
	}

	private static void shakeEntity(Entity entity) {
		entity.getCapability(JaamsArcheologyModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
			capability.Shake = true;
			capability.syncPlayerVariables(entity);
			JaamsArcheologyMod.queueServerWork(3, () -> {
				capability.Shake = false;
				capability.syncPlayerVariables(entity);
			});
		});
	}
}
