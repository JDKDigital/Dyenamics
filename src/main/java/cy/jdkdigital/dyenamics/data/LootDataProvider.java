package cy.jdkdigital.dyenamics.data;

import com.google.common.collect.Maps;
import cy.jdkdigital.dyenamics.core.init.BlockInit;
import cy.jdkdigital.dyenamics.core.init.EntityInit;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

public class LootDataProvider implements DataProvider
{
    private final PackOutput.PathProvider pathProvider;
    private final List<LootTableProvider.SubProviderEntry> subProviders;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public LootDataProvider(PackOutput output, List<LootTableProvider.SubProviderEntry> providers, CompletableFuture<HolderLookup.Provider> registries) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_table");
        this.subProviders = providers;
        this.registries = registries;
    }

    @Override
    public String getName() {
        return "Dyenamics Loot Datagen";
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        return this.registries.thenCompose(provider -> this.run(pOutput, provider));
    }

    private CompletableFuture<?> run(CachedOutput pOutput, HolderLookup.Provider pProvider) {
        final Map<ResourceLocation, LootTable> map = Maps.newHashMap();
        this.subProviders.forEach((providerEntry) -> {
            providerEntry.provider().apply(pProvider).generate((resourceKey, builder) -> {
                builder.setRandomSequence(resourceKey.location());
                if (map.put(resourceKey.location(), builder.setParamSet(providerEntry.paramSet()).build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + resourceKey.location());
                }
            });
        });

        return CompletableFuture.allOf(map.entrySet().stream().map((entry) -> {
            return DataProvider.saveStable(pOutput, pProvider, LootTable.DIRECT_CODEC, entry.getValue(), this.pathProvider.json(entry.getKey()));
        }).toArray(CompletableFuture[]::new));
    }

    public static class BlockProvider extends BlockLootSubProvider
    {
        private static final Map<Block, Function<Block, LootTable.Builder>> functionTable = new HashMap<>();
        private List<Block> knownBlocks = new ArrayList<>();

        public BlockProvider(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            for (DyenamicDyeColor color: DyenamicDyeColor.dyenamicValues()) {
                dropBanner(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("banner").get());
                add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("bed").get(), (block) -> this.createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
                add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("candle").get(), this::createCandleDrops);
                add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("candle_cake").get(), createCandleCakeDrops(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("candle_cake").get()));
                dropSelf(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("carpet").get());
                dropSelf(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("concrete").get());
                dropSelf(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("concrete_powder").get());
                dropSelf(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("glazed_terracotta").get());
                dropSelf(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("rockwool").get());
                add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("shulker_box").get(), this::createShulkerBoxDrop);
                dropWhenSilkTouch(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass").get());
                dropWhenSilkTouch(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass_pane").get());
                dropSelf(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("terracotta").get());
                dropOtherBanner(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wall_banner").get(), BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("banner").get());
                dropSelf(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get());
            }
        }

        @Override
        protected void add(Block block, LootTable.Builder builder) {
            super.add(block, builder);
            knownBlocks.add(block);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return knownBlocks;
        }

        protected void add(Block block, Function<Block, LootTable.Builder> builderFunction) {
            this.add(block, builderFunction.apply(block));
        }

        public void dropSelf(@NotNull Block block) {
            Function<Block, LootTable.Builder> func = functionTable.getOrDefault(block, BlockProvider::genOptionalBlockDrop);
            this.add(block, func.apply(block));
        }

        public void dropOther(@NotNull Block block, @NotNull Block otherBlock) {
            Function<Block, LootTable.Builder> func = functionTable.getOrDefault(block, BlockProvider::genOptionalBlockDrop);
            this.add(block, func.apply(otherBlock));
        }

        public void dropBanner(@NotNull Block block) {
            Function<Block, LootTable.Builder> func = functionTable.getOrDefault(block, super::createBannerDrop);
            this.add(block, func.apply(block));
        }

        public void dropOtherBanner(@NotNull Block block, @NotNull Block otherBlock) {
            Function<Block, LootTable.Builder> func = functionTable.getOrDefault(block, super::createBannerDrop);
            this.add(block, func.apply(otherBlock));
        }

        public void dropNothing(@NotNull Block block) {
            Function<Block, LootTable.Builder> func = functionTable.getOrDefault(block, BlockProvider::genBlankBlockDrop);
            this.add(block, func.apply(block));
        }

        protected static LootTable.Builder genOptionalBlockDrop(Block block) {
            LootPoolEntryContainer.Builder<?> builder = LootItem.lootTableItem(block).when(ExplosionCondition.survivesExplosion());

            return LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(builder));
        }

        protected static LootTable.Builder genBlankBlockDrop(Block block) {
            return LootTable.lootTable();
        }
    }

    public static class EntityLootProvider extends EntityLootSubProvider
    {
        List<EntityType<?>> knownEntities = new ArrayList<>();

        public EntityLootProvider(HolderLookup.Provider registries) {
            super(FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        public void generate() {
            for (DyenamicDyeColor color: DyenamicDyeColor.dyenamicValues()) {
                // TODO make work
//                addSheep(color);
            }
        }

        private void addSheep(DyenamicDyeColor color) {
            var item = BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get();

            var loot = LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(item)));

            this.add(EntityType.SHEEP, loot);
        }

        protected void add(EntityType<?> pEntityType, LootTable.Builder pBuilder) {
            super.add(pEntityType, pBuilder);
            knownEntities.add(pEntityType);
        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return knownEntities.stream();
        }
    }
}
