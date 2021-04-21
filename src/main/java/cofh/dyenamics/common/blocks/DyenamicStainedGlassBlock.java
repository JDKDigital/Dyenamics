package cofh.dyenamics.common.blocks;

import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractGlassBlock;


public class DyenamicStainedGlassBlock extends AbstractGlassBlock {
    private final DyenamicDyeColor color;

    public DyenamicStainedGlassBlock(DyenamicDyeColor colorIn, AbstractBlock.Properties properties) {
        super(properties);
        this.color = colorIn;
    }

    public DyenamicDyeColor getColor() {
        return this.color;
    }
}
