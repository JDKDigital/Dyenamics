package cy.jdkdigital.dyenamics.core.init;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.common.entities.DyenamicSheep;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class EntityInit
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Dyenamics.MOD_ID);
    public static final RegistryObject<EntityType<DyenamicSheep>> SHEEP = ENTITIES.register("sheep", () -> EntityType.Builder.of(DyenamicSheep::new, MobCategory.CREATURE).build("sheep"));
    public static final Map<String, ResourceLocation> SHEEP_LOOT = new HashMap<>();

    public static void register() {
        for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
            SHEEP_LOOT.put(color.getTranslationKey(), new ResourceLocation(Dyenamics.MOD_ID, "entities/sheep/" + color.getTranslationKey()));
        }
    }

    public static void setup() {
        SpawnPlacements.register(SHEEP.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (animal, worldIn, reason, pos, random) -> false);
    }
}
