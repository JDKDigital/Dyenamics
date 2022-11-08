package cofh.dyenamics;

import cofh.dyenamics.common.entities.DyenamicSheep;
import cofh.dyenamics.core.init.*;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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

    public Dyenamics() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::commonSetup);
        bus.addListener(this::onEntityAttributeCreate);

        BlockInit.register();
        ItemInit.register();
        EntityInit.register();

        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        EntityInit.ENTITIES.register(bus);
        BlockEntityInit.BLOCK_ENTITY_TYPES.register(bus);
        RecipeSerializerInit.RECIPE_SERIALIZERS.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(EntityInit::setup);
    }

    private void onEntityAttributeCreate(EntityAttributeCreationEvent event) {
        LOGGER.info("EntityAttributeCreationEvent sheep");
        event.put(EntityInit.SHEEP.get(), Sheep.createAttributes().build());
    }
}