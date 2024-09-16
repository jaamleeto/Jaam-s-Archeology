/**
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.jaams.jaamsarcheology as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.jaams.jaamsarcheology;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Minecraft;

import net.jaams.jaamsarcheology.network.JaamsArcheologyModVariables;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyClientConfiguration;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CameraShake {
	public CameraShake() {
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		new CameraShake();
	}

	@Mod.EventBusSubscriber
	private static class ForgeBusEvents {
		@SubscribeEvent
		public static void serverLoad(ServerStartingEvent event) {
		}

		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent
		public static void clientLoad(FMLClientSetupEvent event) {
		}

		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent
		public static void CameraShake(ViewportEvent.ComputeCameraAngles event) {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player != null) {
				if (JaamsArcheologyClientConfiguration.SHAKE.get()) {
					JaamsArcheologyModVariables.PlayerVariables playerVariables = player.getCapability(JaamsArcheologyModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new JaamsArcheologyModVariables.PlayerVariables());
					if (playerVariables.Shake) {
						double shakeIntensity = 3.5; // Adjust the intensity of the shake for the pistol
						double shakeUpwardMovement = 1.0; // Adjust the upward movement
						// Generate smoother random values within a range
						double randomShake = (Math.random() - 0.5) * shakeIntensity;
						double randomUpward = Math.random() * shakeUpwardMovement;
						// Apply shake to pitch, roll, and yaw
						event.setPitch((float) (event.getPitch() + randomShake));
						event.setRoll((float) (event.getRoll() + randomShake));
						event.setYaw((float) (event.getYaw() + randomShake));
					}
				}
			}
		}
	}
}
