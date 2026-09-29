package cy.jdkdigital.dyenamics.compat;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.core.init.BlockInit;
import cy.jdkdigital.dyenamics.core.init.ItemInit;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@JeiPlugin
public class DyenamicsJeiPlugin implements IModPlugin
{
    private static final ResourceLocation pluginId = ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, Dyenamics.MODID);

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return pluginId;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Ingredient shulkers = Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "shulker_boxes")));
        List<RecipeHolder<CraftingRecipe>> shulkerRecipes = Arrays.stream(DyenamicDyeColor.dyenamicValues()).map(color -> {
            var shulkerBox = BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("shulker_box");
            return new RecipeHolder<CraftingRecipe>(ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, color.getSerializedName() + "_shulker"), new ShapelessRecipe("shulker", CraftingBookCategory.MISC, new ItemStack(shulkerBox.get()), NonNullList.of(Ingredient.EMPTY, shulkers, Ingredient.of(ItemInit.DYE_ITEMS.get(color + "_dye").get()))));
        }).toList();
        registration.addRecipes(RecipeTypes.CRAFTING, shulkerRecipes);
    }
}
