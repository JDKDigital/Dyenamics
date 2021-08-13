package cofh.dyenamics.common.blocks;

import cofh.dyenamics.common.blockentity.DyenamicBedBlockEntity;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.BedBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class DyenamicBedBlock extends BedBlock
{
    private final DyenamicDyeColor color;

    public DyenamicBedBlock(DyenamicDyeColor colorIn, Properties properties) {
        super(DyeColor.WHITE, properties);
        color = colorIn;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new DyenamicBedBlockEntity(this.color);
    }

    public DyenamicDyeColor getDyenamicColor() {
        return color;
    }
}
