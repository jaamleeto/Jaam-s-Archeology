
package net.jaams.jaamsarcheology.block;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.common.util.ForgeSoundType;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

public class PrimordialGoldBlockBlock extends Block {
	public PrimordialGoldBlockBlock() {
		super(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM)
				.sound(new ForgeSoundType(1.0f, 1.0f, () -> ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.metal.break")), () -> ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.copper.step")),
						() -> ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.metal.place")), () -> ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.metal.hit")),
						() -> ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.metal.fall"))))
				.strength(3f, 6f));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}
