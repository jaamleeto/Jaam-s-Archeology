
package net.jaams.jaamsarcheology.util;

import net.minecraft.world.item.ItemStack;

public class WeightedItem {
	private final ItemStack item;
	private final float weight;

	public WeightedItem(ItemStack item, float weight) {
		this.item = item;
		this.weight = weight;
	}

	public ItemStack getItem() {
		return item;
	}

	public float getWeight() {
		return weight;
	}
}
