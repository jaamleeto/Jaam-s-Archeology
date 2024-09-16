
package net.jaams.jaamsarcheology;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;

import java.util.Map;
import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageRenderer {
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
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			return;
		}
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
			double x = entity.getX() + info.randomOffsetX; // Añadir desplazamiento aleatorio en X
			double y = entity.getY() + entity.getBbHeight() + 0.7 + info.randomOffsetY; // Añadir desplazamiento aleatorio en Y
			double z = entity.getZ() + info.randomOffsetZ; // Añadir desplazamiento aleatorio en Z
			// Animación de profundidad (el texto se acerca al jugador)
			double progress = 1.0 - ((double) (info.expireTime - currentTime) / (DISPLAY_TIME * 1000)); // Progreso del tiempo
			double depthOffset = progress * 0.5; // El texto se acerca hacia el jugador con el tiempo
			x -= depthOffset * (cameraPos.x - x);
			y -= depthOffset * (cameraPos.y - y);
			z -= depthOffset * (cameraPos.z - z);
			float alpha = getAlpha(info, currentTime);
			int color = getDamageColor(info);
			float scale = getScale(info, progress); // Escalar dinámicamente
			renderDamageText(event.getPoseStack(), x, y, z, String.format("%.1f", info.damage), color, alpha, scale);
			return false;
		});
	}

	private static float getAlpha(DamageInfo info, long currentTime) {
		long fadeStart = info.expireTime - (long) (FADE_DURATION * 1000);
		if (currentTime > fadeStart) {
			return Math.max(0, (info.expireTime - currentTime) / (FADE_DURATION * 1000f));
		}
		return 1.0f;
	}

	private static float getScale(DamageInfo info, double progress) {
		// Escalar desde un tamaño pequeño al normal durante el tiempo de vida del texto
		float baseScale = info.isFinalHit ? 2.0f : 1.2f;
		float dynamicScale = 0.5f + (float) progress * baseScale; // Empieza pequeño y crece
		return dynamicScale;
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

	@OnlyIn(Dist.CLIENT)
	private static void renderDamageText(PoseStack poseStack, double x, double y, double z, String text, int color, float alpha, float scale) {
		Minecraft mc = Minecraft.getInstance();
		Camera camera = mc.gameRenderer.getMainCamera();
		double distance = camera.getPosition().distanceTo(new Vec3(x, y, z));
		if (distance > 64) {
			return;
		}
		poseStack.pushPose();
		poseStack.translate(x - camera.getPosition().x, y - camera.getPosition().y, z - camera.getPosition().z);
		poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
		float textScale = -0.08F * scale;
		poseStack.scale(textScale, textScale, textScale);
		Font font = mc.font;
		int alphaChannel = (int) (alpha * 255) << 24;
		color = color & 0x00FFFFFF | alphaChannel;
		font.drawInBatch(text, -font.width(text) / 2.0F, 0, color, false, poseStack.last().pose(), mc.renderBuffers().bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
		poseStack.popPose();
	}
}
