package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

import net.jaams.jaamsarcheology.network.JaamsArcheologyModVariables;
import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;
import net.jaams.jaamsarcheology.JaamsArcheologyMod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TotemAnimHandlerProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level(), event.player);
		}
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((entity.getCapability(JaamsArcheologyModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new JaamsArcheologyModVariables.PlayerVariables())).SOUTotem == true) {
			if (world.isClientSide())
				Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(JaamsArcheologyModItems.SWORD_OF_UNDYING.get()));
			JaamsArcheologyMod.queueServerWork(5, () -> {
				{
					boolean _setval = false;
					entity.getCapability(JaamsArcheologyModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.SOUTotem = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
			});
		}
	}
}
