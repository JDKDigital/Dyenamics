package cy.jdkdigital.dyenamics.common.recipes;

import cy.jdkdigital.dyenamics.common.blocks.DyenamicShulkerBoxBlock;
import cy.jdkdigital.dyenamics.common.items.DyenamicDyeItem;
import cy.jdkdigital.dyenamics.core.init.RecipeSerializerInit;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class DyenamicShulkerBoxColoringRecipe extends CustomRecipe
{
    public DyenamicShulkerBoxColoringRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    public boolean matches(CraftingContainer inv, Level pLevel) {
        int boxes = 0;
        int dyes = 0;

        int slots = inv.getContainerSize();
        for (int i = 0; i < slots; ++i) {
            ItemStack slotStack = inv.getItem(i);
            if (!slotStack.isEmpty()) {
                Item item = slotStack.getItem();
                if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock) {
                    if (boxes >= 1) {
                        return false;
                    }
                    ++boxes;
                } else if (item instanceof DyenamicDyeItem) {
                    if (dyes >= 1) {
                        return false;
                    }
                    ++dyes;
                } else {
                    return false;
                }
            }
        }

        return dyes == 1 && boxes == 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess level) {
        ItemStack boxStack = ItemStack.EMPTY;
        DyenamicDyeColor color = DyenamicDyeColor.PEACH;
        int slots = inv.getContainerSize();
        for (int i = 0; i < slots; ++i) {
            ItemStack slotStack = inv.getItem(i);
            if (!slotStack.isEmpty()) {
                Item item = slotStack.getItem();
                if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock) {
                    boxStack = slotStack;
                } else {
                    color = DyenamicDyeColor.getColor(slotStack);
                }
            }
        }

        ItemStack output = DyenamicShulkerBoxBlock.getDyenamicColoredItemStack(color);
        if (boxStack.hasTag()) {
            output.setTag(boxStack.getTag().copy());
        }

        return output;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.SHULKER.get();
    }
}
