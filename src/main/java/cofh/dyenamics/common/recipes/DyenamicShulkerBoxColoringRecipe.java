package cofh.dyenamics.common.recipes;

import cofh.dyenamics.common.blocks.DyenamicShulkerBoxBlock;
import cofh.dyenamics.common.items.DyenamicDyeItem;
import cofh.dyenamics.core.init.RecipeSerializerInit;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

public class DyenamicShulkerBoxColoringRecipe extends SpecialRecipe {

    public DyenamicShulkerBoxColoringRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    public boolean matches(CraftingInventory inv, World worldIn) {

        int vanillaBoxes = 0;
        int vanillaDyes = 0;
        int dyenamicBoxes = 0;
        int dyenamicDyes = 0;

        int slots = inv.getSizeInventory();
        for(int i = 0; i < slots; ++i) {
            ItemStack slotStack = inv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                Item item = slotStack.getItem();
                if (Block.getBlockFromItem(item) instanceof DyenamicShulkerBoxBlock) {
                    ++dyenamicBoxes;
                } else if (Block.getBlockFromItem(item) instanceof ShulkerBoxBlock) {
                    ++vanillaBoxes;
                } else if (item.isIn(Tags.Items.DYES)) {
                    if (item instanceof DyenamicDyeItem) {
                        ++dyenamicDyes;
                    } else {
                        ++vanillaDyes;
                    }
                } else {
                    return false;
                }

                if (vanillaDyes + dyenamicDyes > 1 || vanillaBoxes + dyenamicBoxes > 1) {
                    return false;
                }
            }
        }

        return vanillaDyes + dyenamicDyes == 1 && vanillaBoxes + dyenamicBoxes == 1 && vanillaBoxes + vanillaDyes < 2;
    }

    public ItemStack getCraftingResult(CraftingInventory inv) {

        ItemStack boxStack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;
        int slots = inv.getSizeInventory();
        for(int i = 0; i < slots; ++i) {
            ItemStack slot = inv.getStackInSlot(i);
            if (!slot.isEmpty()) {
                if (Block.getBlockFromItem(slot.getItem()) instanceof ShulkerBoxBlock) {
                    boxStack = slot;
                } else {
                    dyeStack = slot;
                }
            }
        }

        ItemStack output;
        if (dyeStack.getItem() instanceof DyenamicDyeItem) {
            output = DyenamicShulkerBoxBlock.getDyenamicColoredItemStack(DyenamicDyeColor.getColor(dyeStack));
        } else {
            output = ShulkerBoxBlock.getColoredItemStack(DyeColor.getColor(dyeStack));
        }

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
