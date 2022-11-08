package cofh.dyenamics.core.init;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.recipes.DyenamicShulkerBoxColoringRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSerializerInit
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Dyenamics.MOD_ID);

    public static final RegistryObject<SimpleRecipeSerializer<DyenamicShulkerBoxColoringRecipe>> SHULKER = RECIPE_SERIALIZERS.register("shulker_box_coloring", () -> new SimpleRecipeSerializer<>(DyenamicShulkerBoxColoringRecipe::new));
}
