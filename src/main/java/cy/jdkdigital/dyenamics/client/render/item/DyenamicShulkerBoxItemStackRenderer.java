package cy.jdkdigital.dyenamics.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import cy.jdkdigital.dyenamics.common.blockentity.DyenamicShulkerBoxBlockEntity;
import cy.jdkdigital.dyenamics.common.blocks.DyenamicShulkerBoxBlock;
import cy.jdkdigital.dyenamics.core.init.BlockInit;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class DyenamicShulkerBoxItemStackRenderer extends BlockEntityWithoutLevelRenderer
{
    DyenamicShulkerBoxBlockEntity blockEntity = null;

    public DyenamicShulkerBoxItemStackRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pTransformType, PoseStack pPoseStack, MultiBufferSource buffer, int pPackedLight, int pPackedOverlay) {
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
