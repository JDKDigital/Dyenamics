package cofh.dyenamics.core.init;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.items.DyenamicDyeItem;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Dyenamics.MOD_ID);
	public static final Map<String, RegistryObject<Item>> DYE_ITEMS = new HashMap<>();
	
	public synchronized static void register() {
		for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
			String colorName = color.getString();
			DYE_ITEMS.put(colorName + "_dye", ITEMS.register(colorName + "_dye", () -> new DyenamicDyeItem(color, new Item.Properties().group(ItemGroup.MISC))));
		}
	}
	
}
