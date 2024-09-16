
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.jaams.jaamsarcheology.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.jaams.jaamsarcheology.client.renderer.SpearProjectileRenderer;
import net.jaams.jaamsarcheology.client.renderer.SpearFragmentProjectileRenderer;
import net.jaams.jaamsarcheology.client.renderer.AmmoniteProjectileRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class JaamsArcheologyModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(JaamsArcheologyModEntities.SPEAR_PROJECTILE.get(), SpearProjectileRenderer::new);
		event.registerEntityRenderer(JaamsArcheologyModEntities.SPEAR_FRAGMENT_PROJECTILE.get(), SpearFragmentProjectileRenderer::new);
		event.registerEntityRenderer(JaamsArcheologyModEntities.AMMONITE_PROJECTILE.get(), AmmoniteProjectileRenderer::new);
	}
}
