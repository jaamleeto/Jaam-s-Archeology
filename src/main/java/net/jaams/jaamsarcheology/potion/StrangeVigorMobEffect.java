
package net.jaams.jaamsarcheology.potion;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.jaams.jaamsarcheology.procedures.StrangeVigorEffectStartedappliedProcedure;
import net.jaams.jaamsarcheology.procedures.StrangeVigorEffectExpiresProcedure;

public class StrangeVigorMobEffect extends MobEffect {
	public StrangeVigorMobEffect() {
		super(MobEffectCategory.NEUTRAL, -6750055);
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		StrangeVigorEffectStartedappliedProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.removeAttributeModifiers(entity, attributeMap, amplifier);
		StrangeVigorEffectExpiresProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
