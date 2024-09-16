package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class OnItemStopUsingProcedure {
	@SubscribeEvent
	public static void onUseItemStop(LivingEntityUseItemEvent.Stop event) {
		if (event != null && event.getEntity() != null) {
			execute(event, event.getEntity(), event.getItem());
		}
	}

	public static void execute(Entity entity, ItemStack itemstack) {
		execute(null, entity, itemstack);
	}

	private static void execute(@Nullable Event event, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (itemstack.getItem() == JaamsArcheologyModItems.MAGNIFYING_GLASS.get()) {
			itemstack.getOrCreateTag().putDouble("CustomModelData", 0);
		}
		if (itemstack.getItem() == JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()) {
			itemstack.getOrCreateTag().putDouble("CustomModelData", 0);
			if (!ModList.get().isLoaded("epicfight")) {
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown(itemstack.getItem(), 10);
			}
		}
		if (itemstack.getItem() == JaamsArcheologyModItems.SWORD_OF_UNDYING.get()) {
			itemstack.getOrCreateTag().putDouble("CustomModelData", 0);
			if (!ModList.get().isLoaded("epicfight")) {
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown(itemstack.getItem(), 20);
			}
		}
	}
}
