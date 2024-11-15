package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;

@Mod.EventBusSubscriber
public class BrokenSwordFragmentDeflectProcedure {
	private static final double MAX_DEFLECT_AMOUNT = 70.0;

	private static boolean isEpicFightLoaded() {
		return ModList.get().isLoaded("epicfight");
	}

	@SubscribeEvent
	public static void onBrokenSwordFragmentEntityAttacked(LivingAttackEvent event) {
		if (event != null && event.getEntity() instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) event.getEntity();
			ItemStack blockingItem = ItemStack.EMPTY;
			boolean isBlocking = livingEntity.isBlocking();
			ItemStack mainHandItem = livingEntity.getMainHandItem();
			ItemStack offHandItem = livingEntity.getOffhandItem();
			if (mainHandItem.getItem() == JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()) {
				blockingItem = mainHandItem;
			} else if (offHandItem.getItem() == JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()) {
				blockingItem = offHandItem;
			}
			if (!blockingItem.isEmpty()) {
				double deflectAmount;
				if (isBlocking) {
					deflectAmount = Math.min(event.getAmount(), MAX_DEFLECT_AMOUNT);
				} else {
					deflectAmount = Math.min(event.getAmount(), MAX_DEFLECT_AMOUNT);
				}
				blockingItem.getOrCreateTag().putDouble("DeflectAmount", deflectAmount);
				DamageSource damageSource = event.getSource();
				Entity directEntity = damageSource.getDirectEntity();
				if (directEntity instanceof LivingEntity) {
					LivingEntity attacker = (LivingEntity) directEntity;
					blockingItem.getOrCreateTag().putString("AttackerName", attacker.getName().getString());
				} else if (directEntity instanceof Projectile) {
					Projectile projectile = (Projectile) directEntity;
					if (projectile.getOwner() instanceof LivingEntity) {
						LivingEntity projectileOwner = (LivingEntity) projectile.getOwner();
						blockingItem.getOrCreateTag().putString("AttackerName", projectileOwner.getName().getString());
					} else {
						blockingItem.getOrCreateTag().putString("AttackerName", "Projectile");
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBrokenSwordFragmentEntityAttack(LivingHurtEvent event) {
		LivingEntity entity = event.getEntity();
		DamageSource source = event.getSource();
		Entity attacker = source.getEntity();
		if (attacker instanceof Player player) {
			ItemStack weapon = player.getMainHandItem();
			if (weapon.getItem() == JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()) {
				double deflectAmount = weapon.getOrCreateTag().getDouble("DeflectAmount");
				if (deflectAmount > 0) {
					event.setAmount((float) (event.getAmount() + deflectAmount));
					if (entity.isBlocking()) {
						ItemStack blockingItem = entity.getUseItem();
						if (blockingItem.getItem() == JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()) {
							blockingItem.getOrCreateTag().putDouble("DeflectAmount", deflectAmount);
							int durabilityDamage = (int) (deflectAmount * 0.5);
							handleItemDurability(blockingItem, (Player) entity, durabilityDamage);
						} else {
							handleItemDurability(blockingItem, (Player) entity, (int) deflectAmount);
						}
					}
					weapon.getOrCreateTag().putDouble("DeflectAmount", 0);
					int durabilityDamage = (int) (deflectAmount * 0.5);
					handleItemDurability(weapon, player, durabilityDamage);
				}
			}
		}
	}

	private static void handleItemDurability(ItemStack itemstack, Player player, int damage) {
		if (!player.isCreative()) {
			itemstack.hurtAndBreak(damage, player, (p) -> {
				p.broadcastBreakEvent(p.getUsedItemHand());
			});
		}
	}
}
