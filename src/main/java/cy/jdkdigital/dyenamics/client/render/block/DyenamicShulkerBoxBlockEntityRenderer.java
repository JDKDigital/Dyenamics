package cy.jdkdigital.dyenamics.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.common.blockentity.DyenamicShulkerBoxBlockEntity;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;

public class DyenamicShulkerBoxBlockEntityRenderer implements BlockEntityRenderer<DyenamicShulkerBoxBlockEntity>
{
    private final ShulkerModel<?> model;

    public DyenamicShulkerBoxBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.model = new ShulkerModel<>(pContext.bakeLayer(ModelLayers.SHULKER));
    }

    public void render(DyenamicShulkerBoxBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        Direction direction = Direction.UP;
        if (pBlockEntity.hasLevel()) {
            BlockState blockstate = pBlockEntity.getLevel().getBlockState(pBlockEntity.getBlockPos());
            if (blockstate.getBlock() instanceof ShulkerBoxBlock) {
                direction = blockstate.getValue(ShulkerBoxBlock.FACING);
            }
        }

        DyenamicDyeColor dyecolor = pBlockEntity.getDyenamicColor();
        Material material = Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION;
        if (dyecolor != null && Dyenamics.SHULKER_MATERIAL_MAP.containsKey(dyecolor.getSerializedName())) {
            material = Dyenamics.SHULKER_MATERIAL_MAP.get(dyecolor.getSerializedName());
        }

        pPoseStack.pushPose();
        pPoseStack.translate(0.5D, 0.5D, 0.5D);
        pPoseStack.scale(0.9995F, 0.9995F, 0.9995F);
        pPoseStack.mulPose(direction.getRotation());
        pPoseStack.scale(1.0F, -1.0F, -1.0F);
        pPoseStack.translate(0.0D, -1.0D, 0.0D);
        ModelPart modelpart = this.model.getLid();
        modelpart.setPos(0.0F, 24.0F - pBlockEntity.getProgress(pPartialTick) * 0.5F * 16.0F, 0.0F);
        modelpart.yRot = 270.0F * pBlockEntity.getProgress(pPartialTick) * ((float) Math.PI / 180F);
        VertexConsumer vertexconsumer = material.buffer(pBufferSource, RenderType::entityCutoutNoCull);
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
    }
}
