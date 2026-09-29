package cy.jdkdigital.dyenamics.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.common.block.DyenamicCarpetBlock;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(value = LlamaDecorLayer.class)
public class MixinLlamaDecorLayer
{
    private static final Map<String, ResourceLocation> TEXTURE_LOCATIONS = Arrays.stream(DyenamicDyeColor.dyenamicValues()).collect(Collectors.toMap(DyenamicDyeColor::getSerializedName, color ->  ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "textures/entity/llama/decor/" + color.getSerializedName() + "_swag.png")));

    @ModifyVariable(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Llama;FFFFFF)V",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    public ResourceLocation renderDyenamicSwag(ResourceLocation value, PoseStack poseStack, MultiBufferSource buffer, int packedLight, Llama livingEntity) {
        var swagBlockItem = Block.byItem(livingEntity.getItemBySlot(EquipmentSlot.BODY).getItem());
        if (swagBlockItem instanceof DyenamicCarpetBlock dyenamicCarpetBlock) {
            return TEXTURE_LOCATIONS.get(dyenamicCarpetBlock.getDyenamicColor().getSerializedName());
        }
        return value;
    }

}
