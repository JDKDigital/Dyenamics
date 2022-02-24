package cofh.dyenamics.common.blocks;

import cofh.dyenamics.common.blockentity.DyenamicShulkerBoxBlockEntity;
import cofh.dyenamics.core.init.BlockInit;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ShulkerBoxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class DyenamicShulkerBoxBlock extends ShulkerBoxBlock {

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

    public static ItemStack getDyenamicColoredItemStack(DyenamicDyeColor colorIn) {
        return new ItemStack(BlockInit.DYED_BLOCKS.get(colorIn.getString()).get("shulker_box").get());
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof ShulkerBoxTileEntity) {
            ShulkerBoxTileEntity shulkerboxtileentity = (ShulkerBoxTileEntity)tileentity;
            if (!worldIn.isRemote && player.isCreative() && !shulkerboxtileentity.isEmpty()) {
                ItemStack itemstack = getDyenamicColoredItemStack(getDyenamicColor());
                CompoundNBT compoundnbt = shulkerboxtileentity.saveToNbt(new CompoundNBT());
                if (!compoundnbt.isEmpty()) {
                    itemstack.setTagInfo("BlockEntityTag", compoundnbt);
                }

                if (shulkerboxtileentity.hasCustomName()) {
                    itemstack.setDisplayName(shulkerboxtileentity.getCustomName());
                }

                ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemstack);
                itementity.setDefaultPickupDelay();
                worldIn.addEntity(itementity);
            } else {
                shulkerboxtileentity.fillWithLoot(player);
            }
        }

        worldIn.playEvent(player, 2001, pos, getStateId(state));
        if (this.isIn(BlockTags.GUARDED_BY_PIGLINS)) {
            PiglinTasks.func_234478_a_(player, false);
        }
    }

}
