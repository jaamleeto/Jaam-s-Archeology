package net.jaams.jaamsarcheology.init;

import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.jaams.jaamsarcheology.configuration.JaamsArcheologyServerConfiguration;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyClientConfiguration;
import net.jaams.jaamsarcheology.JaamsArcheologyMod;

@Mod.EventBusSubscriber(modid = JaamsArcheologyMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JaamsArcheologyModConfigs {
	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> {
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, JaamsArcheologyClientConfiguration.SPEC, "jaams_archeology_client.toml");
			ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, JaamsArcheologyServerConfiguration.SPEC, "jaams_archeology_server.toml");
		});
	}
}
