package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

import net.minecraft.world.entity.Entity;

import net.jaams.jaamsarcheology.network.JaamsArcheologyModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class CriticalDetectProcedure {
	@SubscribeEvent
	public static void onPlayerCriticalHit(CriticalHitEvent event) {
		execute(event, event.getEntity(), event.isVanillaCritical());
	}

	public static void execute(Entity sourceentity, boolean isvanillacritical) {
		execute(null, sourceentity, isvanillacritical);
	}

	private static void execute(@Nullable Event event, Entity sourceentity, boolean isvanillacritical) {
		if (sourceentity == null)
			return;
		if (isvanillacritical == true) {
			{
				boolean _setval = true;
				sourceentity.getCapability(JaamsArcheologyModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.Critical = _setval;
					capability.syncPlayerVariables(sourceentity);
				});
			}
		}
		if (isvanillacritical == false) {
			{
				boolean _setval = false;
				sourceentity.getCapability(JaamsArcheologyModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.Critical = _setval;
					capability.syncPlayerVariables(sourceentity);
				});
			}
		}
	}
}
