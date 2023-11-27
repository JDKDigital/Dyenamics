package cy.jdkdigital.dyenamics;

import cy.jdkdigital.dyenamics.client.util.ModEventHandler;
import cy.jdkdigital.dyenamics.common.cap.DyenamicSwagProvider;
import cy.jdkdigital.dyenamics.core.init.*;
import net.minecraft.client.resources.model.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(Dyenamics.MOD_ID)
public class Dyenamics
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "dyenamics";
    public static final Map<String, Material> BED_MATERIAL_MAP = new HashMap<>();
    public static final Map<String, Material> SHULKER_MATERIAL_MAP = new HashMap<>();
    public static final Capability<DyenamicSwagProvider> DYENAMIC_SWAG = CapabilityManager.get(new CapabilityToken<>() {});


    public Dyenamics() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BlockInit.register();
        ItemInit.register();
        EntityInit.register();

        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        EntityInit.ENTITIES.register(bus);
        BlockEntityInit.BLOCK_ENTITY_TYPES.register(bus);
        RecipeSerializerInit.RECIPE_SERIALIZERS.register(bus);

//        MinecraftForge.EVENT_BUS.addListener(ModEventHandler::onEntitySpawn);
//        MinecraftForge.EVENT_BUS.addGenericListener(ModEventHandler::attachCaps);
    }
}