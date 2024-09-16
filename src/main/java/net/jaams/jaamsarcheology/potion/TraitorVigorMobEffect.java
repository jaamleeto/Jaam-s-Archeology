
package net.jaams.jaamsarcheology.potion;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.jaams.jaamsarcheology.procedures.TraitorVigorEffectStartedappliedProcedure;
import net.jaams.jaamsarcheology.procedures.TraitorVigorEffectExpiresProcedure;

public class TraitorVigorMobEffect extends MobEffect {
	public TraitorVigorMobEffect() {
		super(MobEffectCategory.NEUTRAL, -3407872);
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		TraitorVigorEffectStartedappliedProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.removeAttributeModifiers(entity, attributeMap, amplifier);
		TraitorVigorEffectExpiresProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
