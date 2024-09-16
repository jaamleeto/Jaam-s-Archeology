package net.jaams.jaamsarcheology.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ItemInHandRenderer;

import net.jaams.jaamsarcheology.item.SpearFragmentItem;
import net.jaams.jaamsarcheology.item.PrimitiveSpearItem;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	@Shadow
	private void renderItem(LivingEntity entity, ItemStack itemStack, ItemDisplayContext transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		// Método sombreado para la identificación correcta del método
	}

	@Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
	private void onRenderItem(LivingEntity entity, ItemStack itemStack, ItemDisplayContext transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
		if (itemStack != null && (transformType == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND)) {
			// Aplicar transformaciones personalizadas
			if (entity.isUsingItem() && itemStack.is(entity.getUseItem().getItem())) {
				Item item = itemStack.getItem();
				// Transformación para los items de lanza
				if (item instanceof PrimitiveSpearItem || item instanceof SpearFragmentItem) {
					poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
					poseStack.translate(0.0, 0.1, 0.0);
				}
			}
		}
	}
}
