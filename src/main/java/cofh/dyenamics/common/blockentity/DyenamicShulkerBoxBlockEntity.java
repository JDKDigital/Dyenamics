package cofh.dyenamics.common.blockentity;

import cofh.dyenamics.common.blocks.DyenamicShulkerBoxBlock;
import cofh.dyenamics.core.init.BlockEntityInit;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.item.DyeColor;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ShulkerBoxTileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public class DyenamicShulkerBoxBlockEntity extends ShulkerBoxTileEntity
{
    private DyenamicDyeColor color;

    public DyenamicShulkerBoxBlockEntity() {
        super(DyeColor.PURPLE);
    }

    public DyenamicShulkerBoxBlockEntity(DyenamicDyeColor colorIn) {
        this();
        this.setColor(colorIn);
    }

    @Nonnull
    @Override
    public TileEntityType<?> getType() {
        return BlockEntityInit.SHULKER_BOX.get();
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 11, this.getUpdateTag());
    }

    public DyenamicDyeColor getDyenamicColor() {
        if (this.color == null) {
            this.color = ((DyenamicShulkerBoxBlock)this.getBlockState().getBlock()).getDyenamicColor();
        }

        return this.color;
    }

    public void setColor(DyenamicDyeColor color) {
        this.color = color;
    }
}
