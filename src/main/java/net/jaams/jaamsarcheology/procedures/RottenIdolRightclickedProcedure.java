package net.jaams.jaamsarcheology.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

import net.jaams.jaamsarcheology.network.JaamsArcheologyModVariables;
import net.jaams.jaamsarcheology.init.JaamsArcheologyModMobEffects;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyCommonConfiguration;
import net.jaams.jaamsarcheology.JaamsArcheologyMod;

public class RottenIdolRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (JaamsArcheologyCommonConfiguration.IDOLS.get() == true) {
			if (!world.isClientSide()) {
				ServerLevel serverWorld = (ServerLevel) world;
				Item item = itemstack.getItem(); // Get the Item from the ItemStack
				double xOffset = entity.getX() + entity.getLookAngle().x * 0.5; // Adjusted x coordinate
				double yOffset = entity.getY() + entity.getEyeHeight(); // Slightly above the player
				double zOffset = entity.getZ() + entity.getLookAngle().z * 0.5; // Adjusted z coordinate
				serverWorld.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemstack), xOffset, yOffset, zOffset, 5, 0.1d, 0.1d, 0.1d, 0.05d);
			}
			if (world.isClientSide()) {
				if (world.isClientSide())
					Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
			}
			if (entity instanceof Player _player)
				_player.getCooldowns().addCooldown(itemstack.getItem(), 20);
			if (!world.isClientSide()) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.totem.use")), SoundSource.PLAYERS, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.totem.use")), SoundSource.PLAYERS, 1, 1, false);
					}
				}
			}
			{
				boolean _setval = true;
				entity.getCapability(JaamsArcheologyModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.Shake = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
			JaamsArcheologyMod.queueServerWork(3, () -> {
				{
					boolean _setval = false;
					entity.getCapability(JaamsArcheologyModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.Shake = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
			});
			if (!(new Object() {
				public boolean checkGamemode(Entity _ent) {
					if (_ent instanceof ServerPlayer _serverPlayer) {
						return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
					} else if (_ent.level().isClientSide() && _ent instanceof Player _player) {
						return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
								&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
					}
					return false;
				}
			}.checkGamemode(entity))) {
				itemstack.shrink(1);
			}
			if (JaamsArcheologyCommonConfiguration.ROTTENVIGOR.get() == true && !world.isClientSide()) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(JaamsArcheologyModMobEffects.ROTTEN_VIGOR.get(), 800, 0));
			}
			if (!world.isClientSide()) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:idol_breaks")), SoundSource.PLAYERS, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:idol_breaks")), SoundSource.PLAYERS, 1, 1, false);
					}
				}
			}
		}
	}
}
