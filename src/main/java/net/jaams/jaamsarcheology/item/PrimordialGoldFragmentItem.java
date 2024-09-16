
package net.jaams.jaamsarcheology.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class PrimordialGoldFragmentItem extends Item {
	public PrimordialGoldFragmentItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}
}
