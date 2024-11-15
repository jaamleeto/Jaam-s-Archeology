package net.jaams.jaamsarcheology.procedures;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.Position;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModEntities;
import net.jaams.jaamsarcheology.entity.AmmoniteProjectileEntity;

public class AmmoniteDispenseBehaviorProcedure extends AbstractProjectileDispenseBehavior {
	@Override
	protected Projectile getProjectile(Level level, Position pos, ItemStack stack) {
		AmmoniteProjectileEntity entity = new AmmoniteProjectileEntity(JaamsArcheologyModEntities.AMMONITE_PROJECTILE.get(), level);
		entity.ammoniteItem = stack.copy(); // Copia el stack para retener sus datos
		entity.setPos(pos.x(), pos.y(), pos.z());
		return entity;
	}
}
