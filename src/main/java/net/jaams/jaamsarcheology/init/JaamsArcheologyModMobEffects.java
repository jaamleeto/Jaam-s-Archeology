
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.jaams.jaamsarcheology.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import net.jaams.jaamsarcheology.potion.TraitorVigorMobEffect;
import net.jaams.jaamsarcheology.potion.StrangeVigorMobEffect;
import net.jaams.jaamsarcheology.potion.SkeletalVigorMobEffect;
import net.jaams.jaamsarcheology.potion.RottenVigorMobEffect;
import net.jaams.jaamsarcheology.potion.PigmanVigorMobEffect;
import net.jaams.jaamsarcheology.potion.ExplosiveVigorMobEffect;
import net.jaams.jaamsarcheology.JaamsArcheologyMod;

public class JaamsArcheologyModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, JaamsArcheologyMod.MODID);
	public static final RegistryObject<MobEffect> EXPLOSIVE_VIGOR = REGISTRY.register("explosive_vigor", () -> new ExplosiveVigorMobEffect());
	public static final RegistryObject<MobEffect> STRANGE_VIGOR = REGISTRY.register("strange_vigor", () -> new StrangeVigorMobEffect());
	public static final RegistryObject<MobEffect> PIGMAN_VIGOR = REGISTRY.register("pigman_vigor", () -> new PigmanVigorMobEffect());
	public static final RegistryObject<MobEffect> SKELETAL_VIGOR = REGISTRY.register("skeletal_vigor", () -> new SkeletalVigorMobEffect());
	public static final RegistryObject<MobEffect> ROTTEN_VIGOR = REGISTRY.register("rotten_vigor", () -> new RottenVigorMobEffect());
	public static final RegistryObject<MobEffect> TRAITOR_VIGOR = REGISTRY.register("traitor_vigor", () -> new TraitorVigorMobEffect());
}
