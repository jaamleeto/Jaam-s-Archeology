package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

import net.minecraft.world.item.ItemStack;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class OnItemUsingProcedure {
	@SubscribeEvent
	public static void onUseItemStart(LivingEntityUseItemEvent.Start event) {
		if (event != null && event.getEntity() != null) {
			execute(event, event.getItem());
		}
	}

	public static void execute(ItemStack itemstack) {
		execute(null, itemstack);
	}

	private static void execute(@Nullable Event event, ItemStack itemstack) {
		if (itemstack.getItem() == JaamsArcheologyModItems.MAGNIFYING_GLASS.get()) {
			itemstack.getOrCreateTag().putDouble("CustomModelData", 1);
		}
		if (itemstack.getItem() == JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()) {
			itemstack.getOrCreateTag().putDouble("CustomModelData", 1);
		}
		if (itemstack.getItem() == JaamsArcheologyModItems.SWORD_OF_UNDYING.get()) {
			itemstack.getOrCreateTag().putDouble("CustomModelData", 1);
		}
	}
}
