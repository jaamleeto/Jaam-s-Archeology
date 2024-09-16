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

public class ExplosiveVigorEffectStartedappliedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!entity.level().isClientSide()) {
			ServerLevel serverWorld = (ServerLevel) entity.level();
			float red = 0.0f;
			float green = 1.0f;
			float blue = 0.0f;
			float particleSize = 1.5f;
			double particleSpeed = 0.02d; // Velocidad reducida para simular dispersión de humo
			double spawnRadius = 0.5d; // Radio más pequeño para generar las partículas más cerca del jugador
			serverWorld.sendParticles(new DustParticleOptions(new Vector3f(red, green, blue), particleSize), entity.getX(), entity.getY() + 1, entity.getZ(), 40, // Mayor número de partículas para un efecto más denso
					spawnRadius, spawnRadius, spawnRadius, particleSpeed);
		}
		if (entity instanceof Player || entity instanceof ServerPlayer) {
			if (!world.isClientSide()) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:explosive_idol_started")), SoundSource.PLAYERS, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("jaams_archeology:explosive_idol_started")), SoundSource.PLAYERS, 1, 1, false);
					}
				}
			}
		}
	}
}
