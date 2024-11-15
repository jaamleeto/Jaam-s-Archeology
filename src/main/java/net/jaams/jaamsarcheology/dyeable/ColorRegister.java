
package net.jaams.jaamsarcheology.dyeable;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ColorRegister {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onItemColorRegister(RegisterColorHandlersEvent.Item event) {
		event.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((IDyeableItem) stack.getItem()).getColor(stack), JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get());
	}
}
