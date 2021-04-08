package cofh.dyenamics;

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
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    	event.enqueueWork(EntityInit::setup);
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
    	RenderingRegistry.registerEntityRenderingHandler(EntityInit.SHEEP.get(), DyenamicSheepRenderer::new);
    }
    
    
}
