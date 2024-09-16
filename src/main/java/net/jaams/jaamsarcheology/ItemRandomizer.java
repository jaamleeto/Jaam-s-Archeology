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
