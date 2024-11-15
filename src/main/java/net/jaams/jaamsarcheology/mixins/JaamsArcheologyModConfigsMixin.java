package net.jaams.jaamsarcheology.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModConfigs;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyCommonConfiguration;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyClientConfiguration;

@Mixin(JaamsArcheologyModConfigs.class)
public abstract class JaamsArcheologyModConfigsMixin {
	@Inject(method = "register", at = @At("HEAD"), cancellable = true, remap = false)
	private static void injectRegister(FMLConstructModEvent event, CallbackInfo ci) {
		event.enqueueWork(() -> {
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, JaamsArcheologyClientConfiguration.SPEC, "jaams/jaams_archeology_client.toml");
			ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, JaamsArcheologyCommonConfiguration.SPEC, "jaams/jaams_archeology_common.toml");
		});
		ci.cancel();
	}
}
