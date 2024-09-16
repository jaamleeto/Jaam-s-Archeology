package net.jaams.jaamsarcheology.procedures;

import org.joml.Vector3f;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.BlockPos;

public class RottenVigorEffectStartedappliedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!entity.level().isClientSide()) {
			ServerLevel serverWorld = (ServerLevel) entity.level();
			float red = 0.0f; // Sin rojo
			float green = 0.5f; // Verde oscuro
			float blue = 0.0f; // Sin azul
			float particleSize = 1.5f;
			double particleSpeed = 0.02d;
			double spawnRadius = 0.5d;
			serverWorld.sendParticles(new DustParticleOptions(new Vector3f(red, green, blue), particleSize), entity.getX(), entity.getY() + 1, entity.getZ(), 40, spawnRadius, spawnRadius, spawnRadius, particleSpeed);
		}
		if (entity instanceof Player || entity instanceof ServerPlayer) {
			if (!world.isClientSide()) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:rotten_idol_started")), SoundSource.PLAYERS, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:rotten_idol_started")), SoundSource.PLAYERS, 1, 1, false);
					}
				}
			}
		}
	}
}
