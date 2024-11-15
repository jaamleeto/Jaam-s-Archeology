package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModMobEffects;

import javax.annotation.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class GuardianIdolGlobalProcedure {
	@SubscribeEvent
	public static void onEntitySetsAttackTarget(LivingChangeTargetEvent event) {
		execute(event, event.getEntity(), event.getNewTarget());
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity instanceof Guardian || entity instanceof ElderGuardian) {
			Mob mob = (Mob) entity;
			LivingEntity targetEntity = mob.getTarget();
			if (targetEntity != null && targetEntity.hasEffect(JaamsArcheologyModMobEffects.GUARDIAN_VIGOR.get())) {
				mob.setTarget(null);
			}
		}
		if (entity.hasEffect(JaamsArcheologyModMobEffects.GUARDIAN_VIGOR.get())) {
			if (entity.hasEffect(MobEffects.DIG_SLOWDOWN)) {
				entity.removeEffect(MobEffects.DIG_SLOWDOWN);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity.hasEffect(JaamsArcheologyModMobEffects.GUARDIAN_VIGOR.get())) {
			Entity source = event.getSource().getEntity();
			if (source instanceof Guardian || source instanceof ElderGuardian) {
				event.setCanceled(true);
			} else {
				List<Guardian> nearbyGuardians = entity.level().getEntitiesOfClass(Guardian.class, entity.getBoundingBox().inflate(10));
				List<ElderGuardian> nearbyElderGuardians = entity.level().getEntitiesOfClass(ElderGuardian.class, entity.getBoundingBox().inflate(10));
				for (Guardian guardian : nearbyGuardians) {
					if (guardian.getTarget() == null || guardian.getTarget() != entity) {
						guardian.setTarget((LivingEntity) source);
					}
				}
				for (ElderGuardian elderGuardian : nearbyElderGuardians) {
					if (elderGuardian.getTarget() == null || elderGuardian.getTarget() != entity) {
						elderGuardian.setTarget((LivingEntity) source);
					}
				}
			}
		}
	}

	public static void execute(Entity entity, Entity targetEntity) {
		execute(null, entity, targetEntity);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity targetEntity) {
		if (entity == null || targetEntity == null)
			return;
		if (entity instanceof Guardian || entity instanceof ElderGuardian) {
			if (targetEntity instanceof LivingEntity livingTargetEntity && livingTargetEntity.hasEffect(JaamsArcheologyModMobEffects.GUARDIAN_VIGOR.get())) {
				if (event instanceof LivingChangeTargetEvent targetEvent) {
					targetEvent.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onGuardianEntityAttacked(LivingAttackEvent event) {
		Entity defender = event.getEntity();
		Entity attacker = event.getSource().getEntity();
		if (defender instanceof LivingEntity livingDefender && attacker instanceof LivingEntity livingAttacker && livingDefender.hasEffect(JaamsArcheologyModMobEffects.GUARDIAN_VIGOR.get())) {
			Vec3 position = defender.position();
			LevelAccessor world = defender.level();
			List<Mob> guardians = world.getEntitiesOfClass(Mob.class, new AABB(position, position).inflate(15), e -> (e instanceof Guardian || e instanceof ElderGuardian) && e != defender);
			for (Mob guardian : guardians) {
				guardian.setTarget(livingAttacker);
			}
		}
	}
}
