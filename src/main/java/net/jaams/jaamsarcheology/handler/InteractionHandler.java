
package net.jaams.jaamsarcheology.handler;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;

import net.jaams.jaamsarcheology.item.SpearFragmentItem;
import net.jaams.jaamsarcheology.item.PrimitiveSpearItem;
import net.jaams.jaamsarcheology.item.BrokenSwordFragmentItem;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyCommonConfiguration;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InteractionHandler {
	@SubscribeEvent
	public static void onInteractionHandlerItemUse(PlayerInteractEvent.RightClickItem event) {
		Player player = event.getEntity();
		ItemStack itemStack = event.getItemStack();
		Level level = player.level();
		// SpearFragmentItem logic
		if (itemStack.getItem() instanceof SpearFragmentItem) {
			if (!JaamsArcheologyCommonConfiguration.THROWSPEARFRAGMENT.get()) {
				event.setCanceled(true);
			}
		}
		// PrimitiveSpearItem logic
		if (itemStack.getItem() instanceof PrimitiveSpearItem) {
			if (!JaamsArcheologyCommonConfiguration.THROWPRIMITIVESPEAR.get()) {
				event.setCanceled(true);
			}
		}
		// BrokenSwordFragmentItem logic
		if (itemStack.getItem() instanceof BrokenSwordFragmentItem) {
			if (!JaamsArcheologyCommonConfiguration.BROKENSWORDBLOCK.get()) {
				event.setCanceled(true);
				if (level.isClientSide()) {
				}
			}
		}
	}
}
