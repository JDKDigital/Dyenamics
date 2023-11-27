package cy.jdkdigital.dyenamics.client.util;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.client.render.block.DyenamicBedRenderer;
import cy.jdkdigital.dyenamics.client.render.block.DyenamicShulkerBoxBlockEntityRenderer;
import cy.jdkdigital.dyenamics.client.render.entity.DyenamicLlamaDecorLayer;
import cy.jdkdigital.dyenamics.client.render.entity.DyenamicSheepRenderer;
import cy.jdkdigital.dyenamics.common.network.PacketHandler;
import cy.jdkdigital.dyenamics.core.init.BlockEntityInit;
import cy.jdkdigital.dyenamics.core.init.BlockInit;
import cy.jdkdigital.dyenamics.core.init.EntityInit;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Dyenamics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEvents
{
    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.AddLayers event) {
        try {
            var llamaRenderer = (MobRenderer<Llama, LlamaModel<Llama>>) event.getRenderer(EntityType.LLAMA);
            llamaRenderer.addLayer(new DyenamicLlamaDecorLayer(llamaRenderer, event.getEntityModels()));
        } catch (Exception e) {
            Dyenamics.LOGGER.warn("Llamarenderer is not a Llamarenderer");
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

            Dyenamics.BED_MATERIAL_MAP.put(color.getSerializedName(), new Material(Sheets.BED_SHEET, new ResourceLocation(Dyenamics.MOD_ID, "entity/bed/" + color.getSerializedName())));
            Dyenamics.SHULKER_MATERIAL_MAP.put(color.getSerializedName(), new Material(Sheets.SHULKER_SHEET, new ResourceLocation(Dyenamics.MOD_ID, "entity/shulker/" + color.getSerializedName())));
        }
    }
}
