package cy.jdkdigital.dyenamics.common.blocks;

import cy.jdkdigital.dyenamics.common.blockentity.DyenamicShulkerBoxBlockEntity;
import cy.jdkdigital.dyenamics.core.init.BlockEntityInit;
import cy.jdkdigital.dyenamics.core.init.BlockInit;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class DyenamicShulkerBoxBlock extends ShulkerBoxBlock
{
    private final DyenamicDyeColor color;

    public DyenamicShulkerBoxBlock(DyenamicDyeColor colorIn, Properties properties) {
        super(DyeColor.PURPLE, properties);
        color = colorIn;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DyenamicShulkerBoxBlockEntity(this.color, pPos, pState);
    }

    public DyenamicDyeColor getDyenamicColor() {
        return color;
    }

    public static ItemStack getDyenamicColoredItemStack(DyenamicDyeColor colorIn) {
        return new ItemStack(BlockInit.DYED_BLOCKS.get(colorIn.getSerializedName()).get("shulker_box").get());
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, BlockEntityInit.SHULKER_BOX.get(), ShulkerBoxBlockEntity::tick);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ShulkerBoxBlockEntity shulkerboxBlockEntity) {
            if (!level.isClientSide && player.isCreative() && !shulkerboxBlockEntity.isEmpty()) {
                ItemStack itemstack = getDyenamicColoredItemStack(getDyenamicColor());
                shulkerboxBlockEntity.saveToItem(itemstack);
                if (shulkerboxBlockEntity.hasCustomName()) {
                    itemstack.setHoverName(shulkerboxBlockEntity.getCustomName());
                }

                ItemEntity itementity = new ItemEntity(level, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, itemstack);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
            } else {
                shulkerboxBlockEntity.unpackLootTable(player);
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }
}
