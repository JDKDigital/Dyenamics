package cy.jdkdigital.dyenamics.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = CatCollarLayer.class)
public class MixinCatCollarLayer
{
    @ModifyVariable(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Cat;FFFFFF)V",
            ordinal = 1,
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/item/DyeColor;getTextureDiffuseColor()I",
                    ordinal = 0
            )
    )
    public int renderDyenamicCollar(int value, PoseStack poseStack, MultiBufferSource buffer, int packedLight, Cat livingEntity) {
        var colorId = livingEntity.getEntityData().get(Cat.DATA_COLLAR_COLOR);
        return DyenamicDyeColor.byId(colorId).getColorComponentValue();
    }
}
