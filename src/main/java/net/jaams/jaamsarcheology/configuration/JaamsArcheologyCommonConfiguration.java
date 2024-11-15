package net.jaams.jaamsarcheology.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.Arrays;

public class JaamsArcheologyCommonConfiguration {
	// Builder and Spec (for common configuration)
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	// Effects
	public static final ForgeConfigSpec.ConfigValue<Boolean> EXPLOSIVEVIGOR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> STRANGEVIGOR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SKELETALVIGOR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> ROTTENVIGOR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> TRAITORVIGOR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> ARTHROPODSVIGOR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> GUARDIANVIGOR;
	// Weapon mechanics
	public static final ForgeConfigSpec.ConfigValue<Boolean> DEFLECT;
	// Projectile behavior
	public static final ForgeConfigSpec.ConfigValue<Boolean> DROPASITEM;
	public static final ForgeConfigSpec.ConfigValue<Boolean> AMMONITEDROPS;
	public static final ForgeConfigSpec.ConfigValue<Boolean> DROPASITEMLAVA;
	// Ammonite Items
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> PETRIFIED_AMMONITE_ITEMS;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> OSSIFIED_AMMONITE_ITEMS;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SHINY_AMMONITE_ITEMS;
	// Projectile despawn times
	public static final ForgeConfigSpec.ConfigValue<Double> PRIMITIVESPEARDESPAWN;
	public static final ForgeConfigSpec.ConfigValue<Double> SPEARFRAGMENTDESPAWN;
	// Ranged item behavior
	public static final ForgeConfigSpec.ConfigValue<Boolean> THROWAMMONITE;
	public static final ForgeConfigSpec.ConfigValue<Boolean> THROWPRIMITIVESPEAR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> THROWSPEARFRAGMENT;
	// Right-click abilities
	public static final ForgeConfigSpec.ConfigValue<Boolean> SWORDOFUNDYING;
	public static final ForgeConfigSpec.ConfigValue<Boolean> IDOLS;
	// Blocking mechanics
	public static final ForgeConfigSpec.ConfigValue<Boolean> SWORDOFUNDYINGBLOCK;
	public static final ForgeConfigSpec.ConfigValue<Boolean> BROKENSWORDBLOCK;
	static {
		// Effects Handler
		BUILDER.push("Effects Handler");
		EXPLOSIVEVIGOR = BUILDER.comment("Enables or disables Explosive Vigor.").define("Explosive Vigor", true);
		STRANGEVIGOR = BUILDER.comment("Enables or disables Strange Vigor.").define("Strange Vigor", true);
		SKELETALVIGOR = BUILDER.comment("Enables or disables Skeletal Vigor.").define("Skeletal Vigor", true);
		ROTTENVIGOR = BUILDER.comment("Enables or disables Rotten Vigor.").define("Rotten Vigor", true);
		TRAITORVIGOR = BUILDER.comment("Enables or disables Traitor's Vigor.").define("Traitor's Vigor", true);
		ARTHROPODSVIGOR = BUILDER.comment("Enables or disables Arthropod's Vigor.").define("Arthropod's Vigor", true);
		GUARDIANVIGOR = BUILDER.comment("Enables or disables Guardian's Vigor.").define("Guardian's Vigor", true);
		BUILDER.pop();
		// Weapon Mechanics
		BUILDER.push("Weapon Mechanics");
		DEFLECT = BUILDER.comment("Enables or disables the deflect ability.").define("Deflect", true);
		BUILDER.pop();
		// Projectile Behavior
		BUILDER.push("Projectile Behavior");
		// Ammonite Items
		BUILDER.push("Ammonite Items");
		PETRIFIED_AMMONITE_ITEMS = BUILDER.comment("List of petrified ammonite items with weight in format: modid:item, weight:x.x").defineList("petrified_ammonite_items", Arrays.asList("minecraft:stick, weight:2.0", "minecraft:stone"),
				obj -> obj instanceof String);
		OSSIFIED_AMMONITE_ITEMS = BUILDER.comment("List of ossified ammonite items with weight in format: modid:item, weight:x.x").defineList("ossified_ammonite_items", Arrays.asList("minecraft:bone, weight:3.0", "minecraft:coal"),
				obj -> obj instanceof String);
		SHINY_AMMONITE_ITEMS = BUILDER.comment("List of shiny ammonite items with weight in format: modid:item, weight:x.x").defineList("shiny_ammonite_items", Arrays.asList("minecraft:diamond, weight:5.0", "minecraft:gold_ingot"),
				obj -> obj instanceof String);
		BUILDER.pop();
		DROPASITEM = BUILDER.comment("Enables or disables projectiles dropping as items.").define("Projectile Drop As Item", false);
		AMMONITEDROPS = BUILDER.comment("Enables or disables ammonites dropping items.").define("Ammonites Drops Items", true);
		DROPASITEMLAVA = BUILDER.comment("Enables or disables projectiles dropping as items in lava.").define("Projectile Drop As Item In Lava", true);
		// Projectile Despawn Time Sub Category
		BUILDER.push("Projectile Despawn Time");
		PRIMITIVESPEARDESPAWN = BUILDER.comment("Despawn time for the primitive spear. Must be between 100 and 72000.").defineInRange("Primitive Spear", 1200.0, 100.0, 72000.0);
		SPEARFRAGMENTDESPAWN = BUILDER.comment("Despawn time for the spear fragment. Must be between 100 and 72000.").defineInRange("Spear Fragment", 800.0, 100.0, 72000.0);
		BUILDER.pop();
		BUILDER.pop();
		// Ranged Items Behavior
		BUILDER.push("Ranged Items Behavior");
		THROWAMMONITE = BUILDER.comment("Enables or disables throwable ammonites.").define("Throwable Ammonites", true);
		THROWPRIMITIVESPEAR = BUILDER.comment("Enables or disables throwable primitive spears.").define("Throwable Primitive Spear", true);
		THROWSPEARFRAGMENT = BUILDER.comment("Enables or disables throwable spear fragments.").define("Throwable Spear Fragment", true);
		BUILDER.pop();
		// Right Click Abilities
		BUILDER.push("Right Click Abilities");
		SWORDOFUNDYING = BUILDER.comment("Enables or disables the sword of undying ability.").define("Sword of Undying", true);
		IDOLS = BUILDER.comment("Enables or disables idols abilities.").define("Idols", true);
		BUILDER.pop();
		// Can Block Attacks
		BUILDER.push("Can Block Attacks");
		SWORDOFUNDYINGBLOCK = BUILDER.comment("Enables or disables blocking with the sword of undying.").define("Sword Of Undying", true);
		BROKENSWORDBLOCK = BUILDER.comment("Enables or disables blocking with the broken sword fragment.").define("Broken Sword Fragment", true);
		BUILDER.pop();
		// Build the configuration
		SPEC = BUILDER.build();
	}
}
