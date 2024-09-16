
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.jaams.jaamsarcheology.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import net.jaams.jaamsarcheology.item.TraitorIdolItem;
import net.jaams.jaamsarcheology.item.SwordOfUndyingItem;
import net.jaams.jaamsarcheology.item.StrangeIdolItem;
import net.jaams.jaamsarcheology.item.SpearFragmentItem;
import net.jaams.jaamsarcheology.item.SkeletalIdolItem;
import net.jaams.jaamsarcheology.item.ShinyAmmoniteItem;
import net.jaams.jaamsarcheology.item.RottenIdolItem;
import net.jaams.jaamsarcheology.item.PrimordialGoldItem;
import net.jaams.jaamsarcheology.item.PrimordialGoldFragmentItem;
import net.jaams.jaamsarcheology.item.PrimitiveSpearItem;
import net.jaams.jaamsarcheology.item.PigmanIdolItem;
import net.jaams.jaamsarcheology.item.PetrifiedAmmoniteItem;
import net.jaams.jaamsarcheology.item.OssifiedAmmoniteItem;
import net.jaams.jaamsarcheology.item.MusicDiscNamelessItem;
import net.jaams.jaamsarcheology.item.MagnifyingGlassItem;
import net.jaams.jaamsarcheology.item.ExplosiveIdolItem;
import net.jaams.jaamsarcheology.item.DiscFragmentNamelessItem;
import net.jaams.jaamsarcheology.item.BrokenSwordFragmentItem;
import net.jaams.jaamsarcheology.item.ArcheoTrowelItem;
import net.jaams.jaamsarcheology.item.ArcheoPickaxeItem;
import net.jaams.jaamsarcheology.item.ArcheoHammerItem;
import net.jaams.jaamsarcheology.JaamsArcheologyMod;

public class JaamsArcheologyModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, JaamsArcheologyMod.MODID);
	public static final RegistryObject<Item> MAGNIFYING_GLASS = REGISTRY.register("magnifying_glass", () -> new MagnifyingGlassItem());
	public static final RegistryObject<Item> ARCHEO_PICKAXE = REGISTRY.register("archeo_pickaxe", () -> new ArcheoPickaxeItem());
	public static final RegistryObject<Item> ARCHEO_TROWEL = REGISTRY.register("archeo_trowel", () -> new ArcheoTrowelItem());
	public static final RegistryObject<Item> ARCHEO_HAMMER = REGISTRY.register("archeo_hammer", () -> new ArcheoHammerItem());
	public static final RegistryObject<Item> PRIMITIVE_SPEAR = REGISTRY.register("primitive_spear", () -> new PrimitiveSpearItem());
	public static final RegistryObject<Item> SPEAR_FRAGMENT = REGISTRY.register("spear_fragment", () -> new SpearFragmentItem());
	public static final RegistryObject<Item> SWORD_OF_UNDYING = REGISTRY.register("sword_of_undying", () -> new SwordOfUndyingItem());
	public static final RegistryObject<Item> PRIMORDIAL_GOLD = REGISTRY.register("primordial_gold", () -> new PrimordialGoldItem());
	public static final RegistryObject<Item> PRIMORDIAL_GOLD_FRAGMENT = REGISTRY.register("primordial_gold_fragment", () -> new PrimordialGoldFragmentItem());
	public static final RegistryObject<Item> PETRIFIED_AMMONITE = REGISTRY.register("petrified_ammonite", () -> new PetrifiedAmmoniteItem());
	public static final RegistryObject<Item> OSSIFIED_AMMONITE = REGISTRY.register("ossified_ammonite", () -> new OssifiedAmmoniteItem());
	public static final RegistryObject<Item> SHINY_AMMONITE = REGISTRY.register("shiny_ammonite", () -> new ShinyAmmoniteItem());
	public static final RegistryObject<Item> BROKEN_SWORD_FRAGMENT = REGISTRY.register("broken_sword_fragment", () -> new BrokenSwordFragmentItem());
	public static final RegistryObject<Item> MUSIC_DISC_NAMELESS = REGISTRY.register("music_disc_nameless", () -> new MusicDiscNamelessItem());
	public static final RegistryObject<Item> DISC_FRAGMENT_NAMELESS = REGISTRY.register("disc_fragment_nameless", () -> new DiscFragmentNamelessItem());
	public static final RegistryObject<Item> EXPLOSIVE_IDOL = REGISTRY.register("explosive_idol", () -> new ExplosiveIdolItem());
	public static final RegistryObject<Item> STRANGE_IDOL = REGISTRY.register("strange_idol", () -> new StrangeIdolItem());
	public static final RegistryObject<Item> PIGMAN_IDOL = REGISTRY.register("pigman_idol", () -> new PigmanIdolItem());
	public static final RegistryObject<Item> SKELETAL_IDOL = REGISTRY.register("skeletal_idol", () -> new SkeletalIdolItem());
	public static final RegistryObject<Item> ROTTEN_IDOL = REGISTRY.register("rotten_idol", () -> new RottenIdolItem());
	public static final RegistryObject<Item> TRAITOR_IDOL = REGISTRY.register("traitor_idol", () -> new TraitorIdolItem());
	// Start of user code block custom items
	// End of user code block custom items
}
