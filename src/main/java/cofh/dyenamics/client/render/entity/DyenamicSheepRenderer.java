package cofh.dyenamics.client.render.entity;

import cofh.dyenamics.common.entities.DyenamicSheepEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DyenamicSheepRenderer extends MobRenderer<DyenamicSheepEntity, SheepModel<DyenamicSheepEntity>> {
   private static final ResourceLocation SHEARED_SHEEP_TEXTURES = new ResourceLocation("textures/entity/sheep/sheep.png");

   public DyenamicSheepRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new SheepModel<>(), 0.7F);
      this.addLayer(new DyenamicSheepWoolLayer(this));
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getEntityTexture(DyenamicSheepEntity sheep) {
      return SHEARED_SHEEP_TEXTURES;
   }
}