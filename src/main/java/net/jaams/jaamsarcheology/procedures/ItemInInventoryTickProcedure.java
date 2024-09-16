package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.nbt.CompoundTag;

import net.jaams.jaamsarcheology.item.MagnifyingGlassItem;

public class ItemInInventoryTickProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		Player player = event.player;
		for (InteractionHand hand : InteractionHand.values()) {
			ItemStack itemstack = player.getItemInHand(hand);
			if (itemstack.getItem() instanceof MagnifyingGlassItem) {
				ItemInInventoryTickProcedure.execute(itemstack, player);
			}
		}
	}

	public static void execute(ItemStack itemstack, Player player) {
		if (player == null) {
			return;
		}
		CompoundTag tag = itemstack.getOrCreateTag();
		boolean isUsing = player.isUsingItem() && player.getUseItem() == itemstack;
		if (isUsing) {
			tag.putInt("CustomModelData", 1);
		} else {
			tag.putInt("CustomModelData", 0);
		}
	}
}
