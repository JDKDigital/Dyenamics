package cofh.dyenamics.common.blockentity;

import cofh.dyenamics.common.blocks.DyenamicBedBlock;
import cofh.dyenamics.core.init.BlockEntityInit;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

public class DyenamicBedBlockEntity extends TileEntity {

    private DyenamicDyeColor color;

    public DyenamicBedBlockEntity() {
        super(BlockEntityInit.BED.get());
    }

    public DyenamicBedBlockEntity(DyenamicDyeColor colorIn) {
        this();
        this.setColor(colorIn);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 11, this.getUpdateTag());
    }

    public DyenamicDyeColor getColor() {
        if (this.color == null) {
            this.color = ((DyenamicBedBlock)this.getBlockState().getBlock()).getDyenamicColor();
        }

        return this.color;
    }

    public void setColor(DyenamicDyeColor color) {
        this.color = color;
    }
}
