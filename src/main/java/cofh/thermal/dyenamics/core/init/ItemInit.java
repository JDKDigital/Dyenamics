package cofh.thermal.dyenamics.core.init;

import cofh.thermal.dyenamics.ThermalDyenamics;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ThermalDyenamics.MOD_ID);
	
	public static final RegistryObject<Item> PEACH_DYE = ITEMS.register("peach_dye", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
	public static final RegistryObject<Item> AQUAMARINE_DYE = ITEMS.register("aquamarine_dye", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
	public static final RegistryObject<Item> FLUORESCENT_DYE = ITEMS.register("fluorescent_dye", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
	
	//Block Items
	public static final RegistryObject<BlockItem> PEACH_TERRACOTTA = ITEMS.register("peach_terracotta", () -> new BlockItem(BlockInit.PEACH_TERRACOTTA.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> AQUAMARINE_TERRACOTTA = ITEMS.register("aquamarine_terracotta", () -> new BlockItem(BlockInit.AQUAMARINE_TERRACOTTA.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> FLUORESCENT_TERRACOTTA = ITEMS.register("fluorescent_terracotta", () -> new BlockItem(BlockInit.FLUORESCENT_TERRACOTTA.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

	public static final RegistryObject<BlockItem> PEACH_GLAZED_TERRACOTTA = ITEMS.register("peach_glazed_terracotta", () -> new BlockItem(BlockInit.PEACH_GLAZED_TERRACOTTA.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
	public static final RegistryObject<BlockItem> AQUAMARINE_GLAZED_TERRACOTTA = ITEMS.register("aquamarine_glazed_terracotta", () -> new BlockItem(BlockInit.AQUAMARINE_GLAZED_TERRACOTTA.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
	public static final RegistryObject<BlockItem> FLUORESCENT_GLAZED_TERRACOTTA = ITEMS.register("fluorescent_glazed_terracotta", () -> new BlockItem(BlockInit.FLUORESCENT_GLAZED_TERRACOTTA.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
	
	public static final RegistryObject<BlockItem> PEACH_CONCRETE = ITEMS.register("peach_concrete", () -> new BlockItem(BlockInit.PEACH_CONCRETE.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> AQUAMARINE_CONCRETE = ITEMS.register("aquamarine_concrete", () -> new BlockItem(BlockInit.AQUAMARINE_CONCRETE.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> FLUORESCENT_CONCRETE = ITEMS.register("fluorescent_concrete", () -> new BlockItem(BlockInit.FLUORESCENT_CONCRETE.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

	public static final RegistryObject<BlockItem> PEACH_CONCRETE_POWDER = ITEMS.register("peach_concrete_powder", () -> new BlockItem(BlockInit.PEACH_CONCRETE_POWDER.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> AQUAMARINE_CONCRETE_POWDER = ITEMS.register("aquamarine_concrete_powder", () -> new BlockItem(BlockInit.AQUAMARINE_CONCRETE_POWDER.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> FLUORESCENT_CONCRETE_POWDER = ITEMS.register("fluorescent_concrete_powder", () -> new BlockItem(BlockInit.FLUORESCENT_CONCRETE_POWDER.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	
	public static final RegistryObject<BlockItem> PEACH_WOOL = ITEMS.register("peach_wool", () -> new BlockItem(BlockInit.PEACH_WOOL.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> AQUAMARINE_WOOL = ITEMS.register("aquamarine_wool", () -> new BlockItem(BlockInit.AQUAMARINE_WOOL.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> FLUORESCENT_WOOL = ITEMS.register("fluorescent_wool", () -> new BlockItem(BlockInit.FLUORESCENT_WOOL.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	
	public static final RegistryObject<BlockItem> PEACH_CARPET = ITEMS.register("peach_carpet", () -> new BlockItem(BlockInit.PEACH_CARPET.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
	public static final RegistryObject<BlockItem> AQUAMARINE_CARPET = ITEMS.register("aquamarine_carpet", () -> new BlockItem(BlockInit.AQUAMARINE_CARPET.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
	public static final RegistryObject<BlockItem> FLUORESCENT_CARPET = ITEMS.register("fluorescent_carpet", () -> new BlockItem(BlockInit.FLUORESCENT_CARPET.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
	
}
