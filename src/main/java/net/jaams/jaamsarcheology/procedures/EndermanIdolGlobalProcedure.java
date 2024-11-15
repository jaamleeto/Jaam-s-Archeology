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
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModMobEffects;

import javax.annotation.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class EndermanIdolGlobalProcedure {
	@SubscribeEvent
	public static void onEntitySetsAttackTarget(LivingChangeTargetEvent event) {
		execute(event, event.getEntity(), event.getNewTarget());
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity instanceof EnderMan) {
			Mob mob = (Mob) entity;
			LivingEntity targetEntity = mob.getTarget();
			if (targetEntity != null && targetEntity.hasEffect(JaamsArcheologyModMobEffects.STRANGE_VIGOR.get())) {
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
		if (entity instanceof EnderMan) {
			if (targetEntity instanceof LivingEntity livingTargetEntity && livingTargetEntity.hasEffect(JaamsArcheologyModMobEffects.STRANGE_VIGOR.get())) {
				if (event instanceof LivingChangeTargetEvent targetEvent) {
					targetEvent.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEndEntityAttacked(LivingAttackEvent event) {
		Entity defender = event.getEntity();
		Entity attacker = event.getSource().getEntity();
		if (defender instanceof LivingEntity livingDefender && attacker instanceof LivingEntity livingAttacker && livingDefender.hasEffect(JaamsArcheologyModMobEffects.STRANGE_VIGOR.get())) {
			Vec3 position = defender.position();
			LevelAccessor world = defender.level();
			List<Mob> endEntities = world.getEntitiesOfClass(Mob.class, new AABB(position, position).inflate(15), e -> (e instanceof EnderMan || e instanceof Endermite) && e != defender);
			for (Mob endEntity : endEntities) {
				endEntity.setTarget(livingAttacker);
			}
		}
	}
}
