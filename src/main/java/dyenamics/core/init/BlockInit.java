package dyenamics.core.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import dyenamics.ThermalDyenamics;
import dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.GlazedTerracottaBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ThermalDyenamics.MOD_ID);
	public static final Map<String, Map<String, RegistryObject<Block>>> DYED_BLOCKS = new HashMap<>();
	
	public static void register() {
		for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
			registerDyeBlocks(color.getString(), color.getLightValue());
		}
	}
	/* wtf is going on
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
		ItemInit.ITEMS.register(name, () -> new BlockItem(blockRegistryObject.get(), new Item.Properties().group(group)));
		blockMap.put(block, blockRegistryObject);
		return blockRegistryObject;
	}
	
}
