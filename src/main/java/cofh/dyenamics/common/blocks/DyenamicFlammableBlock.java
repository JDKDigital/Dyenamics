package cofh.dyenamics.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DyenamicFlammableBlock extends Block
{
    int encouragement;
    int flammability;

    public DyenamicFlammableBlock(Properties properties) {
        this(properties, 30, 60);
    }

    public DyenamicFlammableBlock(Properties properties, int encouragementIn, int flammabilityIn) {
        super(properties);
        this.encouragement = encouragementIn;
        this.flammability = flammabilityIn;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return encouragement;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return flammability;
    }
}
