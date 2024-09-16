package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;

import javax.annotation.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class TooltipsProcedure {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		execute(event, event.getItemStack(), event.getToolTip());
	}

	public static void execute(ItemStack itemstack, List<Component> tooltip) {
		execute(null, itemstack, tooltip);
	}

	private static void execute(@Nullable Event event, ItemStack itemstack, List<Component> tooltip) {
		if (tooltip == null)
			return;
		if (itemstack.getItem() == JaamsArcheologyModItems.ARCHEO_PICKAXE.get()) {
			tooltip.add(1, Component.literal("\u00A76Twohanded"));
			if (Screen.hasShiftDown()) {
				tooltip.add(2, Component.literal((Component.translatable("translation.key.pico").getString())));
			}
			if (Screen.hasShiftDown()) {
				tooltip.add(3, Component.literal("\u00A76Reach II"));
			} else {
				tooltip.add(2, Component.literal("\u00A76Reach II"));
			}
			if (Screen.hasShiftDown()) {
				tooltip.add(4, Component.literal((Component.translatable("translation.key.pico").getString())));
			}
			if (Screen.hasShiftDown()) {
				tooltip.add(5, Component.literal("\u00A76Lethality"));
			} else {
				tooltip.add(3, Component.literal("\u00A76Lethality"));
			}
			if (Screen.hasShiftDown()) {
				tooltip.add(6, Component.literal((Component.translatable("translation.key.pico").getString())));
			}
			if (Screen.hasShiftDown()) {
				tooltip.add(7, Component.literal("\u00A76Heavy"));
			} else {
				tooltip.add(4, Component.literal("\u00A76Heavy"));
			}
			if (Screen.hasShiftDown()) {
				tooltip.add(8, Component.literal((Component.translatable("translation.key.pico").getString())));
			}
			if (!Screen.hasShiftDown()) {
				tooltip.add(Component.literal(""));
			}
			if (!Screen.hasShiftDown()) {
				tooltip.add(Component.literal((Component.translatable("translation.key.details").getString())));
			}
		}
	}
}
