package cofh.thermal.dyenamics.core.init;

import java.util.function.Supplier;

import cofh.thermal.dyenamics.ThermalDyenamics;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.GlazedTerracottaBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Init {
	public static final String[] dyes = {"peach", "aquamarine", "fluorescent"};
	public static final int[] light = {0, 0, 15};
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ThermalDyenamics.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ThermalDyenamics.MOD_ID);
	
	public static void register() {
		for (int i = 0; i < dyes.length; i++) {
			registerDyeAndBlocks(dyes[i], light[i]);
		}
	}
	/*
	public static void setup() {
		FireBlock fireBlock = (FireBlock) Blocks.FIRE;
		fireBlock.setFireInfo(Blocks.OAK_PLANKS, 5, 20);
	}
	*/
	public static void registerDyeAndBlocks(String dye, int light) {
		ITEMS.register(dye + "_dye", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
		registerBlockAndItem(dye + "_terracotta", () -> new Block(AbstractBlock.Properties.from(Blocks.TERRACOTTA).setLightLevel((state) -> {return light;})), ItemGroup.BUILDING_BLOCKS);
		registerBlockAndItem(dye + "_glazed_terracotta", () -> new GlazedTerracottaBlock(AbstractBlock.Properties.from(Blocks.WHITE_GLAZED_TERRACOTTA).setLightLevel((state) -> {return light;})), ItemGroup.DECORATIONS);
		RegistryObject<Block> concrete = registerBlockAndItem(dye + "_concrete", () -> new Block(AbstractBlock.Properties.from(Blocks.WHITE_CONCRETE).setLightLevel((state) -> {return light;})), ItemGroup.BUILDING_BLOCKS);
		registerBlockAndItem(dye + "_concrete_powder", () -> new ConcretePowderBlock(concrete.get(), AbstractBlock.Properties.from(Blocks.WHITE_CONCRETE_POWDER).setLightLevel((state) -> {return light;})), ItemGroup.BUILDING_BLOCKS);
		registerBlockAndItem(dye + "_wool", () -> new Block(AbstractBlock.Properties.from(Blocks.WHITE_WOOL).setLightLevel((state) -> {return light;})), ItemGroup.BUILDING_BLOCKS);
		registerBlockAndItem(dye + "_carpet", () -> new CarpetBlock(DyeColor.WHITE, AbstractBlock.Properties.from(Blocks.WHITE_CARPET).setLightLevel((state) -> {return light;})), ItemGroup.BUILDING_BLOCKS);
		
	}
	
	public static RegistryObject<Block> registerBlockAndItem(String name, Supplier<Block> supplier, ItemGroup group) {
		RegistryObject<Block> block = BLOCKS.register(name, supplier);
		ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(group)));
		return block;
	}
}
