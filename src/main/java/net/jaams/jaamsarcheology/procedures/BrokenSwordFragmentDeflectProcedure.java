package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.fml.common.Mod;
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
	private static final double MAX_DEFLECT_AMOUNT = 70.0; // Limite de daño a guardar

	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		if (event != null && event.getEntity() instanceof LivingEntity && ((LivingEntity) event.getEntity()).isBlocking()) {
			LivingEntity livingEntity = (LivingEntity) event.getEntity();
			ItemStack blockingItem = livingEntity.getUseItem();
			if (blockingItem.getItem() == JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()) {
				DamageSource damageSource = event.getSource();
				Entity directEntity = damageSource.getDirectEntity(); // Entidad directa que causa el daño
				// Verificar si el daño proviene de una entidad o un proyectil
				if (directEntity instanceof LivingEntity || directEntity instanceof Projectile) {
					double deflectAmount = Math.min(event.getAmount(), MAX_DEFLECT_AMOUNT);
					blockingItem.getOrCreateTag().putDouble("DeflectAmount", deflectAmount);
					if (directEntity instanceof LivingEntity attacker) {
						blockingItem.getOrCreateTag().putString("AttackerName", attacker.getName().getString());
					} else if (directEntity instanceof Projectile projectile) {
						if (projectile.getOwner() instanceof LivingEntity projectileOwner) {
							blockingItem.getOrCreateTag().putString("AttackerName", projectileOwner.getName().getString());
						} else {
							blockingItem.getOrCreateTag().putString("AttackerName", "Projectile");
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityAttack(LivingHurtEvent event) {
		LivingEntity entity = event.getEntity();
		DamageSource source = event.getSource();
		Entity attacker = source.getEntity();
		if (attacker instanceof Player player) {
			ItemStack weapon = player.getMainHandItem();
			if (weapon.getItem() == JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()) {
				double deflectAmount = weapon.getOrCreateTag().getDouble("DeflectAmount");
				if (deflectAmount > 0) {
					// Aplicar el daño reflejado al objetivo
					event.setAmount((float) (event.getAmount() + deflectAmount));
					// Verificar si la entidad objetivo está bloqueando con un escudo o arma
					if (entity.isBlocking()) {
						ItemStack blockingItem = entity.getUseItem();
						if (blockingItem.getItem() == JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()) {
							// Transferir el daño reflejado al nuevo arma bloqueadora
							blockingItem.getOrCreateTag().putDouble("DeflectAmount", deflectAmount);
							// Opcional: Aplicar un porcentaje del daño reflejado a la durabilidad del arma bloqueadora
							int durabilityDamage = (int) (deflectAmount * 0.5);
							handleItemDurability(blockingItem, (Player) entity, durabilityDamage);
						} else {
							// Si está usando otro tipo de item (escudo, etc.), reducir la durabilidad del mismo
							handleItemDurability(blockingItem, (Player) entity, (int) deflectAmount);
						}
					}
					// Restablecer el valor de DeflectAmount en el arma original
					weapon.getOrCreateTag().putDouble("DeflectAmount", 0);
					// Aplicar el 50% del daño reflejado a la durabilidad del arma original
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
