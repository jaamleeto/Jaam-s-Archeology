
package net.jaams.jaamsarcheology.potion;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.jaams.jaamsarcheology.procedures.RottenVigorEffectStartedappliedProcedure;
import net.jaams.jaamsarcheology.procedures.RottenVigorEffectExpiresProcedure;

public class RottenVigorMobEffect extends MobEffect {
	public RottenVigorMobEffect() {
		super(MobEffectCategory.NEUTRAL, -16751053);
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		RottenVigorEffectStartedappliedProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.removeAttributeModifiers(entity, attributeMap, amplifier);
		RottenVigorEffectExpiresProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
