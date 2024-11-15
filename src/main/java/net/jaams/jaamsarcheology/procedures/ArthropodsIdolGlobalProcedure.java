package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModMobEffects;

import javax.annotation.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class ArthropodsIdolGlobalProcedure {
	@SubscribeEvent
	public static void onEntitySetsAttackTarget(LivingChangeTargetEvent event) {
		execute(event, event.getEntity(), event.getNewTarget());
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity.getMobType() == MobType.ARTHROPOD) {
			Mob mob = (Mob) entity;
			LivingEntity targetEntity = mob.getTarget();
			if (targetEntity != null && targetEntity.hasEffect(JaamsArcheologyModMobEffects.ARTHROPODS_VIGOR.get())) {
				mob.setTarget(null);
			}
		}
		if (entity.hasEffect(JaamsArcheologyModMobEffects.ARTHROPODS_VIGOR.get())) {
			if (entity.hasEffect(MobEffects.POISON)) {
				entity.removeEffect(MobEffects.POISON);
			}
		}
	}

	public static void execute(Entity entity, Entity targetEntity) {
		execute(null, entity, targetEntity);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity targetEntity) {
		if (entity == null || targetEntity == null)
			return;
		if (entity instanceof LivingEntity livingEntity && livingEntity.getMobType() == MobType.ARTHROPOD) {
			if (targetEntity instanceof LivingEntity livingTargetEntity && livingTargetEntity.hasEffect(JaamsArcheologyModMobEffects.ARTHROPODS_VIGOR.get())) {
				if (event instanceof LivingChangeTargetEvent targetEvent) {
					targetEvent.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onArthropodEntityDamage(LivingDamageEvent event) {
		Entity source = event.getSource().getEntity();
		LivingEntity target = event.getEntity();
		if (source instanceof LivingEntity && ((LivingEntity) source).getMobType() == MobType.ARTHROPOD) {
			if (target.hasEffect(JaamsArcheologyModMobEffects.ARTHROPODS_VIGOR.get())) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onArthropodEntityAttacked(LivingAttackEvent event) {
		Entity defender = event.getEntity();
		Entity attacker = event.getSource().getEntity();
		if (defender instanceof LivingEntity livingDefender && attacker instanceof LivingEntity livingAttacker && livingDefender.hasEffect(JaamsArcheologyModMobEffects.ARTHROPODS_VIGOR.get())) {
			Vec3 position = defender.position();
			LevelAccessor world = defender.level();
			List<Mob> arthropods = world.getEntitiesOfClass(Mob.class, new AABB(position, position).inflate(15), e -> e.getMobType() == MobType.ARTHROPOD && e != defender);
			for (Mob arthropod : arthropods) {
				arthropod.setTarget(livingAttacker);
			}
		}
	}
}
