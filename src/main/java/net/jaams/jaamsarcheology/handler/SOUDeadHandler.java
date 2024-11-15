
package net.jaams.jaamsarcheology.handler;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.advancements.CriteriaTriggers;

import net.jaams.jaamsarcheology.network.JaamsArcheologyModVariables;
import net.jaams.jaamsarcheology.item.SwordOfUndyingItem;
import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;

import java.util.Random;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SOUDeadHandler {
	public SOUDeadHandler() {
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		new SOUDeadHandler();
	}

	@Mod.EventBusSubscriber
	private static class SOUDeadHandlerForgeBusEvents {
		@SubscribeEvent
		public static void serverLoad(ServerStartingEvent event) {
		}

		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent
		public static void clientLoad(FMLClientSetupEvent event) {
		}

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void onEntityDeathSOU(LivingDeathEvent event) {
			LivingEntity entity = event.getEntity();
			Level world = entity.level();
			DamageSource source = event.getSource();
			if (source.is(DamageTypes.FELL_OUT_OF_WORLD) || source.is(DamageTypes.GENERIC_KILL) || source.is(DamageTypes.OUTSIDE_BORDER)) {
				return;
			}
			ItemStack mainHandItem = entity.getMainHandItem();
			ItemStack offHandItem = entity.getOffhandItem();
			boolean hasSwordOfUndying = (mainHandItem.getItem() instanceof SwordOfUndyingItem) || (offHandItem.getItem() instanceof SwordOfUndyingItem);
			if (hasSwordOfUndying) {
				event.setCanceled(true);
				entity.setHealth(1.0F);
				if (world instanceof ServerLevel serverLevel) {
					serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, entity.getX(), entity.getY() + 1, entity.getZ(), 100, 0.5, 0.5, 0.5, 0.5);
					world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.totem.use")), SoundSource.PLAYERS, 1.0F, 1.0F);
					if (entity instanceof ServerPlayer serverPlayer) {
						serverPlayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
						CriteriaTriggers.USED_TOTEM.trigger(serverPlayer, Items.TOTEM_OF_UNDYING.getDefaultInstance());
					}
					Random random = new Random();
					int nuggetCount = random.nextInt(3) + 1;
					for (int i = 0; i < nuggetCount; i++) {
						ItemEntity nuggetEntity = new ItemEntity(world, entity.getX(), entity.getY() + 1, entity.getZ(), new ItemStack(JaamsArcheologyModItems.PRIMORDIAL_GOLD_FRAGMENT.get()));
						double velocityX = (random.nextDouble() - 0.5) * 0.5;
						double velocityY = random.nextDouble() * 0.5 + 0.2;
						double velocityZ = (random.nextDouble() - 0.5) * 0.5;
						nuggetEntity.setDeltaMovement(velocityX, velocityY, velocityZ);
						world.addFreshEntity(nuggetEntity);
					}
				}
				{
					boolean _setval = true;
					entity.getCapability(JaamsArcheologyModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.SOUTotem = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
				entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
				entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
				entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
				ItemStack usedItem = mainHandItem.getItem() instanceof SwordOfUndyingItem ? mainHandItem : offHandItem;
				int maxDurability = usedItem.getMaxDamage();
				int remainingDurability = maxDurability - usedItem.getDamageValue();
				float areaDamage = remainingDurability * 0.05f;
				Vec3 center = new Vec3(entity.getX(), entity.getY(), entity.getZ());
				List<Entity> nearbyEntities = world.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(4)).stream().filter(e -> e != entity && (e instanceof LivingEntity))
						.filter(e -> !(e instanceof TamableAnimal tam && tam.isOwnedBy(entity))).filter(e -> !isAlly(entity, e)).toList();
				for (Entity target : nearbyEntities) {
					target.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.PLAYER_ATTACK), entity), areaDamage);
				}
				InteractionHand usedHand = mainHandItem.getItem() instanceof SwordOfUndyingItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
				usedItem.hurtAndBreak(usedItem.getMaxDamage(), entity, (e) -> {
					e.broadcastBreakEvent(usedHand);
				});
			}
		}

		private static boolean isAlly(Entity sourceEntity, Entity targetEntity) {
			if (sourceEntity instanceof LivingEntity livingSource && targetEntity instanceof LivingEntity livingTarget) {
				return livingSource.isAlliedTo(livingTarget);
			}
			return false;
		}
	}
}
