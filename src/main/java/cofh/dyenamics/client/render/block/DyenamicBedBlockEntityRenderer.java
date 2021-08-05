package cofh.dyenamics.client.render.block;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.blockentity.DyenamicBedBlockEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tileentity.BedTileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class DyenamicBedBlockEntityRenderer extends TileEntityRenderer<DyenamicBedBlockEntity>
{
    private final ModelRenderer bedHead;
    private final ModelRenderer bedFoot;
    private final ModelRenderer[] bedLegs = new ModelRenderer[4];

    public DyenamicBedBlockEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        this.bedHead = new ModelRenderer(64, 64, 0, 0);
        this.bedHead.addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 6.0F, 0.0F);
        this.bedFoot = new ModelRenderer(64, 64, 0, 22);
        this.bedFoot.addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 6.0F, 0.0F);
        this.bedLegs[0] = new ModelRenderer(64, 64, 50, 0);
        this.bedLegs[1] = new ModelRenderer(64, 64, 50, 6);
        this.bedLegs[2] = new ModelRenderer(64, 64, 50, 12);
        this.bedLegs[3] = new ModelRenderer(64, 64, 50, 18);
        this.bedLegs[0].addBox(0.0F, 6.0F, -16.0F, 3.0F, 3.0F, 3.0F);
        this.bedLegs[1].addBox(0.0F, 6.0F, 0.0F, 3.0F, 3.0F, 3.0F);
        this.bedLegs[2].addBox(-16.0F, 6.0F, -16.0F, 3.0F, 3.0F, 3.0F);
        this.bedLegs[3].addBox(-16.0F, 6.0F, 0.0F, 3.0F, 3.0F, 3.0F);
        this.bedLegs[0].rotateAngleX = ((float)Math.PI / 2F);
        this.bedLegs[1].rotateAngleX = ((float)Math.PI / 2F);
        this.bedLegs[2].rotateAngleX = ((float)Math.PI / 2F);
        this.bedLegs[3].rotateAngleX = ((float)Math.PI / 2F);
        this.bedLegs[0].rotateAngleZ = 0.0F;
        this.bedLegs[1].rotateAngleZ = ((float)Math.PI / 2F);
        this.bedLegs[2].rotateAngleZ = ((float)Math.PI * 1.5F);
        this.bedLegs[3].rotateAngleZ = (float)Math.PI;
    }

    @Override
    public void render(@Nonnull DyenamicBedBlockEntity blockEntity, float v, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        RenderMaterial rendermaterial = Dyenamics.BED_MATERIAL_MAP.get(blockEntity.getColor().getString());

        World world = blockEntity.getWorld();
        if (world != null) {
            BlockState blockstate = blockEntity.getBlockState();
            TileEntityMerger.ICallbackWrapper<? extends BedTileEntity> callbackWrapper = TileEntityMerger.func_226924_a_(TileEntityType.BED, BedBlock::getMergeType, BedBlock::getFootDirection, ChestBlock.FACING, blockstate, world, blockEntity.getPos(), (p_228846_0_, p_228846_1_) -> false);
            int i = callbackWrapper.<Int2IntFunction>apply(new DualBrightnessCallback<>()).get(combinedLightIn);
            this.renderBed(matrixStack, buffer, blockstate.get(BedBlock.PART) == BedPart.HEAD, blockstate.get(BedBlock.HORIZONTAL_FACING), rendermaterial, i, combinedOverlayIn, false);
        } else {
            this.renderBed(matrixStack, buffer, true, Direction.SOUTH, rendermaterial, combinedLightIn, combinedOverlayIn, false);
            this.renderBed(matrixStack, buffer, false, Direction.SOUTH, rendermaterial, combinedLightIn, combinedOverlayIn, true);
        }

    }

    private void renderBed(MatrixStack matrixStack, IRenderTypeBuffer buffer, boolean renderHead, Direction direction, RenderMaterial rendermaterial, int combinedLightIn, int combinedOverlayIn, boolean itsABoolean) {
        this.bedHead.showModel = renderHead;
        this.bedFoot.showModel = !renderHead;
        this.bedLegs[0].showModel = !renderHead;
        this.bedLegs[1].showModel = renderHead;
        this.bedLegs[2].showModel = !renderHead;
        this.bedLegs[3].showModel = renderHead;
        matrixStack.push();
        matrixStack.translate(0.0D, 0.5625D, itsABoolean ? -1.0D : 0.0D);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(180.0F + direction.getHorizontalAngle()));
        matrixStack.translate(-0.5D, -0.5D, -0.5D);

        IVertexBuilder ivertexbuilder = rendermaterial.getBuffer(buffer, RenderType::getEntitySolid);
        this.bedHead.render(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        this.bedFoot.render(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        this.bedLegs[0].render(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        this.bedLegs[1].render(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        this.bedLegs[2].render(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        this.bedLegs[3].render(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        matrixStack.pop();
    }
}
