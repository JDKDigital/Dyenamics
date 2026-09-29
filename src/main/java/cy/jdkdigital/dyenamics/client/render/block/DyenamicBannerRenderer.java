package cy.jdkdigital.dyenamics.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import cy.jdkdigital.dyenamics.common.block.DyenamicBannerBlock;
import cy.jdkdigital.dyenamics.common.blockentity.DyenamicBannerBlockEntity;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public class DyenamicBannerRenderer extends BannerRenderer
{
    public DyenamicBannerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BannerBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        boolean isItem = pBlockEntity.getLevel() == null;
        pPoseStack.pushPose();
        long i;
        if (isItem) {
            i = 0L;
            pPoseStack.translate(0.5F, 0.5F, 0.5F);
            this.pole.visible = true;
        } else {
            i = pBlockEntity.getLevel().getGameTime();
            BlockState blockstate = pBlockEntity.getBlockState();
            if (blockstate.getBlock() instanceof DyenamicBannerBlock) {
                pPoseStack.translate(0.5F, 0.5F, 0.5F);
                float f1 = -RotationSegment.convertToDegrees(blockstate.getValue(BannerBlock.ROTATION));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(f1));
                this.pole.visible = true;
            } else {
                pPoseStack.translate(0.5F, -0.16666667F, 0.5F);
                float f3 = -blockstate.getValue(WallBannerBlock.FACING).toYRot();
                pPoseStack.mulPose(Axis.YP.rotationDegrees(f3));
                pPoseStack.translate(0.0F, -0.3125F, -0.4375F);
                this.pole.visible = false;
            }
        }

        pPoseStack.pushPose();
        pPoseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        VertexConsumer vertexconsumer = ModelBakery.BANNER_BASE.buffer(pBuffer, RenderType::entitySolid);
        this.pole.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay);
        this.bar.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay);
        BlockPos blockpos = pBlockEntity.getBlockPos();
        float f2 = ((float)Math.floorMod((long)(blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13) + i, 100L) + pPartialTick) / 100.0F;
        this.flag.xRot = (-0.0125F + 0.01F * Mth.cos((float) (Math.PI * 2) * f2)) * (float) Math.PI;
        this.flag.y = -32.0F;
        DyenamicDyeColor baseColor = pBlockEntity instanceof DyenamicBannerBlockEntity dyenamicBannerBlockEntity ? dyenamicBannerBlockEntity.getDyenamicColor() : DyenamicDyeColor.getColor(pBlockEntity.getBaseColor());
        renderPatterns(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, this.flag, ModelBakery.BANNER_BASE, true, baseColor, pBlockEntity.getPatterns(), false);
        pPoseStack.popPose();
        pPoseStack.popPose();
    }

    public static void renderPatterns(
            PoseStack pPoseStack,
            MultiBufferSource pBuffer,
            int pPackedLight,
            int pPackedOverlay,
            ModelPart pFlagPart,
            Material pFlagMaterial,
            boolean pBanner,
            DyenamicDyeColor pBaseColor,
            BannerPatternLayers pPatterns,
            boolean pGlint
    ) {
        pFlagPart.render(pPoseStack, pFlagMaterial.buffer(pBuffer, RenderType::entitySolid, pGlint), pPackedLight, pPackedOverlay);
        renderPatternLayer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pFlagPart, pBanner ? Sheets.BANNER_BASE : Sheets.SHIELD_BASE, pBaseColor);

        for (int i = 0; i < 16 && i < pPatterns.layers().size(); i++) {
            BannerPatternLayers.Layer patternLayer = pPatterns.layers().get(i);
            Material material = pBanner
                    ? Sheets.getBannerMaterial(patternLayer.pattern())
                    : Sheets.getShieldMaterial(patternLayer.pattern());
            renderPatternLayer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pFlagPart, material, DyenamicDyeColor.getColor(patternLayer.color()));
        }
    }

    private static void renderPatternLayer(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, ModelPart flagPart, Material material, DyenamicDyeColor color
    ) {
        int i = color.getColorComponentValue();
        flagPart.render(poseStack, material.buffer(buffer, RenderType::entityNoOutline), packedLight, packedOverlay, i);
    }
}
