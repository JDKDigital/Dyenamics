package cy.jdkdigital.dyenamics.data;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.core.init.BlockInit;
import cy.jdkdigital.dyenamics.core.init.ItemInit;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider implements IConditionBuilder
{
    public RecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(pOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        for (DyenamicDyeColor color: DyenamicDyeColor.dyenamicValues()) {
            var dyeTag = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyes/" + color.getSerializedName()));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("candle").get(), 1)
                    .group("dyed_candle")
                    .unlockedBy(getHasName(Items.CANDLE), has(Items.CANDLE))
                    .unlockedBy("has_dye", has(dyeTag))
                    .requires(Items.CANDLE).requires(dyeTag)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, color.getSerializedName() + "_candle"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("carpet").get(), 3)
                    .group("carpet")
                    .unlockedBy("has_wool", has(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get()))
                    .pattern("##")
                    .define('#', Ingredient.of(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get()))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, color.getSerializedName() + "_carpet"));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("carpet").get(), 1)
                    .group("carpet")
                    .unlockedBy(getHasName(Items.WHITE_CARPET), has(ItemTags.WOOL_CARPETS))
                    .unlockedBy("has_dye", has(dyeTag))
                    .requires(ItemTags.WOOL_CARPETS).requires(dyeTag)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "dye_" + color.getSerializedName() + "_carpet"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("banner").get(), 1)
                    .group("banner")
                    .unlockedBy("has_wool", has(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get()))
                    .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                    .pattern("###").pattern("###").pattern(" | ")
                    .define('#', Ingredient.of(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get()))
                    .define('|', Ingredient.of(Tags.Items.RODS_WOODEN))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "banner/" + color.getSerializedName() + "_banner"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("bed").get(), 1)
                    .group("bed")
                    .unlockedBy("has_wool", has(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get()))
                    .unlockedBy("has_plank", has(ItemTags.PLANKS))
                    .pattern("###").pattern("XXX")
                    .define('#', Ingredient.of(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get()))
                    .define('X', Ingredient.of(ItemTags.PLANKS))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "bed/" + color.getSerializedName() + "_bed"));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("bed").get(), 1)
                    .group("dyed_bed")
                    .unlockedBy(getHasName(Items.WHITE_BED), has(ItemTags.BEDS))
                    .unlockedBy("has_dye", has(dyeTag))
                    .requires(ItemTags.BEDS).requires(dyeTag)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "bed/" + color.getSerializedName() + "_bed_frm_white_bed"));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("concrete_powder").get(), 8)
                    .group("concrete_powder")
                    .unlockedBy(getHasName(Items.SAND), has(Items.SAND))
                    .unlockedBy(getHasName(Items.GRAVEL), has(Items.GRAVEL))
                    .unlockedBy("has_dye", has(dyeTag))
                    .requires(dyeTag).requires(Items.SAND).requires(Items.SAND).requires(Items.SAND).requires(Items.SAND)
                    .requires(Items.GRAVEL).requires(Items.GRAVEL).requires(Items.GRAVEL).requires(Items.GRAVEL)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, color.getSerializedName() + "_concrete_powder"));

            SimpleCookingRecipeBuilder.smelting(Ingredient.of(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("terracotta").get()), RecipeCategory.MISC, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("glazed_terracotta").get(), 0.1F, 200)
                    .unlockedBy("has_terracotta", has(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("terracotta").get()))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, color.getSerializedName() + "_glazed_terracotta"));

            var stainedGlass = BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass").get();
            var stainedGlassPane = BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass_pane").get();
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, stainedGlass, 8)
                    .group("stained_glass")
                    .unlockedBy(getHasName(Items.GLASS), has(Items.GLASS))
                    .unlockedBy("has_dye", has(dyeTag))
                    .pattern("###").pattern("#D#").pattern("###")
                    .define('#', Ingredient.of(Items.GLASS))
                    .define('D', Ingredient.of(dyeTag))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, color.getSerializedName() + "_stained_glass"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, stainedGlassPane, 16)
                    .group("stained_glass")
                    .unlockedBy(getHasName(stainedGlass), has(stainedGlass))
                    .pattern("###").pattern("###")
                    .define('#', Ingredient.of(stainedGlass))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, color.getSerializedName() + "_stained_glass_pane"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, stainedGlassPane, 8)
                    .group("stained_glass_pane")
                    .unlockedBy(getHasName(Items.GLASS_PANE), has(Items.GLASS_PANE))
                    .unlockedBy("has_dye", has(dyeTag))
                    .pattern("###").pattern("#D#").pattern("###")
                    .define('#', Ingredient.of(Items.GLASS_PANE))
                    .define('D', Ingredient.of(dyeTag))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, color.getSerializedName() + "_stained_glass_pane_from_glass_pane"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("terracotta").get(), 8)
                    .group("stained_terracotta")
                    .unlockedBy(getHasName(Items.TERRACOTTA), has(Items.TERRACOTTA))
                    .unlockedBy("has_dye", has(dyeTag))
                    .pattern("###").pattern("#D#").pattern("###")
                    .define('#', Ingredient.of(Items.TERRACOTTA))
                    .define('D', Ingredient.of(dyeTag))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, color.getSerializedName() + "_terracotta"));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get(), 1)
                    .group("wool")
                    .unlockedBy(getHasName(Items.WHITE_WOOL), has(ItemTags.WOOL))
                    .unlockedBy("has_dye", has(dyeTag))
                    .requires(dyeTag).requires(ItemTags.WOOL)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, color.getSerializedName() + "_wool"));

            // MA compat

            // Thermal compat
//            SingleConditionalRecipe.builder().addCondition(new ModLoadedCondition("thermal")).setRecipe(
//                            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("rockwool").get(), 1)
//                                    .group("rockwool")
//                                    .unlockedBy("has_rockwool", has(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal:white_rockwool"))))
//                                    .unlockedBy("has_dye", has(dyeTag))
//                                    .requires(BuiltInRegistries.ITEMS.getValue(new ResourceLocation("thermal:white_rockwool"))).requires(dyeTag)::save)
//                    .build(consumer, ResourceLocation.fromNamespaceAndPath(Dyenamics.MOD_ID, "rockwool/" + color.getSerializedName() + "_rockwool"));
//            SingleConditionalRecipe.builder().addCondition(new ModLoadedCondition("thermal")).setRecipe(
//                            ThermalCentrifugeRecipeBuilder.direct(Ingredient.of(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("rockwool").get()), List.of(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal:white_rockwool"))), new ItemStack(ItemInit.DYE_ITEMS.get(color.getSerializedName() + "_dye").get())), 1600)
//                                    .group("rockwool")
//                                    .unlockedBy("has_rockwool", has(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("rockwool").get()))
//                                    ::save)
//                    .build(consumer, ResourceLocation.fromNamespaceAndPath(Dyenamics.MOD_ID, "thermal/centrifuge/" + color.getSerializedName() + "_rockwool"));

//            var slag = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal:slag"));
//            SingleConditionalRecipe.builder().addCondition(new ModLoadedCondition("thermal")).setRecipe(
//                            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("concrete_powder").get(), 8)
//                                    .group("concrete_powder")
//                                    .unlockedBy(getHasName(Items.SAND), has(Items.SAND))
//                                    .unlockedBy(getHasName(slag), has(slag))
//                                    .unlockedBy("has_dye", has(dyeTag))
//                                    .requires(dyeTag).requires(Items.SAND).requires(Items.SAND).requires(Items.SAND).requires(Items.SAND)
//                                    .requires(slag).requires(slag).requires(slag).requires(slag)
//                                    ::save)
//                    .build(consumer, ResourceLocation.fromNamespaceAndPath(Dyenamics.MOD_ID, color.getSerializedName() + "_concrete_powder_with_slag"));
        }

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("aquamarine_dye").get(), 2)
                .group("aquamarine_dye")
                .unlockedBy(getHasName(Items.CYAN_DYE), has(Tags.Items.DYES_CYAN))
                .unlockedBy(getHasName(Items.BLUE_DYE), has(Tags.Items.DYES_BLUE))
                .requires(Tags.Items.DYES_CYAN).requires(Tags.Items.DYES_BLUE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "aquamarine_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("bubblegum_dye").get(), 2)
                .group("bubblegum_dye")
                .unlockedBy(getHasName(Items.PINK_DYE), has(Tags.Items.DYES_PINK))
                .unlockedBy(getHasName(Items.MAGENTA_DYE), has(Tags.Items.DYES_MAGENTA))
                .requires(Tags.Items.DYES_PINK).requires(Tags.Items.DYES_MAGENTA)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "bubblegum_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("cherenkov_dye").get(), 3)
                .group("cherenkov_dye")
                .unlockedBy(getHasName(Items.LIGHT_BLUE_DYE), has(Tags.Items.DYES_LIGHT_BLUE))
                .unlockedBy(getHasName(Items.BLUE_DYE), has(Tags.Items.DYES_BLUE))
                .unlockedBy(getHasName(Items.GLOWSTONE), has(Tags.Items.DUSTS_GLOWSTONE))
                .requires(Tags.Items.DYES_LIGHT_BLUE).requires(Tags.Items.DYES_BLUE).requires(Tags.Items.DUSTS_GLOWSTONE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "cherenkov_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("fluorescent_dye").get(), 3)
                .group("fluorescent_dye")
                .unlockedBy(getHasName(Items.YELLOW_DYE), has(Tags.Items.DYES_YELLOW))
                .unlockedBy(getHasName(Items.WHITE_DYE), has(Tags.Items.DYES_WHITE))
                .unlockedBy(getHasName(Items.GLOWSTONE), has(Tags.Items.DUSTS_GLOWSTONE))
                .requires(Tags.Items.DYES_YELLOW).requires(Tags.Items.DYES_WHITE).requires(Tags.Items.DUSTS_GLOWSTONE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "fluorescent_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("lavender_dye").get(), 2)
                .group("lavender_dye")
                .unlockedBy(getHasName(Items.PURPLE_DYE), has(Tags.Items.DYES_PURPLE))
                .unlockedBy(getHasName(Items.WHITE_DYE), has(Tags.Items.DYES_WHITE))
                .requires(Tags.Items.DYES_PURPLE).requires(Tags.Items.DYES_WHITE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "lavender_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("maroon_dye").get(), 2)
                .group("maroon_dye")
                .unlockedBy(getHasName(Items.RED_DYE), has(Tags.Items.DYES_RED))
                .unlockedBy(getHasName(Items.BLACK_DYE), has(Tags.Items.DYES_BLACK))
                .requires(Tags.Items.DYES_RED).requires(Tags.Items.DYES_BLACK)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "maroon_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("mint_dye").get(), 3)
                .group("mint_dye")
                .unlockedBy(getHasName(Items.GREEN_DYE), has(Tags.Items.DYES_GREEN))
                .unlockedBy(getHasName(Items.WHITE_DYE), has(Tags.Items.DYES_WHITE))
                .requires(Tags.Items.DYES_GREEN).requires(Tags.Items.DYES_WHITE).requires(Tags.Items.DYES_WHITE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "mint_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("peach_dye").get(), 2)
                .group("peach_dye")
                .unlockedBy(getHasName(Items.ORANGE_DYE), has(Tags.Items.DYES_ORANGE))
                .unlockedBy(getHasName(Items.WHITE_DYE), has(Tags.Items.DYES_WHITE))
                .requires(Tags.Items.DYES_ORANGE).requires(Tags.Items.DYES_WHITE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "peach_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("persimmon_dye").get(), 2)
                .group("persimmon_dye")
                .unlockedBy(getHasName(Items.ORANGE_DYE), has(Tags.Items.DYES_ORANGE))
                .unlockedBy(getHasName(Items.RED_DYE), has(Tags.Items.DYES_RED))
                .requires(Tags.Items.DYES_ORANGE).requires(Tags.Items.DYES_RED)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "persimmon_dye"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("amber_dye").get(), 2)
                .group("amber_dye")
                .unlockedBy(getHasName(Items.ORANGE_DYE), has(Tags.Items.DYES_ORANGE))
                .unlockedBy("has_persimmon_dye", has(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyes/persimmon"))))
                .requires(Tags.Items.DYES_ORANGE).requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyes/persimmon")))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "amber_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("honey_dye").get(), 2)
                .group("honey_dye")
                .unlockedBy(getHasName(Items.ORANGE_DYE), has(Tags.Items.DYES_ORANGE))
                .unlockedBy(getHasName(Items.YELLOW_DYE), has(Tags.Items.DYES_YELLOW))
                .unlockedBy(getHasName(Items.HONEY_BOTTLE), has(Items.HONEY_BOTTLE))
                .requires(Tags.Items.DYES_ORANGE).requires(Tags.Items.DYES_YELLOW).requires(Items.HONEY_BOTTLE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "honey_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("icy_blue_dye").get(), 2)
                .group("icy_blue_dye")
                .unlockedBy(getHasName(Items.WHITE_DYE), has(Tags.Items.DYES_WHITE))
                .unlockedBy(getHasName(Items.LIGHT_BLUE_DYE), has(Tags.Items.DYES_LIGHT_BLUE))
                .requires(Tags.Items.DYES_WHITE).requires(Tags.Items.DYES_LIGHT_BLUE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "icy_blue_dye"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Tags.Items.DYES_BLUE), RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("ultramarine_dye").get(), 1.0F, 200)
                .unlockedBy(getHasName(Items.BLUE_DYE), has(Tags.Items.DYES_BLUE))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "ultramarine_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("rose_dye").get(), 3)
                .group("rose_dye")
                .unlockedBy("has_bubblegum_dye", has(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyes/bubblegum"))))
                .unlockedBy("has_maroon_dye", has(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyes/maroon"))))
                .requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyes/bubblegum"))).requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyes/maroon")))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "rose_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("navy_dye").get(), 2)
                .group("navy_dye")
                .unlockedBy(getHasName(Items.BLACK_DYE), has(Tags.Items.DYES_BLACK))
                .unlockedBy(getHasName(Items.BLUE_DYE), has(Tags.Items.DYES_BLUE))
                .requires(Tags.Items.DYES_BLACK).requires(Tags.Items.DYES_BLUE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "navy_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("spring_green_dye").get(), 2)
                .group("spring_green_dye")
                .unlockedBy(getHasName(Items.LIME_DYE), has(Tags.Items.DYES_LIME))
                .unlockedBy("has_mint_dye", has(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyes/mint"))))
                .unlockedBy(getHasName(Items.GLOWSTONE), has(Tags.Items.DUSTS_GLOWSTONE))
                .requires(Tags.Items.DYES_LIME).requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyes/mint"))).requires(Tags.Items.DUSTS_GLOWSTONE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "spring_green_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("wine_dye").get(), 2)
                .group("wine_dye")
                .unlockedBy(getHasName(Items.BLACK_DYE), has(Tags.Items.DYES_BLACK))
                .unlockedBy(getHasName(Items.PURPLE_DYE), has(Tags.Items.DYES_PURPLE))
                .unlockedBy("has_maroon_dye", has(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyes/maroon"))))
                .requires(Tags.Items.DYES_BLACK).requires(Tags.Items.DYES_PURPLE).requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyes/maroon")))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "wine_dye"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.DYE_ITEMS.get("conifer_dye").get(), 3)
                .group("conifer_dye")
                .unlockedBy(getHasName(Items.YELLOW_DYE), has(Tags.Items.DYES_YELLOW))
                .unlockedBy(getHasName(Items.LIME_DYE), has(Tags.Items.DYES_LIME))
                .unlockedBy(getHasName(Items.LIGHT_GRAY_DYE), has(Tags.Items.DYES_LIGHT_GRAY))
                .requires(Tags.Items.DYES_YELLOW).requires(Tags.Items.DYES_LIME).requires(Tags.Items.DYES_LIGHT_GRAY)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "conifer_dye"));

    }
}
