package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModMobEffects;

import javax.annotation.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class ZombieIdolGlobalProcedure {
	@SubscribeEvent
	public static void onEntitySetsAttackTarget(LivingChangeTargetEvent event) {
		execute(event, event.getEntity(), event.getNewTarget());
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity instanceof Zombie || entity instanceof Husk || entity instanceof Drowned) {
			Mob mob = (Mob) entity;
			LivingEntity targetEntity = mob.getTarget();
			if (targetEntity != null && targetEntity.hasEffect(JaamsArcheologyModMobEffects.ROTTEN_VIGOR.get())) {
				mob.setTarget(null);
			}
		}
	}

	public static void execute(Entity entity, Entity targetEntity) {
		execute(null, entity, targetEntity);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity targetEntity) {
		if (entity == null || targetEntity == null)
			return;
		if (entity instanceof Zombie || entity instanceof Husk || entity instanceof Drowned) {
			if (targetEntity instanceof LivingEntity livingTargetEntity && livingTargetEntity.hasEffect(JaamsArcheologyModMobEffects.ROTTEN_VIGOR.get())) {
				if (event instanceof LivingChangeTargetEvent targetEvent) {
					targetEvent.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onZombieEntityAttacked(LivingAttackEvent event) {
		Entity defender = event.getEntity();
		Entity attacker = event.getSource().getEntity();
		if (defender instanceof LivingEntity livingDefender && attacker instanceof LivingEntity livingAttacker && livingDefender.hasEffect(JaamsArcheologyModMobEffects.ROTTEN_VIGOR.get())) {
			Vec3 position = defender.position();
			LevelAccessor world = defender.level();
			List<Mob> zombies = world.getEntitiesOfClass(Mob.class, new AABB(position, position).inflate(15), e -> (e instanceof Zombie || e instanceof Husk || e instanceof Drowned) && e != defender);
			for (Mob zombie : zombies) {
				zombie.setTarget(livingAttacker);
			}
		}
	}
}
