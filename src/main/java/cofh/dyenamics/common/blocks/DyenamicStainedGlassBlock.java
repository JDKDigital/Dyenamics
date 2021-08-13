package cofh.dyenamics.common.blocks;

import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;


public class DyenamicStainedGlassBlock extends AbstractGlassBlock
{
    private final DyenamicDyeColor color;

    public DyenamicStainedGlassBlock(DyenamicDyeColor colorIn, AbstractBlock.Properties properties) {
        super(properties);
        this.color = colorIn;
    }

    public DyenamicDyeColor getColor() {
        return this.color;
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplier(BlockState state, IWorldReader world, BlockPos pos, BlockPos beaconPos) {
        return getColor().getColorComponentValues();
    }
}
