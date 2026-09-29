package cy.jdkdigital.dyenamics.core.init;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.common.block.*;
import cy.jdkdigital.dyenamics.common.item.DyenamicBannerItem;
import cy.jdkdigital.dyenamics.common.item.DyenamicBedBlockItem;
import cy.jdkdigital.dyenamics.common.item.DyenamicShulkerBlockItem;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockInit
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Dyenamics.MODID);
    public static final Map<String, Map<String, DeferredHolder<Block, Block>>> DYED_BLOCKS = new HashMap<>();

    public static void register() {
        for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
            registerDyeBlocks(color);
        }
    }

    public synchronized static void registerDyeBlocks(DyenamicDyeColor color) {
        String colorName = color.getSerializedName();
        int light = color.getLightValue();
        MapColor mapColor = color.getMapColor();
        DyeColor analogue = color.getAnalogue();
        final Map<String, DeferredHolder<Block, Block>> blocks = new HashMap<>();
        DYED_BLOCKS.put(colorName, blocks);

        registerBlockAndItem(colorName, "terracotta", blocks, BlockItem::new, () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(mapColor).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "glazed_terracotta", blocks, BlockItem::new, () -> new GlazedTerracottaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_GLAZED_TERRACOTTA).mapColor(analogue).lightLevel(state -> light)));
        final DeferredHolder<Block, Block> concrete = registerBlockAndItem(colorName, "concrete", blocks, BlockItem::new, () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE).mapColor(analogue).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "concrete_powder", blocks, BlockItem::new, () -> new ConcretePowderBlock(concrete.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE_POWDER).mapColor(analogue).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "wool", blocks, BlockItem::new, () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).mapColor(mapColor).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "rockwool", blocks, BlockItem::new, () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).strength(2.0F, 6.0F).sound(SoundType.WOOL).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "carpet", blocks, BlockItem::new, () -> new DyenamicCarpetBlock(color, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CARPET).mapColor(mapColor).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "stained_glass", blocks, BlockItem::new, () -> new DyenamicStainedGlassBlock(color, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_STAINED_GLASS).mapColor(mapColor).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "stained_glass_pane", blocks, BlockItem::new, () -> new DyenamicStainedGlassPane(color, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_STAINED_GLASS_PANE).mapColor(mapColor).lightLevel(state -> light)));
        var candle = registerBlockAndItem(colorName, "candle", blocks, BlockItem::new, () -> new CandleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE).mapColor(mapColor).lightLevel(state -> {
            var candleLight = CandleBlock.LIGHT_EMISSION.applyAsInt(state);
            return Math.max(light, candleLight);
        })));
        registerBlockAndItem(colorName, "candle_cake", blocks, null, () -> new CandleCakeBlock(candle.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE).lightLevel(state -> light > 0 ? light :state.getValue(BlockStateProperties.LIT) ? 3 : 0)));
        registerBedAndItem(colorName, "bed", blocks, DyenamicBedBlockItem::new, () -> new DyenamicBedBlock(color, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_BED).mapColor((state) -> state.getValue(BedBlock.PART) == BedPart.FOOT ? mapColor : MapColor.WOOL).lightLevel(state -> light)));
        registerShulkerBoxAndItem(colorName, "shulker_box", blocks, DyenamicShulkerBlockItem::new, () -> new DyenamicShulkerBoxBlock(color, BlockBehaviour.Properties.ofFullCopy(Blocks.SHULKER_BOX).lightLevel(state -> light).mapColor(mapColor)));
        var wallBanner = registerBlockAndItem(colorName, "wall_banner", blocks, null, () -> new DyenamicWallBannerBlock(color, BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_BANNER).lightLevel(state -> light)));
        registerBannerBlockAndItem(colorName, "banner", blocks, () -> new DyenamicBannerBlock(color, BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_BANNER).lightLevel(state -> light)), wallBanner);
    }

    public synchronized static DeferredHolder<Block, Block> registerBedAndItem(String color, String nameSuffix, Map<String, DeferredHolder<Block, Block>> blockMap, BlockItemSupplier<?> itemSupplier, Supplier<Block> supplier) {
        return registerBlockAndItem(color, nameSuffix, blockMap, itemSupplier, supplier, new Item.Properties().stacksTo(1));
    }

    public synchronized static DeferredHolder<Block, Block> registerShulkerBoxAndItem(String color, String nameSuffix, Map<String, DeferredHolder<Block, Block>> blockMap, BlockItemSupplier<?> itemSupplier, Supplier<Block> supplier) {
        return registerBlockAndItem(color, nameSuffix, blockMap, itemSupplier, supplier, new Item.Properties().stacksTo(1));
    }

    public synchronized static DeferredHolder<Block, Block> registerBlockAndItem(String color, String nameSuffix, Map<String, DeferredHolder<Block, Block>> blockMap, BlockItemSupplier<?> itemSupplier, Supplier<Block> supplier) {
        return registerBlockAndItem(color, nameSuffix, blockMap, itemSupplier, supplier, new Item.Properties());
    }

    public synchronized static DeferredHolder<Block, Block> registerBlockAndItem(String color, String nameSuffix, Map<String, DeferredHolder<Block, Block>> blockMap, BlockItemSupplier<?> itemSupplier, Supplier<Block> supplier, Item.Properties itemProperties) {
        String name = color + "_" + nameSuffix;
        DeferredHolder<Block, Block> block = BLOCKS.register(name, supplier);
        if (itemSupplier != null) {
            ItemInit.ITEMS.register(name, () -> itemSupplier.create(block.get(), itemProperties));
        }
        blockMap.put(nameSuffix, block);
        return block;
    }

    public synchronized static DeferredHolder<Block, Block> registerBannerBlockAndItem(String color, String nameSuffix, Map<String, DeferredHolder<Block, Block>> blockMap, Supplier<Block> banner, DeferredHolder<Block, Block> wallBanner) {
        String name = color + "_" + nameSuffix;
        DeferredHolder<Block, Block> block = BLOCKS.register(name, banner);
        ItemInit.ITEMS.register(name, () -> new DyenamicBannerItem(block.get(), wallBanner.get(), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)));
        blockMap.put(nameSuffix, block);
        return block;
    }

    @FunctionalInterface
    public interface BlockItemSupplier<T extends BlockItem>
    {
        T create(Block pBlock, Item.Properties pProperties);
    }
}