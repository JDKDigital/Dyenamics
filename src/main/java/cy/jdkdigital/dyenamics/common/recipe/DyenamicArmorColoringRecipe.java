package cy.jdkdigital.dyenamics.common.recipe;

import com.google.common.collect.Lists;
import cy.jdkdigital.dyenamics.common.item.DyenamicDyeItem;
import cy.jdkdigital.dyenamics.core.init.RecipeSerializerInit;
import cy.jdkdigital.dyenamics.core.util.ColorUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;

public class DyenamicArmorColoringRecipe extends CustomRecipe
{
    public DyenamicArmorColoringRecipe(CraftingBookCategory category) {
        super(category);
    }

    public boolean matches(CraftingInput pInv, Level pLevel) {
        ItemStack armorStack = ItemStack.EMPTY;
        List<ItemStack> list = Lists.newArrayList();

        for(int i = 0; i < pInv.size(); ++i) {
            ItemStack stack = pInv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(ItemTags.DYEABLE)) {
                    if (!armorStack.isEmpty()) {
                        return false;
                    }

                    armorStack = stack;
                } else if (stack.getItem() instanceof DyeItem || stack.getItem() instanceof DyenamicDyeItem) {
                    list.add(stack);
                } else {
                    return false;
                }
            }
        }

        return !armorStack.isEmpty() && !list.isEmpty();
    }

    public ItemStack assemble(CraftingInput pContainer, HolderLookup.Provider pRegistryAccess) {
        List<Item> list = Lists.newArrayList();
        ItemStack armorItem = ItemStack.EMPTY;

        for(int i = 0; i < pContainer.size(); ++i) {
            ItemStack stack = pContainer.getItem(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                if (stack.is(ItemTags.DYEABLE)) {
                    if (!armorItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    armorItem = stack.copy();
                } else if (item instanceof DyeItem || item instanceof DyenamicDyeItem) {
                    list.add(item);
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        return !armorItem.isEmpty() && !list.isEmpty() ? ColorUtil.dyeArmor(armorItem, list) : ItemStack.EMPTY;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.ARMOR.get();
    }
}
