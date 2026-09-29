package cy.jdkdigital.dyenamics;

import com.mojang.serialization.MapCodec;
import cy.jdkdigital.dyenamics.core.init.*;
import net.minecraft.client.resources.model.Material;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(Dyenamics.MODID)
public class Dyenamics
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "dyenamics";
    public static final Map<String, Material> BED_MATERIAL_MAP = new HashMap<>();
    public static final Map<String, Material> SHULKER_MATERIAL_MAP = new HashMap<>();

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, MODID);

    public Dyenamics(IEventBus modEventBus, ModContainer modContainer) {
        BlockInit.register();
        ItemInit.register();
        EntityInit.register();

        BlockInit.BLOCKS.register(modEventBus);
        ItemInit.ITEMS.register(modEventBus);
        EntityInit.ENTITIES.register(modEventBus);
        BlockEntityInit.BLOCK_ENTITY_TYPES.register(modEventBus);
        RecipeSerializerInit.RECIPE_SERIALIZERS.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        CONDITION_CODECS.register(modEventBus);

        // Coral blocks
    }
}