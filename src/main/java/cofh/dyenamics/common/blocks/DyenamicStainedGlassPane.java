package cofh.dyenamics.common.blocks;

import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.PaneBlock;


public class DyenamicStainedGlassPane extends PaneBlock {
    private final DyenamicDyeColor color;

    public DyenamicStainedGlassPane(DyenamicDyeColor colorIn, Properties properties) {
        super(properties);
        this.color = colorIn;
        this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, Boolean.valueOf(false)).with(EAST, Boolean.valueOf(false)).with(SOUTH, Boolean.valueOf(false)).with(WEST, Boolean.valueOf(false)).with(WATERLOGGED, Boolean.valueOf(false)));

    }

    public DyenamicDyeColor getColor() {
        return this.color;
    }
}
