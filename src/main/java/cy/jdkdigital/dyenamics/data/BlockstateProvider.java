package cy.jdkdigital.dyenamics.data;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.core.init.BlockInit;
import cy.jdkdigital.dyenamics.core.init.ItemInit;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockstateProvider implements DataProvider
{
    protected final PackOutput packOutput;

    protected final Map<ResourceLocation, Supplier<JsonElement>> models = new HashMap<>();

    public BlockstateProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<Block, BlockStateGenerator> blockModels = Maps.newHashMap();
        Consumer<BlockStateGenerator> blockStateOutput = (blockStateGenerator) -> {
            Block block = blockStateGenerator.getBlock();
            BlockStateGenerator blockstategenerator = blockModels.put(block, blockStateGenerator);
            if (blockstategenerator != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        };
        Map<ResourceLocation, Supplier<JsonElement>> itemModels = Maps.newHashMap();
        BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput = (resourceLocation, elementSupplier) -> {
            Supplier<JsonElement> supplier = itemModels.put(resourceLocation, elementSupplier);
            if (supplier != null) {
                throw new IllegalStateException("Duplicate model definition for " + resourceLocation);
            }
        };

        ModelGenerator generator = new ModelGenerator();
        generator.registerStatesAndModels(blockStateOutput, modelOutput);

        for (DyenamicDyeColor color: DyenamicDyeColor.dyenamicValues()) {
            createBedItem(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("bed").get(), BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get(), modelOutput);
            createBannerItem(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("banner").get(), modelOutput);
            createShulkerBox(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("shulker_box").get(), modelOutput);
            createSimpleFlatItemModel(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("candle").get().asItem(), modelOutput);
            addBlockItemParentModel(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get(), itemModels);
            addOtherBlockItemParentModel(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("rockwool").get(), BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get(), itemModels);
            addBlockItemParentModel(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("carpet").get(), itemModels);
            addBlockItemParentModel(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("terracotta").get(), itemModels);
            addBlockItemParentModel(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("glazed_terracotta").get(), itemModels);
            addBlockItemParentModel(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("concrete").get(), itemModels);
            addBlockItemParentModel(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("concrete_powder").get(), itemModels);
            addBlockItemParentModel(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass").get(), itemModels);
            createSimpleFlatItemModel(ItemInit.DYE_ITEMS.get(color.getSerializedName() + "_dye").get(), modelOutput);
        }

        PackOutput.PathProvider blockstatePathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        PackOutput.PathProvider modelPathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");

        List<CompletableFuture<?>> output = new ArrayList<>();
        blockModels.forEach((block, supplier) -> {
            output.add(DataProvider.saveStable(cache, supplier.get(), blockstatePathProvider.json(BuiltInRegistries.BLOCK.getKey(block))));
        });
        itemModels.forEach((rLoc, supplier) -> {
            output.add(DataProvider.saveStable(cache, supplier.get(), modelPathProvider.json(rLoc)));
        });

        return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
    }

    private void addItemModel(Item item, Supplier<JsonElement> supplier, Map<ResourceLocation, Supplier<JsonElement>> itemModels) {
        if (item != null) {
            ResourceLocation resourcelocation = ModelLocationUtils.getModelLocation(item);
            if (!itemModels.containsKey(resourcelocation)) {
                itemModels.put(resourcelocation, supplier);
            }
        }
    }

    private void addBlockItemParentModel(Block block, Map<ResourceLocation, Supplier<JsonElement>> itemModels) {
        Item item = Item.BY_BLOCK.get(block);
        if (item != null) {
            addItemModel(item, new DelegatedModel(BuiltInRegistries.BLOCK.getKey(block).withPath(p -> "block/" + p)), itemModels);
        }
    }

    private void addOtherBlockItemParentModel(Block block, Block otherBlock, Map<ResourceLocation, Supplier<JsonElement>> itemModels) {
        Item item = Item.BY_BLOCK.get(block);
        if (item != null) {
            addItemModel(item, new DelegatedModel(BuiltInRegistries.BLOCK.getKey(otherBlock).withPath(p -> "block/" + p)), itemModels);
        }
    }

    private void createSimpleFlatItemModel(Item pFlatItem, BiConsumer<ResourceLocation, Supplier<JsonElement>> itemModels) {
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(pFlatItem), TextureMapping.layer0(pFlatItem), itemModels);
    }

    private void createBedItem(Block pBedBlock, Block pWoolBlock, BiConsumer<ResourceLocation, Supplier<JsonElement>> itemModels) {
        ModelTemplates.BED_INVENTORY.create(ModelLocationUtils.getModelLocation(pBedBlock.asItem()), TextureMapping.particle(pWoolBlock), itemModels);
    }

    private void createBannerItem(Block pBedBlock, BiConsumer<ResourceLocation, Supplier<JsonElement>> itemModels) {
        ModelTemplates.BANNER_INVENTORY.create(ModelLocationUtils.getModelLocation(pBedBlock.asItem()), TextureMapping.particle(Blocks.OAK_PLANKS), itemModels);
    }

    private void createShulkerBox(Block pShulkerBoxBlock, BiConsumer<ResourceLocation, Supplier<JsonElement>> itemModels) {
        ModelTemplates.SHULKER_BOX_INVENTORY.create(ModelLocationUtils.getModelLocation(pShulkerBoxBlock.asItem()), TextureMapping.particle(pShulkerBoxBlock), itemModels);
    }

    @Override
    public String getName() {
        return "Dyenamics Blockstate and Model generator";
    }

    static class ModelGenerator
    {
        Consumer<BlockStateGenerator> blockStateOutput;
        BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;

        protected void registerStatesAndModels(Consumer<BlockStateGenerator> blockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput) {
            this.blockStateOutput = blockStateOutput;
            this.modelOutput = modelOutput;

            for (DyenamicDyeColor color: DyenamicDyeColor.dyenamicValues()) {
                this.blockStateOutput.accept(createEntityBlock(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("bed").get(), ModelLocationUtils.decorateBlockModelLocation("bed")));
                this.blockStateOutput.accept(createEntityBlock(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("banner").get(), ModelLocationUtils.decorateBlockModelLocation("banner")));
                this.blockStateOutput.accept(createEntityBlock(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wall_banner").get(), ModelLocationUtils.decorateBlockModelLocation("banner")));
                this.blockStateOutput.accept(createSimpleBlock(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("shulker_box").get(), TexturedModel.PARTICLE_ONLY.create(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("shulker_box").get(), this.modelOutput)));

                createCandleAndCandleCake(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("candle").get(), BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("candle_cake").get());

                createCubeBlock(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get());
                this.blockStateOutput.accept(createSimpleBlock(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("rockwool").get(), ModelLocationUtils.getModelLocation(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get())));
                createCarpetBlock(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get(), BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("carpet").get());
                createCubeBlock(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("terracotta").get());
                createColoredBlockWithStateRotations(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("glazed_terracotta").get());
                createCubeBlock(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("concrete").get());
                createCubeBlock(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("concrete_powder").get());

                createGlassBlocks(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass").get(), BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass_pane").get());
            }
        }

        private void createCubeBlock(Block pBlock) {
            this.blockStateOutput.accept(createSimpleBlock(pBlock, ModelTemplates.CUBE_ALL.create(pBlock, TextureMapping.defaultTexture(pBlock).put(TextureSlot.ALL, ModelLocationUtils.getModelLocation(pBlock)), this.modelOutput)));
        }

        private void createGlassBlock(Block pBlock) {
            var template = new ModelTemplate(Optional.of(ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "block/stained_glass")), Optional.empty(), TextureSlot.ALL);
            this.blockStateOutput.accept(createSimpleBlock(pBlock, template.create(pBlock, TextureMapping.defaultTexture(pBlock).put(TextureSlot.ALL, ModelLocationUtils.getModelLocation(pBlock)), this.modelOutput)));
        }

        private void createColoredBlockWithStateRotations(Block pBlock) {
            ResourceLocation resourcelocation = TexturedModel.GLAZED_TERRACOTTA.create(pBlock, this.modelOutput);
            this.blockStateOutput
                    .accept(
                            MultiVariantGenerator.multiVariant(pBlock, Variant.variant().with(VariantProperties.MODEL, resourcelocation))
                                    .with(createHorizontalFacingDispatchAlt())
                    );
        }

        private static PropertyDispatch createHorizontalFacingDispatchAlt() {
            return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
                    .select(Direction.SOUTH, Variant.variant())
                    .select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                    .select(Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                    .select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
        }

        private MultiVariantGenerator createEntityBlock(Block pBlock, ResourceLocation pBaseModel) {
            return MultiVariantGenerator.multiVariant(pBlock, Variant.variant().with(VariantProperties.MODEL, pBaseModel));
        }

        private MultiVariantGenerator createSimpleBlock(Block pBlock, ResourceLocation pModelLocation) {
            return MultiVariantGenerator.multiVariant(pBlock, Variant.variant().with(VariantProperties.MODEL, pModelLocation));
        }

        private void createCarpetBlock(Block wool, Block carpet) {
            ResourceLocation resourcelocation = TexturedModel.CARPET.get(wool).create(carpet, this.modelOutput);
            this.blockStateOutput.accept(createSimpleBlock(carpet, resourcelocation));
        }

        private void createCandleAndCandleCake(Block pCandleBlock, Block pCandleCakeBlock) {
            TextureMapping candleTextureMapping = TextureMapping.cube(TextureMapping.getBlockTexture(pCandleBlock));
            TextureMapping litCandleTextureMapping = TextureMapping.cube(TextureMapping.getBlockTexture(pCandleBlock, "_lit"));
            ResourceLocation oneCandle = ModelTemplates.CANDLE.createWithSuffix(pCandleBlock, "_one_candle", candleTextureMapping, this.modelOutput);
            ResourceLocation twoCandles = ModelTemplates.TWO_CANDLES.createWithSuffix(pCandleBlock, "_two_candles", candleTextureMapping, this.modelOutput);
            ResourceLocation threeCandles = ModelTemplates.THREE_CANDLES.createWithSuffix(pCandleBlock, "_three_candles", candleTextureMapping, this.modelOutput);
            ResourceLocation fourCandles = ModelTemplates.FOUR_CANDLES.createWithSuffix(pCandleBlock, "_four_candles", candleTextureMapping, this.modelOutput);
            ResourceLocation oneCandleLit = ModelTemplates.CANDLE.createWithSuffix(pCandleBlock, "_one_candle_lit", litCandleTextureMapping, this.modelOutput);
            ResourceLocation twoCandlesLit = ModelTemplates.TWO_CANDLES.createWithSuffix(pCandleBlock, "_two_candles_lit", litCandleTextureMapping, this.modelOutput);
            ResourceLocation threeCandlesLit = ModelTemplates.THREE_CANDLES.createWithSuffix(pCandleBlock, "_three_candles_lit", litCandleTextureMapping, this.modelOutput);
            ResourceLocation fourCandlesLit = ModelTemplates.FOUR_CANDLES.createWithSuffix(pCandleBlock, "_four_candles_lit", litCandleTextureMapping, this.modelOutput);

            this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(pCandleBlock).with(PropertyDispatch.properties(BlockStateProperties.CANDLES, BlockStateProperties.LIT).select(1, false, Variant.variant().with(VariantProperties.MODEL, oneCandle)).select(2, false, Variant.variant().with(VariantProperties.MODEL, twoCandles)).select(3, false, Variant.variant().with(VariantProperties.MODEL, threeCandles)).select(4, false, Variant.variant().with(VariantProperties.MODEL, fourCandles)).select(1, true, Variant.variant().with(VariantProperties.MODEL, oneCandleLit)).select(2, true, Variant.variant().with(VariantProperties.MODEL, twoCandlesLit)).select(3, true, Variant.variant().with(VariantProperties.MODEL, threeCandlesLit)).select(4, true, Variant.variant().with(VariantProperties.MODEL, fourCandlesLit))));
            ResourceLocation candleCake = ModelTemplates.CANDLE_CAKE.create(pCandleCakeBlock, TextureMapping.candleCake(pCandleBlock, false), this.modelOutput);
            ResourceLocation candleCakeLit = ModelTemplates.CANDLE_CAKE.createWithSuffix(pCandleCakeBlock, "_lit", TextureMapping.candleCake(pCandleBlock, true), this.modelOutput);
            this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(pCandleCakeBlock).with(createBooleanModelDispatch(BlockStateProperties.LIT, candleCakeLit, candleCake)));
        }

        private void createGlassBlocks(Block pGlassBlock, Block pPaneBlock) {
            this.createGlassBlock(pGlassBlock);
            TextureMapping texturemapping = TextureMapping.pane(pGlassBlock, pPaneBlock);
            ResourceLocation panePost = ModelTemplates.STAINED_GLASS_PANE_POST.create(pPaneBlock, texturemapping, this.modelOutput);
            ResourceLocation paneSide = ModelTemplates.STAINED_GLASS_PANE_SIDE.create(pPaneBlock, texturemapping, this.modelOutput);
            ResourceLocation paneSideAlt = ModelTemplates.STAINED_GLASS_PANE_SIDE_ALT.create(pPaneBlock, texturemapping, this.modelOutput);
            ResourceLocation pandNoside = ModelTemplates.STAINED_GLASS_PANE_NOSIDE.create(pPaneBlock, texturemapping, this.modelOutput);
            ResourceLocation paneNosideAlt = ModelTemplates.STAINED_GLASS_PANE_NOSIDE_ALT.create(pPaneBlock, texturemapping, this.modelOutput);
            Item item = pPaneBlock.asItem();
            ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(pGlassBlock), this.modelOutput);
            this.blockStateOutput.accept(MultiPartGenerator.multiPart(pPaneBlock).with(Variant.variant().with(VariantProperties.MODEL, panePost)).with(Condition.condition().term(BlockStateProperties.NORTH, true), Variant.variant().with(VariantProperties.MODEL, paneSide)).with(Condition.condition().term(BlockStateProperties.EAST, true), Variant.variant().with(VariantProperties.MODEL, paneSide).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).with(Condition.condition().term(BlockStateProperties.SOUTH, true), Variant.variant().with(VariantProperties.MODEL, paneSideAlt)).with(Condition.condition().term(BlockStateProperties.WEST, true), Variant.variant().with(VariantProperties.MODEL, paneSideAlt).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).with(Condition.condition().term(BlockStateProperties.NORTH, false), Variant.variant().with(VariantProperties.MODEL, pandNoside)).with(Condition.condition().term(BlockStateProperties.EAST, false), Variant.variant().with(VariantProperties.MODEL, paneNosideAlt)).with(Condition.condition().term(BlockStateProperties.SOUTH, false), Variant.variant().with(VariantProperties.MODEL, paneNosideAlt).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).with(Condition.condition().term(BlockStateProperties.WEST, false), Variant.variant().with(VariantProperties.MODEL, pandNoside).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
        }

        private static PropertyDispatch createBooleanModelDispatch(BooleanProperty pProperty, ResourceLocation pTrueModelLocation, ResourceLocation pFalseModelLocation) {
            return PropertyDispatch.property(pProperty).select(true, Variant.variant().with(VariantProperties.MODEL, pTrueModelLocation)).select(false, Variant.variant().with(VariantProperties.MODEL, pFalseModelLocation));
        }
    }
}
