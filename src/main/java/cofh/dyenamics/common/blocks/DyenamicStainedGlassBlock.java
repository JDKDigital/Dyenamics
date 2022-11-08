package cofh.dyenamics.common.blocks;

import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DyenamicStainedGlassBlock extends AbstractGlassBlock
{
    private final DyenamicDyeColor color;

    public DyenamicStainedGlassBlock(DyenamicDyeColor colorIn, Block.Properties properties) {
        super(properties);
        this.color = colorIn;
    }

    public DyenamicDyeColor getColor() {
        return this.color;
    }

    @Override
    public float[] getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
        return getColor().getColorComponentValues();
    }
}
