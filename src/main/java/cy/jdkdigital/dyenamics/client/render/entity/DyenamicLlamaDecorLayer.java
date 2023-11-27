package cy.jdkdigital.dyenamics.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Llama;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class DyenamicLlamaDecorLayer extends LlamaDecorLayer
{
    private static final Map<String, ResourceLocation> TEXTURE_LOCATIONS = Arrays.stream(DyenamicDyeColor.dyenamicValues()).collect(Collectors.toMap(DyenamicDyeColor::getSerializedName, color -> new ResourceLocation(Dyenamics.MOD_ID, "textures/entity/llama/decor/" + color.getSerializedName() + "_swag.png")));
    private final LlamaModel<Llama> model;

    public DyenamicLlamaDecorLayer(RenderLayerParent<Llama, LlamaModel<Llama>> layerParent, EntityModelSet modelSet) {
        super(layerParent, modelSet);
        this.model = new LlamaModel<>(modelSet.bakeLayer(ModelLayers.LLAMA_DECOR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, Llama llama, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        llama.getCapability(Dyenamics.DYENAMIC_SWAG).ifPresent(swagProvider -> {
            if (swagProvider.getSwagId() >= 16) {
                DyenamicDyeColor color = DyenamicDyeColor.byId(swagProvider.getSwagId());
                ResourceLocation resourcelocation;
                if (color != null) {
                    resourcelocation = TEXTURE_LOCATIONS.get(color.getSerializedName());
                    this.getParentModel().copyPropertiesTo(this.model);
                    this.model.setupAnim(llama, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                    VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(resourcelocation));
                    this.model.renderToBuffer(poseStack, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                }
            } else {
                super.render(poseStack, bufferIn, packedLightIn, llama, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        });
    }
}
