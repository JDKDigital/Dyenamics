package cofh.dyenamics.core.init;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.recipes.DyenamicShulkerBoxColoringRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerInit {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Dyenamics.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<?>> SHULKER = RECIPE_SERIALIZERS.register("shulker_box_coloring", () -> new SpecialRecipeSerializer<>(DyenamicShulkerBoxColoringRecipe::new));
}
