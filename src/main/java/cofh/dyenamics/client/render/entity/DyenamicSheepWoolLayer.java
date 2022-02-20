package cofh.dyenamics.client.render.entity;

import cofh.dyenamics.common.entities.DyenamicSheepEntity;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

public class DyenamicSheepWoolLayer extends LayerRenderer<DyenamicSheepEntity, SheepModel<DyenamicSheepEntity>> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
	protected final SheepWoolModel<DyenamicSheepEntity> sheepModel = new SheepWoolModel<>();
	public DyenamicSheepWoolLayer(IEntityRenderer<DyenamicSheepEntity, SheepModel<DyenamicSheepEntity>> rendererIn) {
		super(rendererIn);
	}
	
	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DyenamicSheepEntity sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

		if (!sheep.getSheared() && !sheep.isInvisible()) {
			float r;
			float g;
			float b;
			if (sheep.hasCustomName() && "jeb_".equals(sheep.getName().getUnformattedComponentText())) {
				int i = sheep.ticksExisted / 25 + sheep.getEntityId();
				int j = DyeColor.values().length;
				int k = i % j;
				int l = (i + 1) % j;
				float f3 = ((float)(sheep.ticksExisted % 25) + partialTicks) / 25.0F;
				float[] afloat1 = SheepEntity.getDyeRgb(DyeColor.byId(k));
				float[] afloat2 = SheepEntity.getDyeRgb(DyeColor.byId(l));
				r = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
				g = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
				b = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
			} else {
				DyenamicDyeColor color = sheep.getDyenamicFleeceColor();
				if (color.getLightValue() > 0) {
					int light = Math.min(color.getLightValue() * 2, 15);
					packedLightIn = (Math.max((packedLightIn >> 20) & 15, light) << 20) | (Math.max((packedLightIn >> 4) & 15, light) << 4);
				}
				float[] afloat = DyenamicSheepEntity.getDyeRgb(color);
				r = afloat[0];
				g = afloat[1];
				b = afloat[2];
			}
			renderCopyCutoutModel(this.getEntityModel(), this.sheepModel, TEXTURE, matrixStackIn, bufferIn, packedLightIn, sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, r, g, b);
		}
	 }
	   
}
