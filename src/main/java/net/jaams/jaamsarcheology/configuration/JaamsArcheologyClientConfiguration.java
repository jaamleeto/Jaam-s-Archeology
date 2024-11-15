package net.jaams.jaamsarcheology.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class JaamsArcheologyClientConfiguration {
	// Builder and Spec (for client configuration)
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	// Projectile size options
	public static final ForgeConfigSpec.ConfigValue<Double> PRIMITIVESPEARSIZE;
	public static final ForgeConfigSpec.ConfigValue<Double> AMMONITESIZE;
	public static final ForgeConfigSpec.ConfigValue<Double> SPEARFRAGMENTSIZE;
	// Camera effects
	public static final ForgeConfigSpec.ConfigValue<Boolean> SHAKE;
	public static final ForgeConfigSpec.ConfigValue<Boolean> FOVMODIFIER;
	// FOV modifiers
	public static final ForgeConfigSpec.ConfigValue<Boolean> MAGNIFYINGGLASSFOV;
	public static final ForgeConfigSpec.ConfigValue<Boolean> PRIMITIVESPEARFOV;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SPEARFRAGMENTFOV;
	static {
		// Configuration for projectile sizes
		BUILDER.push("Projectiles Size");
		PRIMITIVESPEARSIZE = BUILDER.comment("Size of the primitive spear. Must be between 1 and 10.").defineInRange("Primitive Spear", 1.0, 1.0, 10.0);
		AMMONITESIZE = BUILDER.comment("Size of the ammonite. Must be between 1 and 10.").defineInRange("Ammonite", 1.5, 1.0, 10.0);
		SPEARFRAGMENTSIZE = BUILDER.comment("Size of the spear fragment. Must be between 1 and 10.").defineInRange("Spear Fragment", 1.5, 1.0, 10.0);
		BUILDER.pop();
		// Configuration for camera effects
		BUILDER.push("Camera Effects");
		SHAKE = BUILDER.comment("Enables or disables camera shake.").define("Shake", true);
		FOVMODIFIER = BUILDER.comment("Enables or disables FOV modifier.").define("Fov Modifier", true);
		BUILDER.pop();
		// Configuration for FOV modifiers
		BUILDER.push("FOV Modifier Handler");
		MAGNIFYINGGLASSFOV = BUILDER.comment("Enables or disables FOV change for the magnifying glass.").define("Magnifying Glass", true);
		PRIMITIVESPEARFOV = BUILDER.comment("Enables or disables FOV change for the primitive spear.").define("Primitive Spear", true);
		SPEARFRAGMENTFOV = BUILDER.comment("Enables or disables FOV change for the spear fragment.").define("Spear Fragment", true);
		BUILDER.pop();
		// Build the configuration
		SPEC = BUILDER.build();
	}
}
