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

import net.jaams.jaamsarcheology.entity.AmmoniteProjectileEntity;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyClientConfiguration;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

@OnlyIn(Dist.CLIENT)
public class AmmoniteProjectileRenderer extends EntityRenderer<AmmoniteProjectileEntity> {
	public AmmoniteProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(AmmoniteProjectileEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		matrixStack.pushPose();
		final float SCALE_FACTOR = JaamsArcheologyClientConfiguration.AMMONITESIZE.get().floatValue();
		matrixStack.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
		matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 0.0F));
		if (!entity.hasImpacted()) {
			float spinRotation = (float) entity.getSpinTicks() * 30.0F; 
			matrixStack.mulPose(Axis.XP.rotationDegrees(spinRotation));
		}
		if (entity.hasImpacted()) {
			matrixStack.mulPose(Axis.ZP.rotationDegrees(entity.getXRot() + 0));
		}
		matrixStack.translate(0.0F, -0.175F, 0.0F);
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		itemRenderer.renderStatic(entity.ammoniteItem, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer, entity.level(), entity.getId());
		matrixStack.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(AmmoniteProjectileEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

	@Override
	public boolean shouldRender(AmmoniteProjectileEntity entity, Frustum frustum, double camX, double camY, double camZ) {
		return super.shouldRender(entity, frustum, camX, camY, camZ);
	}
}
