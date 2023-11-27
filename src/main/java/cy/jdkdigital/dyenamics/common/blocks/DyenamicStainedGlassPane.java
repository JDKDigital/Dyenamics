package cy.jdkdigital.dyenamics.common.blocks;

import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;

public class DyenamicStainedGlassPane extends IronBarsBlock
{
    private final DyenamicDyeColor color;

    public DyenamicStainedGlassPane(DyenamicDyeColor colorIn, Properties properties) {
        super(properties);
        this.color = colorIn;
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
    }

    public DyenamicDyeColor getColor() {
        return this.color;
    }

    @Override
    public float[] getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
        return getColor().getColorComponentValues();
    }
}
