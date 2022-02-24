package cofh.dyenamics.common.recipes;

import cofh.dyenamics.common.blocks.DyenamicShulkerBoxBlock;
import cofh.dyenamics.common.items.DyenamicDyeItem;
import cofh.dyenamics.core.init.RecipeSerializerInit;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class DyenamicShulkerBoxColoringRecipe extends SpecialRecipe {

    public DyenamicShulkerBoxColoringRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    public boolean matches(CraftingInventory inv, World worldIn) {

        int boxes = 0;
        int dyes = 0;

        int slots = inv.getSizeInventory();
        for(int i = 0; i < slots; ++i) {
            ItemStack slotStack = inv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                Item item = slotStack.getItem();
                if (Block.getBlockFromItem(item) instanceof ShulkerBoxBlock) {
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

    public ItemStack getCraftingResult(CraftingInventory inv) {

        ItemStack boxStack = ItemStack.EMPTY;
        DyenamicDyeColor color = DyenamicDyeColor.PEACH;
        int slots = inv.getSizeInventory();
        for(int i = 0; i < slots; ++i) {
            ItemStack slot = inv.getStackInSlot(i);
            if (!slot.isEmpty()) {
                if (Block.getBlockFromItem(slot.getItem()) instanceof ShulkerBoxBlock) {
                    boxStack = slot;
                } else {
                    color = DyenamicDyeColor.getColor(slot);
                }
            }
        }

        ItemStack output = DyenamicShulkerBoxBlock.getDyenamicColoredItemStack(color);
        if (boxStack.hasTag()) {
            output.setTag(boxStack.getTag().copy());
        }

        return output;
    }

    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.SHULKER.get();
    }
}
