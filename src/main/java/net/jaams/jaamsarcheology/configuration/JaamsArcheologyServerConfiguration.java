package net.jaams.jaamsarcheology.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class JaamsArcheologyServerConfiguration {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Boolean> EXPLOSIVEVIGOR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> STRANGEVIGOR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> PIGMANVIGOR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SKELETALVIGOR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> ROTTENVIGOR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> TRAITORVIGOR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> DEFLECT;
	public static final ForgeConfigSpec.ConfigValue<Boolean> DROPASITEM;
	public static final ForgeConfigSpec.ConfigValue<Boolean> AMMONITEDROPS;
	public static final ForgeConfigSpec.ConfigValue<Double> PRIMITIVESPEARDESPAWN;
	public static final ForgeConfigSpec.ConfigValue<Double> SPEARFRAGMENTDESPAWN;
	public static final ForgeConfigSpec.ConfigValue<Boolean> THROWAMMONITE;
	public static final ForgeConfigSpec.ConfigValue<Boolean> THROWPRIMITIVESPEAR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> THROWSPEARFRAGMENT;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SWORDOFUNDYING;
	public static final ForgeConfigSpec.ConfigValue<Boolean> IDOLS;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SWORDOFUNDYINGBLOCK;
	public static final ForgeConfigSpec.ConfigValue<Boolean> BROKENSWORDBLOCK;
	static {
		BUILDER.push("Effects Handler");
		EXPLOSIVEVIGOR = BUILDER.define("Explosive Vigor", true);
		STRANGEVIGOR = BUILDER.define("Strange Vigor", true);
		PIGMANVIGOR = BUILDER.define("Pigman's Vigor", true);
		SKELETALVIGOR = BUILDER.define("Skeletal Vigor", true);
		ROTTENVIGOR = BUILDER.define("Rotten Vigor", true);
		TRAITORVIGOR = BUILDER.define("Traitor's Vigor", true);
		BUILDER.pop();
		BUILDER.push("Weapon Mechanics ");
		DEFLECT = BUILDER.define("Deflect", true);
		BUILDER.pop();
		BUILDER.push("Projectile Behavior");
		DROPASITEM = BUILDER.define("Projectile Drop As Item", true);
		AMMONITEDROPS = BUILDER.define("Ammonites Drops Items", true);
		BUILDER.pop();
		BUILDER.push("Projectile Despawn Time");
		PRIMITIVESPEARDESPAWN = BUILDER.define("Primitive Spear", (double) 1200);
		SPEARFRAGMENTDESPAWN = BUILDER.define("Spear Fragment ", (double) 800);
		BUILDER.pop();
		BUILDER.push("Ranged Items Behavior");
		THROWAMMONITE = BUILDER.define("Throwable Ammonites", true);
		THROWPRIMITIVESPEAR = BUILDER.define("Throwable Primitive Spear", true);
		THROWSPEARFRAGMENT = BUILDER.define("Throwable  Spear Fragment", true);
		BUILDER.pop();
		BUILDER.push("Right Click Abilities");
		SWORDOFUNDYING = BUILDER.define("Sword of Undying", true);
		IDOLS = BUILDER.define("Idols", true);
		BUILDER.pop();
		BUILDER.push("Can Block Attacks");
		SWORDOFUNDYINGBLOCK = BUILDER.define("Sword Of Undying", true);
		BROKENSWORDBLOCK = BUILDER.define("Broken Sword Fragment", true);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}
