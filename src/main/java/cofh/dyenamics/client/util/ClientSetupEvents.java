package cofh.dyenamics.client.util;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.client.render.block.DyenamicBedRenderer;
import cofh.dyenamics.client.render.block.DyenamicShulkerBoxBlockEntityRenderer;
import cofh.dyenamics.client.render.entity.DyenamicSheepRenderer;
import cofh.dyenamics.core.init.BlockEntityInit;
import cofh.dyenamics.core.init.BlockInit;
import cofh.dyenamics.core.init.EntityInit;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static cofh.dyenamics.Dyenamics.BED_MATERIAL_MAP;
import static cofh.dyenamics.Dyenamics.SHULKER_MATERIAL_MAP;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Dyenamics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEvents
{
    @SubscribeEvent
    public static void textureStitch(final TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(Sheets.BED_SHEET)) {
            for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
                ResourceLocation rLoc = new ResourceLocation(Dyenamics.MOD_ID, "entity/bed/" + color.getSerializedName());
                event.addSprite(rLoc);
                BED_MATERIAL_MAP.put(color.getSerializedName(), new Material(Sheets.BED_SHEET, rLoc));
            }
        } else if (event.getAtlas().location().equals(Sheets.SHULKER_SHEET)) {
            for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
                ResourceLocation rLoc = new ResourceLocation(Dyenamics.MOD_ID, "entity/shulker/" + color.getSerializedName());
                event.addSprite(rLoc);
                SHULKER_MATERIAL_MAP.put(color.getSerializedName(), new Material(Sheets.SHULKER_SHEET, rLoc));
            }
        }
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.BED.get(), DyenamicBedRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.SHULKER_BOX.get(), DyenamicShulkerBoxBlockEntityRenderer::new);
        event.registerEntityRenderer(EntityInit.SHEEP.get(), DyenamicSheepRenderer::new);
    }

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
            ItemBlockRenderTypes.setRenderLayer(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass").get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass_pane").get(), RenderType.translucent());
        }
    }
}
