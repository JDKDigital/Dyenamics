package cofh.dyenamics.core.init;

import java.util.HashMap;
import java.util.Map;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.entities.DyenamicSheepEntity;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Dyenamics.MOD_ID);
	public static final RegistryObject<EntityType<DyenamicSheepEntity>> SHEEP = ENTITIES.register("sheep", () -> EntityType.Builder.create(DyenamicSheepEntity::new, EntityClassification.CREATURE).build("sheep"));
	public static final Map<String, ResourceLocation> SHEEP_LOOT = new HashMap<>();

	

	public static void register() {
		for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
			SHEEP_LOOT.put(color.getTranslationKey(), new ResourceLocation(Dyenamics.MOD_ID, "entities/sheep/" + color.getTranslationKey()));
		}
	}
	
	public static void setup() {
		GlobalEntityTypeAttributes.put(SHEEP.get(), DyenamicSheepEntity.registerAttributes().create());
		EntitySpawnPlacementRegistry.register(SHEEP.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (animal, worldIn, reason, pos, random) -> false);
	}
}
