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

import net.jaams.jaamsarcheology.entity.SpearFragmentProjectileEntity;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyClientConfiguration;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

@OnlyIn(Dist.CLIENT)
public class SpearFragmentProjectileRenderer extends EntityRenderer<SpearFragmentProjectileEntity> {
	public SpearFragmentProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(SpearFragmentProjectileEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		matrixStack.pushPose();
		// Scale the entity
		final float SCALE_FACTOR = JaamsArcheologyClientConfiguration.SPEARFRAGMENTSIZE.get().floatValue();
		matrixStack.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
		// Apply rotations based on the entity's orientation
		matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
		matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
		matrixStack.mulPose(Axis.ZP.rotationDegrees(-135.0F));
		matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		// Adjust translation to position the spear correctly
		matrixStack.translate(0.0F, -0.175F, 0.0F);
		// Get the item renderer instance
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		// Render the spear item with appropriate transformation and overlay settings
		itemRenderer.renderStatic(entity.weaponItem, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer, entity.level(), entity.getId());
		matrixStack.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(SpearFragmentProjectileEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

	@Override
	public boolean shouldRender(SpearFragmentProjectileEntity entity, Frustum frustum, double camX, double camY, double camZ) {
		return super.shouldRender(entity, frustum, camX, camY, camZ);
	}
}