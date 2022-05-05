package cofh.dyenamics.client.util;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cofh.dyenamics.Dyenamics.BED_MATERIAL_MAP;
import static cofh.dyenamics.Dyenamics.SHULKER_MATERIAL_MAP;

@Mod.EventBusSubscriber (value = Dist.CLIENT, modid = Dyenamics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEvents {

    @SubscribeEvent
    public static void textureStitch(final TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(Atlases.BED_ATLAS)) {
            for (DyenamicDyeColor color: DyenamicDyeColor.dyenamicValues()) {
                ResourceLocation rLoc = new ResourceLocation(Dyenamics.MOD_ID, "entity/bed/" + color.getString());
                event.addSprite(rLoc);
                BED_MATERIAL_MAP.put(color.getString(), new RenderMaterial(Atlases.BED_ATLAS, rLoc));
            }
        } else if (event.getMap().getTextureLocation().equals(Atlases.SHULKER_BOX_ATLAS)) {
            for (DyenamicDyeColor color: DyenamicDyeColor.dyenamicValues()) {
                ResourceLocation rLoc = new ResourceLocation(Dyenamics.MOD_ID, "entity/shulker/" + color.getString());
                event.addSprite(rLoc);
                SHULKER_MATERIAL_MAP.put(color.getString(), new RenderMaterial(Atlases.SHULKER_BOX_ATLAS, rLoc));
            }
        }
    }
}
