
package net.jaams.jaamsarcheology.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class PrimordialGoldItem extends Item {
	public PrimordialGoldItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
	}
}
