package cofh.dyenamics.client.render.item;

import cofh.dyenamics.common.blockentity.DyenamicShulkerBoxBlockEntity;
import cofh.dyenamics.common.blocks.DyenamicShulkerBoxBlock;
import cofh.dyenamics.core.init.BlockInit;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class DyenamicShulkerBoxItemStackRenderer extends BlockEntityWithoutLevelRenderer
{
    DyenamicShulkerBoxBlockEntity blockEntity = null;

    public DyenamicShulkerBoxItemStackRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource buffer, int pPackedLight, int pPackedOverlay) {
        if (blockEntity == null) {
            blockEntity = new DyenamicShulkerBoxBlockEntity(BlockPos.ZERO, BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.PEACH.getSerializedName()).get("shulker_box").get().defaultBlockState());
        }
        Item item = pStack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            if (block instanceof DyenamicShulkerBoxBlock shulkerBoxBlock) {
                blockEntity.setColor(shulkerBoxBlock.getDyenamicColor());

                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(blockEntity, pPoseStack, buffer, pPackedLight, pPackedOverlay);
            }
        }
    }
}
