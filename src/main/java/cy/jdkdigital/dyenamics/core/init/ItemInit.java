package cy.jdkdigital.dyenamics.core.init;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.common.item.DyenamicDyeItem;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class ItemInit
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Dyenamics.MODID);
    public static final Map<String, DeferredHolder<Item, Item>> DYE_ITEMS = new HashMap<>();

    public synchronized static void register() {
        for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
            String colorName = color.getSerializedName();
            DYE_ITEMS.put(colorName + "_dye", ITEMS.register(colorName + "_dye", () -> new DyenamicDyeItem(color, new Item.Properties())));
        }
    }
}
