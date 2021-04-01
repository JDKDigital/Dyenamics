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
	
}
