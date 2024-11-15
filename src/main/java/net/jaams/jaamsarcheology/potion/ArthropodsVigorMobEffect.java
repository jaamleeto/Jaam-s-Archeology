
package net.jaams.jaamsarcheology.potion;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.jaams.jaamsarcheology.procedures.ArthropodsVigorEffectStartedappliedProcedure;
import net.jaams.jaamsarcheology.procedures.ArthropodsVigorEffectExpiresProcedure;

public class ArthropodsVigorMobEffect extends MobEffect {
	public ArthropodsVigorMobEffect() {
		super(MobEffectCategory.NEUTRAL, -26368);
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		ArthropodsVigorEffectStartedappliedProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.removeAttributeModifiers(entity, attributeMap, amplifier);
		ArthropodsVigorEffectExpiresProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
