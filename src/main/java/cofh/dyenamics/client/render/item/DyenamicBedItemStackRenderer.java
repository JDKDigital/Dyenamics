package cofh.dyenamics.client.render.item;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.blockentity.DyenamicBedBlockEntity;
import cofh.dyenamics.common.blocks.DyenamicBedBlock;
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

public class DyenamicBedItemStackRenderer extends BlockEntityWithoutLevelRenderer
{
    DyenamicBedBlockEntity blockEntity = null;

    public DyenamicBedItemStackRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int pPackedLight, int pPackedOverlay) {
        if (blockEntity == null) {
            blockEntity = new DyenamicBedBlockEntity(BlockPos.ZERO, BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.PEACH.getSerializedName()).get("bed").get().defaultBlockState());
        }
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            if (block instanceof DyenamicBedBlock bedBlock) {
                blockEntity.setColor(bedBlock.getDyenamicColor());

                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(blockEntity, poseStack, buffer, pPackedLight, pPackedOverlay);
            }
        }
    }
}
