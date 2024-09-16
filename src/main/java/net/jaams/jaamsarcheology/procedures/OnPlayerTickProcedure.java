package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.Entity;

import net.jaams.jaamsarcheology.network.JaamsArcheologyModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class OnPlayerTickProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity.isSprinting()) {
			{
				boolean _setval = true;
				entity.getCapability(JaamsArcheologyModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.RunHit = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
		if (!entity.isSprinting()) {
			{
				boolean _setval = false;
				entity.getCapability(JaamsArcheologyModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.RunHit = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
	}
}
