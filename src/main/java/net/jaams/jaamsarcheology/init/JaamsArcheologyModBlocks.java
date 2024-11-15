
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.jaams.jaamsarcheology.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import net.jaams.jaamsarcheology.block.PrimordialGoldBlockBlock;
import net.jaams.jaamsarcheology.JaamsArcheologyMod;

public class JaamsArcheologyModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, JaamsArcheologyMod.MODID);
	public static final RegistryObject<Block> PRIMORDIAL_GOLD_BLOCK = REGISTRY.register("primordial_gold_block", () -> new PrimordialGoldBlockBlock());
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
