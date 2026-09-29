package cy.jdkdigital.dyenamics.core.init;

import com.mojang.serialization.MapCodec;
import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.common.recipe.DyenamicArmorColoringRecipe;
import cy.jdkdigital.dyenamics.common.recipe.DyenamicFireworkStarRecipe;
import cy.jdkdigital.dyenamics.common.recipe.DyenamicShulkerBoxColoringRecipe;
import cy.jdkdigital.dyenamics.common.recipe.condition.LazyCondition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RecipeSerializerInit
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Dyenamics.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DyenamicShulkerBoxColoringRecipe>> SHULKER = RECIPE_SERIALIZERS.register("shulker_box_coloring", () -> new SimpleCraftingRecipeSerializer<>(DyenamicShulkerBoxColoringRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DyenamicArmorColoringRecipe>> ARMOR = RECIPE_SERIALIZERS.register("armor_coloring", () -> new SimpleCraftingRecipeSerializer<>(DyenamicArmorColoringRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DyenamicFireworkStarRecipe>> FIREWORK_STAR = RECIPE_SERIALIZERS.register("firework_star", () -> new SimpleCraftingRecipeSerializer<>(DyenamicFireworkStarRecipe::new));

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<LazyCondition>> LAZY_CONDITION = Dyenamics.CONDITION_CODECS.register("lazy", () -> LazyCondition.CODEC);
}
