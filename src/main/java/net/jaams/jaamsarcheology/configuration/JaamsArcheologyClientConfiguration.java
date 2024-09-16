package net.jaams.jaamsarcheology.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class JaamsArcheologyClientConfiguration {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Double> PRIMITIVESPEARSIZE;
	public static final ForgeConfigSpec.ConfigValue<Double> AMMONITESIZE;
	public static final ForgeConfigSpec.ConfigValue<Double> SPEARFRAGMENTSIZE;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SHAKE;
	public static final ForgeConfigSpec.ConfigValue<Boolean> FOVMODIFIER;
	public static final ForgeConfigSpec.ConfigValue<Boolean> MAGNIFYINGGLASSFOV;
	public static final ForgeConfigSpec.ConfigValue<Boolean> PRIMITIVESPEARFOV;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SPEARFRAGMENTFOV;
	public static final ForgeConfigSpec.ConfigValue<Boolean> AMMONITEFOV;
	static {
		BUILDER.push("Projectiles Size");
		PRIMITIVESPEARSIZE = BUILDER.define("Primitive Spear ", (double) 1);
		AMMONITESIZE = BUILDER.define("Ammonite", (double) 1.5);
		SPEARFRAGMENTSIZE = BUILDER.define("Spear Fragment ", (double) 1.5);
		BUILDER.pop();
		BUILDER.push("Camera Effects");
		SHAKE = BUILDER.define("Shake", true);
		FOVMODIFIER = BUILDER.define("Fov Modifier", true);
		BUILDER.pop();
		BUILDER.push("FOV Modifier Handler");
		MAGNIFYINGGLASSFOV = BUILDER.define("Magnifying Glass", true);
		PRIMITIVESPEARFOV = BUILDER.define("Primitive Spear", true);
		SPEARFRAGMENTFOV = BUILDER.define("Spear Fragment", true);
		AMMONITEFOV = BUILDER.define("Ammonites", true);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}
