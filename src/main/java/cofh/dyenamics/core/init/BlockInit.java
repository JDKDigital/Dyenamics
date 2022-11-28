package cofh.dyenamics.core.init;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.blocks.*;
import cofh.dyenamics.common.items.DyenamicBedBlockItem;
import cofh.dyenamics.common.items.DyenamicShulkerBlockItem;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockInit
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Dyenamics.MOD_ID);
    public static final Map<String, Map<String, RegistryObject<Block>>> DYED_BLOCKS = new HashMap<>();

    public static void register() {
        for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
            registerDyeBlocks(color);
        }
    }

    public synchronized static void registerDyeBlocks(DyenamicDyeColor color) {
        String colorName = color.getSerializedName();
        int light = color.getLightValue();
        MaterialColor mapColor = color.getMapColor();
        DyeColor analogue = color.getAnalogue();
        final Map<String, RegistryObject<Block>> blocks = new HashMap<>();
        DYED_BLOCKS.put(colorName, blocks);

        registerBlockAndItem(colorName, "terracotta", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, BlockItem::new, () -> new Block(BlockBehaviour.Properties.of(Material.STONE, mapColor).strength(1.25F, 4.2F).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "glazed_terracotta", blocks, CreativeModeTab.TAB_DECORATIONS, BlockItem::new, () -> new GlazedTerracottaBlock(BlockBehaviour.Properties.of(Material.STONE, analogue).strength(1.4F).lightLevel(state -> light)));
        final RegistryObject<Block> concrete = registerBlockAndItem(colorName, "concrete", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, BlockItem::new, () -> new Block(BlockBehaviour.Properties.of(Material.STONE, analogue).strength(1.8F).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "concrete_powder", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, BlockItem::new, () -> new ConcretePowderBlock(concrete.get(), BlockBehaviour.Properties.of(Material.SAND, analogue).strength(0.5F).sound(SoundType.SAND).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "wool", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, BlockItem::new, () -> new DyenamicFlammableBlock(BlockBehaviour.Properties.of(Material.WOOL, mapColor).strength(0.8F).sound(SoundType.WOOL).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "rockwool", blocks, compatCreativeModeTab("thermal", CreativeModeTab.TAB_BUILDING_BLOCKS), BlockItem::new, () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.SNOW).strength(2.0F, 6.0F).sound(SoundType.WOOL).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "carpet", blocks, CreativeModeTab.TAB_DECORATIONS, BlockItem::new, () -> new DyenamicCarpetBlock(color, BlockBehaviour.Properties.of(Material.CLOTH_DECORATION, mapColor).strength(0.1F).sound(SoundType.WOOL).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "stained_glass", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, BlockItem::new, () -> new DyenamicStainedGlassBlock(color, BlockBehaviour.Properties.of(Material.GLASS, mapColor).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn((a, b, c, d) -> false).isRedstoneConductor((a, b, c) -> false).isSuffocating((a, b, c) -> false).isViewBlocking((a, b, c) -> false).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "stained_glass_pane", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, BlockItem::new, () -> new DyenamicStainedGlassPane(color, BlockBehaviour.Properties.of(Material.GLASS, mapColor).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn((a, b, c, d) -> false).isRedstoneConductor((a, b, c) -> false).isSuffocating((a, b, c) -> false).isViewBlocking((a, b, c) -> false).lightLevel(state -> light)));
        var candle = registerBlockAndItem(colorName, "candle", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, BlockItem::new, () -> new CandleBlock(BlockBehaviour.Properties.of(Material.DECORATION, mapColor).noOcclusion().strength(0.1F).sound(SoundType.CANDLE).lightLevel(state -> light)));
        registerBlockAndItem(colorName, "candle_cake", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, null, () -> new CandleCakeBlock(candle.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).lightLevel(state -> light)));
        registerBedAndItem(colorName, "bed", blocks, CreativeModeTab.TAB_DECORATIONS, DyenamicBedBlockItem::new, () -> new DyenamicBedBlock(color, BlockBehaviour.Properties.of(Material.WOOL, (state) -> state.getValue(BedBlock.PART) == BedPart.FOOT ? mapColor : MaterialColor.WOOL).sound(SoundType.WOOD).strength(0.2F).noOcclusion().lightLevel(state -> light)));
        BlockBehaviour.StatePredicate shulkerBoxBehavior = (blockState, level, blockPos) -> !(level.getBlockEntity(blockPos) instanceof ShulkerBoxBlockEntity box) || box.isClosed();
        registerShulkerBoxAndItem(colorName, "shulker_box", blocks, CreativeModeTab.TAB_DECORATIONS, DyenamicShulkerBlockItem::new, () -> new DyenamicShulkerBoxBlock(color, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, mapColor).strength(2.0F).dynamicShape().noOcclusion().isSuffocating(shulkerBoxBehavior).isViewBlocking(shulkerBoxBehavior)));
    }

    public synchronized static RegistryObject<Block> registerBedAndItem(String color, String nameSuffix, Map<String, RegistryObject<Block>> blockMap, CreativeModeTab group, BlockItemSupplier<?> itemSupplier, Supplier<Block> supplier) {
        return registerBlockAndItem(color, nameSuffix, blockMap, itemSupplier, supplier, new Item.Properties().tab(group).stacksTo(1));
    }

    public synchronized static RegistryObject<Block> registerShulkerBoxAndItem(String color, String nameSuffix, Map<String, RegistryObject<Block>> blockMap, CreativeModeTab group, BlockItemSupplier<?> itemSupplier, Supplier<Block> supplier) {
        return registerBlockAndItem(color, nameSuffix, blockMap, itemSupplier, supplier, new Item.Properties().tab(group).stacksTo(1));
    }

    public synchronized static RegistryObject<Block> registerBlockAndItem(String color, String nameSuffix, Map<String, RegistryObject<Block>> blockMap, CreativeModeTab group, BlockItemSupplier<?> itemSupplier, Supplier<Block> supplier) {
        return registerBlockAndItem(color, nameSuffix, blockMap, itemSupplier, supplier, new Item.Properties().tab(group));
    }

    public synchronized static RegistryObject<Block> registerBlockAndItem(String color, String nameSuffix, Map<String, RegistryObject<Block>> blockMap, BlockItemSupplier<?> itemSupplier, Supplier<Block> supplier, Item.Properties itemProperties) {
        String name = color + "_" + nameSuffix;
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        if (itemSupplier != null) {
            ItemInit.ITEMS.register(name, () -> itemSupplier.create(block.get(), itemProperties));
        }
        blockMap.put(nameSuffix, block);
        return block;
    }

    private static CreativeModeTab compatCreativeModeTab(String modId, CreativeModeTab group) {
        if (ModList.get().isLoaded(modId)) {
            return group;
        }
        return null;
    }

    @FunctionalInterface
    public interface BlockItemSupplier<T extends BlockItem> {
        T create(Block pBlock, Item.Properties pProperties);
    }
}