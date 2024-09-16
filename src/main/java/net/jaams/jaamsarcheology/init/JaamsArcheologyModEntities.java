
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.jaams.jaamsarcheology.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

import net.jaams.jaamsarcheology.entity.SpearProjectileEntity;
import net.jaams.jaamsarcheology.entity.SpearFragmentProjectileEntity;
import net.jaams.jaamsarcheology.entity.AmmoniteProjectileEntity;
import net.jaams.jaamsarcheology.JaamsArcheologyMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class JaamsArcheologyModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, JaamsArcheologyMod.MODID);
	public static final RegistryObject<EntityType<SpearProjectileEntity>> SPEAR_PROJECTILE = register("spear_projectile", EntityType.Builder.<SpearProjectileEntity>of(SpearProjectileEntity::new, MobCategory.MISC)
			.setCustomClientFactory(SpearProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<SpearFragmentProjectileEntity>> SPEAR_FRAGMENT_PROJECTILE = register("spear_fragment_projectile",
			EntityType.Builder.<SpearFragmentProjectileEntity>of(SpearFragmentProjectileEntity::new, MobCategory.MISC).setCustomClientFactory(SpearFragmentProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64)
					.setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<AmmoniteProjectileEntity>> AMMONITE_PROJECTILE = register("ammonite_projectile", EntityType.Builder.<AmmoniteProjectileEntity>of(AmmoniteProjectileEntity::new, MobCategory.MISC)
			.setCustomClientFactory(AmmoniteProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}
}
