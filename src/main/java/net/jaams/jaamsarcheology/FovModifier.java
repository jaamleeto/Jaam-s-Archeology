
package net.jaams.jaamsarcheology;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import net.jaams.jaamsarcheology.item.SpearFragmentItem;
import net.jaams.jaamsarcheology.item.PrimitiveSpearItem;
import net.jaams.jaamsarcheology.item.MagnifyingGlassItem;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyClientConfiguration;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FovModifier {
	private static final TagKey<Item> AMMONITES = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("jaams_archeology:ammonites"));

	public FovModifier() {
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		new FovModifier();
	}

	@Mod.EventBusSubscriber
	private static class ForgeBusEvents {
		@SubscribeEvent
		public static void serverLoad(ServerStartingEvent event) {
		}

		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent
		public static void onFOVModifier(ComputeFovModifierEvent event) {
			Player player = event.getPlayer();
			if (player != null && player.isUsingItem()) {
				if (JaamsArcheologyClientConfiguration.FOVMODIFIER.get() == false) {
					event.setCanceled(true);
				}
				if (JaamsArcheologyClientConfiguration.FOVMODIFIER.get() == true) {
					ItemStack useStack = player.getUseItem();
					Item useItem = useStack.getItem();
					if (JaamsArcheologyClientConfiguration.MAGNIFYINGGLASSFOV.get() == true) {
						if (useItem instanceof MagnifyingGlassItem) {
							float f1 = player.getTicksUsingItem() / 10.0f;
							f1 = f1 > 1.0F ? 1.0F : (float) Math.pow(f1, 1.6D);
							event.setNewFovModifier(event.getFovModifier() * (1.0F - f1 * 0.5F));
						}
						if (JaamsArcheologyClientConfiguration.PRIMITIVESPEARFOV.get() == true) {
							if (useItem instanceof PrimitiveSpearItem) {
								float f1 = player.getTicksUsingItem() / 20.0f;
								f1 = f1 > 1.0F ? 1.0F : (float) Math.pow(f1, 1.6D);
								event.setNewFovModifier(event.getFovModifier() * (1.0F - f1 * 0.2F));
							}
						}
						if (JaamsArcheologyClientConfiguration.SPEARFRAGMENTFOV.get() == true) {
							if (useItem instanceof SpearFragmentItem) {
								float f1 = player.getTicksUsingItem() / 20.0f;
								f1 = f1 > 1.0F ? 1.0F : (float) Math.pow(f1, 1.6D);
								event.setNewFovModifier(event.getFovModifier() * (1.0F - f1 * 0.2F));
							}
						}
						if (JaamsArcheologyClientConfiguration.AMMONITEFOV.get() == true) {
							if (useStack.is(AMMONITES)) {
								float f1 = player.getTicksUsingItem() / 20.0f;
								f1 = f1 > 1.0F ? 1.0F : (float) Math.pow(f1, 1.6D);
								event.setNewFovModifier(event.getFovModifier() * (1.0F - f1 * 0.2F));
							}
						}
					}
				}
			}
		}
	}
}
