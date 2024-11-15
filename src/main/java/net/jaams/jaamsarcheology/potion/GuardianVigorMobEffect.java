
package net.jaams.jaamsarcheology.potion;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.jaams.jaamsarcheology.procedures.GuardianVigorEffectStartedappliedProcedure;
import net.jaams.jaamsarcheology.procedures.GuardianVigorEffectExpiresProcedure;

public class GuardianVigorMobEffect extends MobEffect {
	public GuardianVigorMobEffect() {
		super(MobEffectCategory.NEUTRAL, -26215);
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		GuardianVigorEffectStartedappliedProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.removeAttributeModifiers(entity, attributeMap, amplifier);
		GuardianVigorEffectExpiresProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
