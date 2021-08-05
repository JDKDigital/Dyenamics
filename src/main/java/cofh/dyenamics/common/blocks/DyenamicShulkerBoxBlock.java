package cofh.dyenamics.common.blocks;

import cofh.dyenamics.common.blockentity.DyenamicShulkerBoxBlockEntity;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class DyenamicShulkerBoxBlock extends ShulkerBoxBlock
{
    private final DyenamicDyeColor color;

    public DyenamicShulkerBoxBlock(DyenamicDyeColor colorIn, Properties properties) {
        super(DyeColor.PURPLE, properties);
        color = colorIn;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new DyenamicShulkerBoxBlockEntity(this.color);
    }

    public DyenamicDyeColor getDyenamicColor() {
        return color;
    }
}
