package cofh.dyenamics.core.init;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.client.render.item.DyenamicBedItemStackRenderer;
import cofh.dyenamics.client.render.item.DyenamicShulkerBoxItemStackRenderer;
import cofh.dyenamics.common.blockentity.DyenamicShulkerBoxBlockEntity;
import cofh.dyenamics.common.blocks.*;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

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

		registerBlockAndItem(colorName, "terracotta", blocks, ItemGroup.BUILDING_BLOCKS, () -> new Block(AbstractBlock.Properties.create(Material.ROCK, mapColor).setRequiresTool().hardnessAndResistance(1.25F, 4.2F).setLightLevel(state -> light)));
		registerBlockAndItem(colorName, "glazed_terracotta", blocks, ItemGroup.DECORATIONS, () -> new GlazedTerracottaBlock(AbstractBlock.Properties.create(Material.ROCK, analogue).setRequiresTool().hardnessAndResistance(1.4F).setLightLevel(state -> light)));
		final RegistryObject<Block> concrete = registerBlockAndItem(colorName, "concrete", blocks, ItemGroup.BUILDING_BLOCKS, () -> new Block(AbstractBlock.Properties.create(Material.ROCK, analogue).setRequiresTool().hardnessAndResistance(1.8F).setLightLevel(state -> light)));
		registerBlockAndItem(colorName, "concrete_powder", blocks, ItemGroup.BUILDING_BLOCKS, () -> new ConcretePowderBlock(concrete.get(), AbstractBlock.Properties.create(Material.SAND, analogue).hardnessAndResistance(0.5F).sound(SoundType.SAND).harvestTool(ToolType.SHOVEL).setLightLevel(state -> light)));
		registerBlockAndItem(colorName, "wool", blocks, ItemGroup.BUILDING_BLOCKS, () -> new DyenamicFlammableBlock(AbstractBlock.Properties.create(Material.WOOL, mapColor).hardnessAndResistance(0.8F).sound(SoundType.CLOTH).setLightLevel(state -> light)));
		registerBlockAndItem(colorName, "carpet", blocks, ItemGroup.DECORATIONS, () -> new DyenamicCarpetBlock(color, AbstractBlock.Properties.create(Material.CARPET, mapColor).hardnessAndResistance(0.1F).sound(SoundType.CLOTH).setLightLevel(state -> light)));
		registerBlockAndItem(colorName, "stained_glass", blocks, ItemGroup.BUILDING_BLOCKS, () -> new DyenamicStainedGlassBlock(color, AbstractBlock.Properties.create(Material.GLASS, mapColor).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid().setAllowsSpawn((a, b, c, d) -> false).setOpaque((a, b, c) -> false).setSuffocates((a, b, c) -> false).setBlocksVision((a, b, c) -> false).setLightLevel(state -> light)));
		registerBlockAndItem(colorName, "stained_glass_pane", blocks, ItemGroup.BUILDING_BLOCKS, () -> new DyenamicStainedGlassPane(color, AbstractBlock.Properties.create(Material.GLASS, mapColor).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid().setAllowsSpawn((a, b, c, d) -> false).setOpaque((a, b, c) -> false).setSuffocates((a, b, c) -> false).setBlocksVision((a, b, c) -> false).setLightLevel(state -> light)));
		registerBlockAndItem(colorName, "bed", blocks, ItemGroup.DECORATIONS, () -> new DyenamicBedBlock(color, AbstractBlock.Properties.create(Material.WOOL, (state) -> state.get(BedBlock.PART) == BedPart.FOOT ? mapColor : MaterialColor.WOOL).sound(SoundType.WOOD).hardnessAndResistance(0.2F).notSolid()));

		AbstractBlock.IPositionPredicate shulkerPositionPredicate = (state, reader, pos) -> {
			TileEntity tileentity = reader.getTileEntity(pos);
			if ((tileentity instanceof DyenamicShulkerBoxBlockEntity)) {
				DyenamicShulkerBoxBlockEntity shulkerBoxBlockEntity = (DyenamicShulkerBoxBlockEntity)tileentity;
				return shulkerBoxBlockEntity.func_235676_l_();
			}
			return true;
		};
		registerBlockAndItem(colorName, "shulker_box", blocks, ItemGroup.DECORATIONS, () -> new DyenamicShulkerBoxBlock(color, AbstractBlock.Properties.create(Material.SHULKER, mapColor).hardnessAndResistance(2.0F).variableOpacity().notSolid().setSuffocates(shulkerPositionPredicate).setBlocksVision(shulkerPositionPredicate)));
	}

	public synchronized static RegistryObject<Block> registerBlockAndItem(String color, String nameSuffix, Map<String, RegistryObject<Block>> blockMap, ItemGroup group, Supplier<Block> supplier) {
		String name = color + "_" + nameSuffix;
		RegistryObject<Block> block = BLOCKS.register(name, supplier);

		if (name.endsWith("_bed")) {
			ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(group).setISTER(() -> DyenamicBedItemStackRenderer::new)));
		} else if (name.endsWith("_shulker_box")) {
			ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(group).setISTER(() -> DyenamicShulkerBoxItemStackRenderer::new)));
		} else {
			ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(group)));
		}
		blockMap.put(nameSuffix, block);
		return block;
	}
}