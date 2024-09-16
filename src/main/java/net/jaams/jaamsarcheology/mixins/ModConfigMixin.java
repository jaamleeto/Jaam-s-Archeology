package net.jaams.jaamsarcheology.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.config.ModConfig;

import net.minecraft.world.level.pathfinder.Path;

@Mixin(ModConfig.class)
public class ModConfigMixin {
	@Inject(method = "getFullPath", at = @At("HEAD"), cancellable = true)
	private void modifyServerConfigPath(CallbackInfoReturnable<Path> cir) {
		ModConfig config = (ModConfig) (Object) this;
		// Verifica si la configuración es de tu mod y de tipo SERVER
		if (config.getModId().equals("jaams_archeology") && config.getType() == ModConfig.Type.SERVER) {
			// Cambia la ruta a la carpeta config global
			Path customPath = FMLPaths.CONFIGDIR.get().resolve("jaams_archeology/jaams_archeology_server.toml");
			cir.setReturnValue(customPath); // Devuelve el nuevo path
		}
	}
}
