package cy.jdkdigital.dyenamics.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import cy.jdkdigital.dyenamics.common.entities.DyenamicSheep;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

public class DyenamicSheepFurLayer extends RenderLayer<DyenamicSheep, SheepModel<DyenamicSheep>>
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    protected final SheepFurModel<DyenamicSheep> sheepModel;

    public DyenamicSheepFurLayer(RenderLayerParent<DyenamicSheep, SheepModel<DyenamicSheep>> rendererIn, EntityModelSet modelSet) {
        super(rendererIn);
        this.sheepModel = new SheepFurModel<>(modelSet.bakeLayer(ModelLayers.SHEEP_FUR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, DyenamicSheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!sheep.isSheared() && !sheep.isInvisible()) {
            float r;
            float g;
            float b;
            if (sheep.hasCustomName() && "jeb_".equals(sheep.getName().getContents())) {
                int i = sheep.tickCount / 25 + sheep.getId();
                int j = DyeColor.values().length;
                int k = i % j;
                int l = (i + 1) % j;
                float f3 = ((float) (sheep.tickCount % 25) + partialTicks) / 25.0F;
                float[] afloat1 = Sheep.getColorArray(DyeColor.byId(k));
                float[] afloat2 = Sheep.getColorArray(DyeColor.byId(l));
                r = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
                g = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
                b = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
            } else {
                DyenamicDyeColor color = sheep.getDyenamicColor();
                if (color.getLightValue() > 0) {
                    int light = Math.min(color.getLightValue() * 2, 15);
                    packedLightIn = (Math.max((packedLightIn >> 20) & 15, light) << 20) | (Math.max((packedLightIn >> 4) & 15, light) << 4);
                }
                float[] afloat = DyenamicSheep.getDyeRgb(color);
                r = afloat[0];
                g = afloat[1];
                b = afloat[2];
            }
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.sheepModel, TEXTURE, poseStack, bufferIn, packedLightIn, sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, r, g, b);
        }
    }

}
