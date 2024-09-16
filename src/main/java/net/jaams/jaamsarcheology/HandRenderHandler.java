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
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.Minecraft;

import net.jaams.jaamsarcheology.item.MagnifyingGlassItem;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class HandRenderHandler {
	public HandRenderHandler() {
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		new HandRenderHandler();
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
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void onHandRender(RenderHandEvent event) {
			Player player = Minecraft.getInstance().player;
			if (player != null) {
				ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
				ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
				ItemStack stack = event.getItemStack();
				if (player.isUsingItem() && (player.getUsedItemHand() == InteractionHand.MAIN_HAND || player.getUsedItemHand() == InteractionHand.OFF_HAND) && player.getUseItem().getItem() instanceof BowItem) {
					event.setCanceled(false);
				}
			}
			if (player.isUsingItem() && player.getUsedItemHand() == InteractionHand.MAIN_HAND && player.getUseItem().getItem() instanceof BowItem) {
				// If main hand holds a crossbow item, hide the offhand
				if (event.getHand() == InteractionHand.OFF_HAND) {
					event.setCanceled(true);
				}
			} else if (player.isUsingItem() && player.getUsedItemHand() == InteractionHand.OFF_HAND && player.getUseItem().getItem() instanceof BowItem) {
				// If offhand holds a crossbow item, hide the main hand
				if (event.getHand() == InteractionHand.MAIN_HAND) {
					event.setCanceled(true);
				}
			}
			if (player.isUsingItem() && player.getUsedItemHand() == InteractionHand.MAIN_HAND && player.getUseItem().getItem() instanceof MagnifyingGlassItem) {
				// If main hand holds a crossbow item, hide the offhand
				if (event.getHand() == InteractionHand.OFF_HAND || event.getHand() == InteractionHand.MAIN_HAND) {
					event.setCanceled(true);
				}
			} else if (player.isUsingItem() && player.getUsedItemHand() == InteractionHand.OFF_HAND && player.getUseItem().getItem() instanceof MagnifyingGlassItem) {
				// If offhand holds a crossbow item, hide the main hand
				if (event.getHand() == InteractionHand.MAIN_HAND || event.getHand() == InteractionHand.OFF_HAND) {
					event.setCanceled(true);
				}
			}
		}
	}
}
