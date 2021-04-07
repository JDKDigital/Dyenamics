package cofh.thermal.dyenamics.core.init;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import cofh.thermal.dyenamics.ThermalDyenamics;
import cofh.thermal.dyenamics.common.entities.DyenamicSheepEntity;
import cofh.thermal.dyenamics.common.items.DyenamicDyeItem;
import cofh.thermal.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.GlazedTerracottaBlock;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;

public class Init {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ThermalDyenamics.MOD_ID);
	public static final Map<String, Map<String, RegistryObject<Block>>> DYED_BLOCKS = new HashMap<>();
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ThermalDyenamics.MOD_ID);
	public static final Map<String, RegistryObject<Item>> DYE_ITEMS = new HashMap<>();
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ThermalDyenamics.MOD_ID);
	public static final RegistryObject<EntityType<DyenamicSheepEntity>> SHEEP = ENTITIES.register("sheep", () -> EntityType.Builder.create(DyenamicSheepEntity::new, EntityClassification.CREATURE).build("sheep"));
	
	public static void register() {
		registerDyes();
		registerEntities();
	}
	
	public synchronized static void registerEntities() {
		System.out.println("SEE THAT!! " + SHEEP);
		System.out.println("SEE THIS!! " + SHEEP.isPresent());
		//ITEMS.register("dyenamic_sheep_spawn_egg", () -> new SpawnEggItem(SHEEP.get(), 255, 255, new Item.Properties().group(ItemGroup.MISC)));
		
	}
	
	public synchronized static void registerDyes() {
		for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
			String colorName = color.getString();
			DYE_ITEMS.put(colorName + "_dye", ITEMS.register(colorName + "_dye", () -> new DyenamicDyeItem(color, new Item.Properties().group(ItemGroup.MISC))));
			registerDyeBlocks(colorName, color.getLightValue());
		}
	}
	/*
	public static void setup() {
		FireBlock fireBlock = (FireBlock) Blocks.FIRE;
		fireBlock.setFireInfo(Blocks.OAK_PLANKS, 5, 20);
	}
	*/
	public synchronized static void registerDyeBlocks(String color, int light) {
		final Map<String, RegistryObject<Block>> blocks = new HashMap<>();
		DYED_BLOCKS.put(color, blocks);
		registerBlockAndItem(color, "terracotta", blocks, ItemGroup.BUILDING_BLOCKS, () -> new Block(AbstractBlock.Properties.from(Blocks.TERRACOTTA).setLightLevel((state) -> {return light;})));
		registerBlockAndItem(color, "glazed_terracotta", blocks, ItemGroup.DECORATIONS, () -> new GlazedTerracottaBlock(AbstractBlock.Properties.from(Blocks.WHITE_GLAZED_TERRACOTTA).setLightLevel((state) -> {return light;})));
		final RegistryObject<Block> concrete = registerBlockAndItem(color, "concrete", blocks, ItemGroup.BUILDING_BLOCKS, () -> new Block(AbstractBlock.Properties.from(Blocks.WHITE_CONCRETE).setLightLevel((state) -> {return light;})));
		registerBlockAndItem(color, "concrete_powder", blocks, ItemGroup.BUILDING_BLOCKS, () -> new ConcretePowderBlock(concrete.get(), AbstractBlock.Properties.from(Blocks.WHITE_CONCRETE_POWDER).setLightLevel((state) -> {return light;})));
		registerBlockAndItem(color, "wool", blocks, ItemGroup.BUILDING_BLOCKS, () -> new Block(AbstractBlock.Properties.from(Blocks.WHITE_WOOL).setLightLevel((state) -> {return light;})));
		registerBlockAndItem(color, "carpet", blocks, ItemGroup.DECORATIONS, () -> new CarpetBlock(DyeColor.WHITE, AbstractBlock.Properties.from(Blocks.WHITE_CARPET).setLightLevel((state) -> {return light;})));
	}
	
	public synchronized static RegistryObject<Block> registerBlockAndItem(String color, String block, Map<String, RegistryObject<Block>> blockMap, ItemGroup group, Supplier<Block> supplier) {
		String name = color + "_" + block;
		RegistryObject<Block> blockRegistryObject = BLOCKS.register(name, supplier);
		ITEMS.register(name, () -> new BlockItem(blockRegistryObject.get(), new Item.Properties().group(group)));
		blockMap.put(block, blockRegistryObject);
		return blockRegistryObject;
	}
	
	public static void setup() {
		GlobalEntityTypeAttributes.put(SHEEP.get(), DyenamicSheepEntity.registerAttributes().create());
		EntitySpawnPlacementRegistry.register(SHEEP.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (animal, worldIn, reason, pos, random) -> false);
	}
}
