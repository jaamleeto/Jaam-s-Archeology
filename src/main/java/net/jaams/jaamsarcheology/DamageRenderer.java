
package net.jaams.jaamsarcheology;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageRenderer {
	/*
		private static class DamageInfo {
			float damage;
			long expireTime;
			boolean isFinalHit;
			DamageSource damageSource;
			double randomOffsetX;
			double randomOffsetY;
			double randomOffsetZ;
			DamageInfo(float damage, long expireTime, DamageSource damageSource, boolean isFinalHit) {
				this.damage = damage;
				this.expireTime = expireTime;
				this.damageSource = damageSource;
				this.isFinalHit = isFinalHit;
				// Generar un desplazamiento aleatorio para que no aparezca siempre en el mismo lugar
				this.randomOffsetX = (Math.random() - 0.5) * 0.8; // Desplazamiento en X (-0.25 a 0.25)
				this.randomOffsetY = (Math.random() - 0.5) * 0.5; // Desplazamiento en Y (-0.15 a 0.15)
				this.randomOffsetZ = (Math.random() - 0.5) * 0.8; // Desplazamiento en Z (-0.25 a 0.25)
			}
		}
		private static final Map<Entity, DamageInfo> damageMap = new HashMap<>();
		private static final float DISPLAY_TIME = 1.5f;
		private static final float FADE_DURATION = 0.3f;
		@SubscribeEvent
		public static void onEntityHurt(LivingHurtEvent event) {
			Entity entity = event.getEntity();
			float damage = event.getAmount();
			boolean isFinalHit = entity instanceof LivingEntity && (((LivingEntity) entity).getHealth() - damage <= 0);
			damageMap.put(entity, new DamageInfo(damage, System.currentTimeMillis() + (long) (DISPLAY_TIME * 1000), event.getSource(), isFinalHit));
		}
		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent
		public static void onRenderLevelStage(RenderLevelStageEvent event) {
			if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) { // Cambiado a AFTER_PARTICLES
				long currentTime = System.currentTimeMillis();
				Minecraft mc = Minecraft.getInstance();
				Camera camera = mc.gameRenderer.getMainCamera();
				Vec3 cameraPos = camera.getPosition();
				damageMap.entrySet().removeIf(entry -> {
					Entity entity = entry.getKey();
					DamageInfo info = entry.getValue();
					if (currentTime > info.expireTime) {
						return true;
					}
					double x = entity.getX() + info.randomOffsetX;
					double y = entity.getY() + entity.getBbHeight() + 0.7 + info.randomOffsetY;
					double z = entity.getZ() + info.randomOffsetZ;
					double progress = 1.0 - ((double) (info.expireTime - currentTime) / (DISPLAY_TIME * 1000));
					double depthOffset = progress * 0.5;
					x -= depthOffset * (cameraPos.x - x);
					y -= depthOffset * (cameraPos.y - y);
					z -= depthOffset * (cameraPos.z - z);
					float alpha = getAlpha(info, currentTime);
					int color = getDamageColor(info);
					float scale = getScale(info, progress);
					renderDamageText(event.getPoseStack(), x, y, z, String.format("%.1f", info.damage), color, alpha, scale, progress);
					return false;
				});
			}
		}
		private static float getAlpha(DamageInfo info, long currentTime) {
			long fadeStart = info.expireTime - (long) (FADE_DURATION * 1000);
			if (currentTime > fadeStart) {
				return Math.max(0, (info.expireTime - currentTime) / (FADE_DURATION * 1000f));
			}
			return 1.0f;
		}
		private static float getScale(DamageInfo info, double progress) {
			// Mantener una escala más constante
			float baseScale = info.isFinalHit ? 1.5f : 1.0f; // Menor escala base
			float dynamicScale = 0.8f + (float) progress * 0.4f; // El crecimiento es más limitado
			return baseScale * dynamicScale; // Multiplicamos por el escalado dinámico limitado
		}
		private static int getDamageColor(DamageInfo info) {
			DamageSource damageSource = info.damageSource;
			if (damageSource.is(DamageTypes.ON_FIRE) || damageSource.is(DamageTypes.IN_FIRE)) {
				return 0xFFA500;
			} else if (damageSource.is(DamageTypes.FALL)) {
				return 0xFFFFFF;
			} else if (info.isFinalHit) {
				return 0xFF0000;
			} else {
				return 0xFFFFFF;
			}
		}
		private static void renderDamageText(PoseStack poseStack, double x, double y, double z, String text, int color, float alpha, float scale, double progress) {
			Minecraft mc = Minecraft.getInstance();
			Camera camera = mc.gameRenderer.getMainCamera();
			Vec3 cameraPos = camera.getPosition();
			double distance = cameraPos.distanceTo(new Vec3(x, y, z));
			if (distance > 64) {
				return;
			}
			// Usar una función cuadrática para aumentar el acercamiento inicial y final
			double enhancedProgress = Math.pow(progress - 0.5, 2) * 4; // Escalar el progreso
			// Ajustar el texto para que se acerque más rápido al inicio y final
			double offsetX = (cameraPos.x - x) * enhancedProgress * 0.75; // Factor de acercamiento aumentado
			double offsetY = (cameraPos.y - y) * enhancedProgress * 0.75;
			double offsetZ = (cameraPos.z - z) * enhancedProgress * 0.75;
			poseStack.pushPose();
			poseStack.translate(x - cameraPos.x + offsetX, y - cameraPos.y + offsetY, z - cameraPos.z + offsetZ);
			poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
			float textScale = -0.08F * scale;
			poseStack.scale(textScale, textScale, textScale);
			Font font = mc.font;
			RenderSystem.depthMask(false);
			RenderSystem.disableDepthTest();
			int alphaChannel = (int) (alpha * 255) << 24;
			color = color & 0x00FFFFFF | alphaChannel;
			font.drawInBatch(text, -font.width(text) / 2.0F, 0, color, false, poseStack.last().pose(), mc.renderBuffers().bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
			RenderSystem.enableDepthTest();
			RenderSystem.depthMask(true);
			poseStack.popPose();
		}
		*/
}
