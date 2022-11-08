package cofh.dyenamics.common.blockentity;

import cofh.dyenamics.common.blocks.DyenamicShulkerBoxBlock;
import cofh.dyenamics.core.init.BlockEntityInit;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class DyenamicShulkerBoxBlockEntity extends ShulkerBoxBlockEntity
{
    private DyenamicDyeColor color;

    public DyenamicShulkerBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(DyeColor.PURPLE, blockPos, blockState);
    }

    public DyenamicShulkerBoxBlockEntity(DyenamicDyeColor colorIn, BlockPos blockPos, BlockState blockState) {
        this(blockPos, blockState);
        this.setColor(colorIn);
    }

    @Nonnull
    @Override
    public BlockEntityType<?> getType() {
        return BlockEntityInit.SHULKER_BOX.get();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public DyenamicDyeColor getDyenamicColor() {
        if (this.color == null) {
            this.color = ((DyenamicShulkerBoxBlock) this.getBlockState().getBlock()).getDyenamicColor();
        }

        return this.color;
    }

    public void setColor(DyenamicDyeColor color) {
        this.color = color;
    }
}
