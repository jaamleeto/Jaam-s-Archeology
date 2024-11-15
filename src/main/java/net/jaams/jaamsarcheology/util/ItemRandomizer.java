
package net.jaams.jaamsarcheology.util;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.util.RandomSource;

import java.util.List;

public class ItemRandomizer {
	public static ItemStack generate(Level level, List<Item> items) {
		if (items.isEmpty()) {
			return ItemStack.EMPTY;
		}
		RandomSource random = level.random; // Use RandomSource from the level
		Item randomItem = items.get(random.nextInt(items.size()));
		return new ItemStack(randomItem);
	}
}
