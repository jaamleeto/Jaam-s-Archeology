
package net.jaams.jaamsarcheology.item;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;

public class MusicDiscNamelessItem extends RecordItem {
	public MusicDiscNamelessItem() {
		super(3, () -> ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:primitive_spear_hit")), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), 2400);
	}
}
