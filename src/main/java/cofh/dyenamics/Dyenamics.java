package cofh.dyenamics;

import cofh.dyenamics.common.blocks.DyenamicStainedGlassBlock;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cofh.dyenamics.client.render.entity.DyenamicSheepRenderer;
import cofh.dyenamics.core.init.BlockInit;
import cofh.dyenamics.core.init.EntityInit;
import cofh.dyenamics.core.init.ItemInit;

import javax.swing.*;
import java.util.List;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Dyenamics.MOD_ID)
public class Dyenamics
{
    static {
        BlockInit.register();
        ItemInit.register();
        EntityInit.register();
    }

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "dyenamics";

    public Dyenamics() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        EntityInit.ENTITIES.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(EntityInit::setup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.SHEEP.get(), DyenamicSheepRenderer::new);
        RenderTypeLookup.setRenderLayer(BlockInit.DYED_BLOCKS.get("peach").get("stained_glass").get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockInit.DYED_BLOCKS.get("aquamarine").get("stained_glass").get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockInit.DYED_BLOCKS.get("fluorescent").get("stained_glass").get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockInit.DYED_BLOCKS.get("mint").get("stained_glass").get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockInit.DYED_BLOCKS.get("maroon").get("stained_glass").get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockInit.DYED_BLOCKS.get("bubblegum").get("stained_glass").get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockInit.DYED_BLOCKS.get("lavender").get("stained_glass").get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockInit.DYED_BLOCKS.get("persimmon").get("stained_glass").get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockInit.DYED_BLOCKS.get("cherenkov").get("stained_glass").get(), RenderType.getTranslucent());
    }
}