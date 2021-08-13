package cofh.dyenamics.client.render.block;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.blockentity.DyenamicShulkerBoxBlockEntity;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.ShulkerModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;

public class DyenamicShulkerBoxBlockEntityRenderer extends TileEntityRenderer<DyenamicShulkerBoxBlockEntity>
{
    ShulkerModel<?> model = new ShulkerModel();

    public DyenamicShulkerBoxBlockEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(@Nonnull DyenamicShulkerBoxBlockEntity blockEntity, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = Direction.UP;
        if (blockEntity.hasWorld()) {
            BlockState blockstate = blockEntity.getWorld().getBlockState(blockEntity.getPos());
            if (blockstate.getBlock() instanceof ShulkerBoxBlock) {
                direction = blockstate.get(ShulkerBoxBlock.FACING);
            }
        }

        DyenamicDyeColor dyeColor = blockEntity.getDyenamicColor();
        RenderMaterial rendermaterial;
        if (dyeColor == null) {
            rendermaterial = Atlases.DEFAULT_SHULKER_TEXTURE;
        } else {
            rendermaterial = Dyenamics.SHULKER_MATERIAL_MAP.get(dyeColor.getString());
        }

        matrixStack.push();
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.scale(0.9995F, 0.9995F, 0.9995F);
        matrixStack.rotate(direction.getRotation());
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        matrixStack.translate(0.0D, -1.0D, 0.0D);
        IVertexBuilder ivertexbuilder = rendermaterial.getBuffer(buffer, RenderType::getEntityCutoutNoCull);
        this.model.getBase().render(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        matrixStack.translate(0.0D, -blockEntity.getProgress(partialTicks) * 0.5F, 0.0D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(270.0F * blockEntity.getProgress(partialTicks)));
        this.model.getLid().render(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        matrixStack.pop();
    }
}
