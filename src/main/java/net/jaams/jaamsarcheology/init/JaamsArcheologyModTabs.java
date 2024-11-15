
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.jaams.jaamsarcheology.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.jaams.jaamsarcheology.JaamsArcheologyMod;

public class JaamsArcheologyModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JaamsArcheologyMod.MODID);
	public static final RegistryObject<CreativeModeTab> ARCHEOLOGY = REGISTRY.register("archeology",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.jaams_archeology.archeology")).icon(() -> new ItemStack(JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get())).displayItems((parameters, tabData) -> {
				tabData.accept(JaamsArcheologyModItems.MAGNIFYING_GLASS.get());
				tabData.accept(JaamsArcheologyModItems.ARCHEO_PICKAXE.get());
				tabData.accept(JaamsArcheologyModItems.ARCHEO_TROWEL.get());
				tabData.accept(JaamsArcheologyModItems.ARCHEO_HAMMER.get());
				tabData.accept(JaamsArcheologyModItems.PRIMORDIAL_GOLD.get());
				tabData.accept(JaamsArcheologyModItems.PRIMORDIAL_GOLD_FRAGMENT.get());
				tabData.accept(JaamsArcheologyModItems.OSSIFIED_AMMONITE.get());
				tabData.accept(JaamsArcheologyModItems.SHINY_AMMONITE.get());
				tabData.accept(JaamsArcheologyModItems.PETRIFIED_AMMONITE.get());
				tabData.accept(JaamsArcheologyModItems.GUARDIAN_IDOL.get());
				tabData.accept(JaamsArcheologyModItems.TRAITOR_IDOL.get());
				tabData.accept(JaamsArcheologyModItems.ROTTEN_IDOL.get());
				tabData.accept(JaamsArcheologyModItems.STRANGE_IDOL.get());
				tabData.accept(JaamsArcheologyModItems.SKELETAL_IDOL.get());
				tabData.accept(JaamsArcheologyModItems.EXPLOSIVE_IDOL.get());
				tabData.accept(JaamsArcheologyModItems.ARTHROPODS_IDOL.get());
				tabData.accept(JaamsArcheologyModItems.PRIMITIVE_SPEAR.get());
				tabData.accept(JaamsArcheologyModItems.SPEAR_FRAGMENT.get());
				tabData.accept(JaamsArcheologyModItems.SWORD_OF_UNDYING.get());
				tabData.accept(JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get());
				tabData.accept(JaamsArcheologyModItems.MUSIC_DISC_NAMELESS.get());
				tabData.accept(JaamsArcheologyModItems.DISC_FRAGMENT_NAMELESS.get());
				tabData.accept(JaamsArcheologyModBlocks.PRIMORDIAL_GOLD_BLOCK.get().asItem());
			})

					.build());
}
