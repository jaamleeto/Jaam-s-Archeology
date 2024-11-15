
package net.jaams.jaamsarcheology.handler;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Minecraft;

import net.jaams.jaamsarcheology.network.JaamsArcheologyModVariables;
import net.jaams.jaamsarcheology.item.SpearFragmentItem;
import net.jaams.jaamsarcheology.item.PrimitiveSpearItem;
import net.jaams.jaamsarcheology.item.MagnifyingGlassItem;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyClientConfiguration;

@Mod.EventBusSubscriber
public class CameraEventsHandler {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void CameraShake(ViewportEvent.ComputeCameraAngles event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null) {
			if (JaamsArcheologyClientConfiguration.SHAKE.get()) {
				JaamsArcheologyModVariables.PlayerVariables playerVariables = player.getCapability(JaamsArcheologyModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new JaamsArcheologyModVariables.PlayerVariables());
				if (playerVariables.Shake) {
					double shakeIntensity = 3.5;
					double shakeUpwardMovement = 1.0;
					double randomShake = (Math.random() - 0.5) * shakeIntensity;
					double randomUpward = Math.random() * shakeUpwardMovement;
					event.setPitch((float) (event.getPitch() + randomShake));
					event.setRoll((float) (event.getRoll() + randomShake));
					event.setYaw((float) (event.getYaw() + randomShake));
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onFOVModifier(ComputeFovModifierEvent event) {
		Player player = event.getPlayer();
		if (player != null && player.isUsingItem()) {
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
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onHandRender(RenderHandEvent event) {
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
			ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
			ItemStack stack = event.getItemStack();
			if (player.isUsingItem() && (player.getUsedItemHand() == InteractionHand.MAIN_HAND || player.getUsedItemHand() == InteractionHand.OFF_HAND) && player.getUseItem().getItem() instanceof BowItem) {
				event.setCanceled(false);
			}
		}
		if (player.isUsingItem() && player.getUsedItemHand() == InteractionHand.MAIN_HAND && player.getUseItem().getItem() instanceof BowItem) {
			if (event.getHand() == InteractionHand.OFF_HAND) {
				event.setCanceled(true);
			}
		} else if (player.isUsingItem() && player.getUsedItemHand() == InteractionHand.OFF_HAND && player.getUseItem().getItem() instanceof BowItem) {
			if (event.getHand() == InteractionHand.MAIN_HAND) {
				event.setCanceled(true);
			}
		}
		if (player.isUsingItem() && player.getUsedItemHand() == InteractionHand.MAIN_HAND && player.getUseItem().getItem() instanceof MagnifyingGlassItem) {
			if (event.getHand() == InteractionHand.OFF_HAND || event.getHand() == InteractionHand.MAIN_HAND) {
				event.setCanceled(true);
			}
		} else if (player.isUsingItem() && player.getUsedItemHand() == InteractionHand.OFF_HAND && player.getUseItem().getItem() instanceof MagnifyingGlassItem) {
			if (event.getHand() == InteractionHand.MAIN_HAND || event.getHand() == InteractionHand.OFF_HAND) {
				event.setCanceled(true);
			}
		}
	}
}
