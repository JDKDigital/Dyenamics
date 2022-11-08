package cofh.dyenamics.common.blockentity;

import cofh.dyenamics.common.blocks.DyenamicBedBlock;
import cofh.dyenamics.core.init.BlockEntityInit;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DyenamicBedBlockEntity extends BlockEntity
{
    private DyenamicDyeColor color;

    public DyenamicBedBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.BED.get(), blockPos, blockState);
    }

    public DyenamicBedBlockEntity(DyenamicDyeColor colorIn, BlockPos blockPos, BlockState blockState) {
        this(blockPos, blockState);
        this.setColor(colorIn);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public DyenamicDyeColor getColor() {
        if (this.color == null) {
            this.color = ((DyenamicBedBlock) this.getBlockState().getBlock()).getDyenamicColor();
        }

        return this.color;
    }

    public void setColor(DyenamicDyeColor color) {
        this.color = color;
    }
}
