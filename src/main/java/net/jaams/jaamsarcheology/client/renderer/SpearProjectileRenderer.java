package net.jaams.jaamsarcheology.client.renderer;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.Minecraft;

import net.jaams.jaamsarcheology.entity.SpearProjectileEntity;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyClientConfiguration;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

@OnlyIn(Dist.CLIENT)
public class SpearProjectileRenderer extends EntityRenderer<SpearProjectileEntity> {
	public SpearProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(SpearProjectileEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		matrixStack.pushPose();
		final float SCALE_FACTOR = JaamsArcheologyClientConfiguration.PRIMITIVESPEARSIZE.get().floatValue();
		matrixStack.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
		matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 180.0F));
		matrixStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) - 90));
		matrixStack.translate(0.0F, -1.195F, 0.0F);
		ItemRenderer render = Minecraft.getInstance().getItemRenderer();
		render.renderStatic(entity.weaponItem, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer, entity.level(), entity.getId());
		matrixStack.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(SpearProjectileEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

	@Override
	public boolean shouldRender(SpearProjectileEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
		return super.shouldRender(pLivingEntity, pCamera, pCamX, pCamY, pCamZ);
	}
}
