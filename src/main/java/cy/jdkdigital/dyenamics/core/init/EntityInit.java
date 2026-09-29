package cy.jdkdigital.dyenamics.core.init;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.common.entity.DyenamicSheep;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class EntityInit
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Dyenamics.MODID);
    public static final DeferredHolder<EntityType<?>, EntityType<DyenamicSheep>> SHEEP = ENTITIES.register("sheep", () -> EntityType.Builder.of(DyenamicSheep::new, MobCategory.CREATURE).sized(0.9F, 1.3F).eyeHeight(1.235F).passengerAttachments(1.2375F).clientTrackingRange(10).build("sheep"));
    public static final Map<String, ResourceKey<LootTable>> SHEEP_LOOT = new HashMap<>();

    public static void register() {
        for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
            SHEEP_LOOT.put(color.getTranslationKey(), ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "entities/sheep/" + color.getTranslationKey())));
        }
    }
}
