package net.jaams.jaamsarcheology.procedures;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.Position;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModEntities;
import net.jaams.jaamsarcheology.entity.SpearFragmentProjectileEntity;

public class SpearFragmentDispenseBehaviorProcedure extends AbstractProjectileDispenseBehavior {
	@Override
	protected Projectile getProjectile(Level level, Position pos, ItemStack stack) {
		SpearFragmentProjectileEntity entity = new SpearFragmentProjectileEntity(JaamsArcheologyModEntities.SPEAR_FRAGMENT_PROJECTILE.get(), level);
		entity.weaponItem = stack.copy(); // Copia el stack para retener sus datos
		entity.setPos(pos.x(), pos.y(), pos.z());
		entity.pickup = AbstractArrow.Pickup.ALLOWED; // Solo se puede recoger en modos diferentes al creativo
		return entity;
	}
}
