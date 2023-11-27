package cy.jdkdigital.dyenamics.core.init;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.common.items.DyenamicDyeItem;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class ItemInit
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Dyenamics.MOD_ID);
    public static final Map<String, RegistryObject<Item>> DYE_ITEMS = new HashMap<>();

    public synchronized static void register() {
        for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
            String colorName = color.getSerializedName();
            DYE_ITEMS.put(colorName + "_dye", ITEMS.register(colorName + "_dye", () -> new DyenamicDyeItem(color, new Item.Properties())));
        }
    }
}
