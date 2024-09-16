
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.jaams.jaamsarcheology.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.jaams.jaamsarcheology.client.model.Modelstake_model;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class JaamsArcheologyModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelstake_model.LAYER_LOCATION, Modelstake_model::createBodyLayer);
	}
}
