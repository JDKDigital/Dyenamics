package cofh.dyenamics.core.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.blocks.DyenamicCarpetBlock;
import cofh.dyenamics.common.blocks.DyenamicFlammableBlock;
import cofh.dyenamics.common.blocks.DyenamicStainedGlassBlock;
import cofh.dyenamics.common.blocks.DyenamicStainedGlassPane;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.GlazedTerracottaBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Dyenamics.MOD_ID);
	public static final Map<String, Map<String, RegistryObject<Block>>> DYED_BLOCKS = new HashMap<>();

	public static void register() {
		for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
			registerDyeBlocks(color);
		}
	}

	public synchronized static void registerDyeBlocks(DyenamicDyeColor color) {
		String colorName = color.getString();
		int light = color.getLightValue();
		MaterialColor mapColor = color.getMapColor();
		DyeColor analogue = color.getAnalogue();
		final Map<String, RegistryObject<Block>> blocks = new HashMap<>();
		DYED_BLOCKS.put(colorName, blocks);

		registerBlockAndItem(colorName, "terracotta", blocks, ItemGroup.BUILDING_BLOCKS, () -> new Block(AbstractBlock.Properties.create(Material.ROCK, mapColor).setRequiresTool().hardnessAndResistance(1.25F, 4.2F).setLightLevel((state) -> {return light;})));
		registerBlockAndItem(colorName, "glazed_terracotta", blocks, ItemGroup.DECORATIONS, () -> new GlazedTerracottaBlock(AbstractBlock.Properties.create(Material.ROCK, analogue).setRequiresTool().hardnessAndResistance(1.4F).setLightLevel((state) -> {return light;})));
		final RegistryObject<Block> concrete = registerBlockAndItem(colorName, "concrete", blocks, ItemGroup.BUILDING_BLOCKS, () -> new Block(AbstractBlock.Properties.create(Material.ROCK, analogue).setRequiresTool().hardnessAndResistance(1.8F).setLightLevel((state) -> {return light;})));
		registerBlockAndItem(colorName, "concrete_powder", blocks, ItemGroup.BUILDING_BLOCKS, () -> new ConcretePowderBlock(concrete.get(), AbstractBlock.Properties.create(Material.SAND, analogue).hardnessAndResistance(0.5F).sound(SoundType.SAND).harvestTool(ToolType.SHOVEL).setLightLevel((state) -> {return light;})));
		registerBlockAndItem(colorName, "wool", blocks, ItemGroup.BUILDING_BLOCKS, () -> new DyenamicFlammableBlock(AbstractBlock.Properties.create(Material.WOOL, mapColor).hardnessAndResistance(0.8F).sound(SoundType.CLOTH).setLightLevel((state) -> {return light;})));
		registerBlockAndItem(colorName, "carpet", blocks, ItemGroup.DECORATIONS, () -> new DyenamicCarpetBlock(color, AbstractBlock.Properties.create(Material.CARPET, mapColor).hardnessAndResistance(0.1F).sound(SoundType.CLOTH).setLightLevel((state) -> {return light;})));
		registerBlockAndItem(colorName, "stained_glass", blocks, ItemGroup.BUILDING_BLOCKS, () -> new DyenamicStainedGlassBlock(color, AbstractBlock.Properties.create(Material.GLASS, mapColor).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid().setLightLevel((state) -> {return light;})));
		registerBlockAndItem(colorName, "stained_glass_pane", blocks, ItemGroup.BUILDING_BLOCKS, () -> new DyenamicStainedGlassPane(color, AbstractBlock.Properties.create(Material.GLASS, mapColor).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid().setLightLevel((state) -> {return light;})));
	}

	public synchronized static RegistryObject<Block> registerBlockAndItem(String color, String block, Map<String, RegistryObject<Block>> blockMap, ItemGroup group, Supplier<Block> supplier) {
		String name = color + "_" + block;
		RegistryObject<Block> blockRegistryObject = BLOCKS.register(name, supplier);
		ItemInit.ITEMS.register(name, () -> new BlockItem(blockRegistryObject.get(), new Item.Properties().group(group)));
		blockMap.put(block, blockRegistryObject);
		return blockRegistryObject;
	}

}