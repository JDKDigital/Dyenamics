package cy.jdkdigital.dyenamics.core.util;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.core.init.BlockInit;
import cy.jdkdigital.dyenamics.core.init.EntityInit;
import cy.jdkdigital.dyenamics.core.init.ItemInit;
import net.minecraft.core.Holder;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.GameData;

import java.util.Map;
import java.util.function.Function;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Dyenamics.MODID)
public class ModEventHandler
{
    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
            var blocks = BlockInit.DYED_BLOCKS.get(color.getSerializedName());
            CauldronInteraction.WATER.map().put(blocks.get("banner").get().asItem(), CauldronInteraction.BANNER);
        }

        // villagers claim a bed through the HOME poi, which vanilla only maps to the head half of vanilla beds
        event.enqueueWork(() -> {
            Holder<PoiType> home = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(PoiTypes.HOME);
            Map<BlockState, Holder<PoiType>> poiByState = GameData.getBlockStatePointOfInterestTypeMap();
            for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
                BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("bed").get().getStateDefinition().getPossibleStates().stream()
                        .filter(state -> state.getValue(BedBlock.PART) == BedPart.HEAD)
                        .forEach(state -> poiByState.putIfAbsent(state, home));
            }
        });
    }

    @SubscribeEvent
    public static void onEntityAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(EntityInit.SHEEP.get(), Sheep.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(EntityInit.SHEEP.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (animal, worldIn, reason, pos, random) -> false, RegisterSpawnPlacementsEvent.Operation.OR);
    }

    @SubscribeEvent
    public static void tabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
            insertAfter(event, new ItemStack(Items.PINK_DYE), color -> ItemInit.DYE_ITEMS.get(color.getSerializedName() + "_dye").get());
        }
        if (event.getTabKey().equals(CreativeModeTabs.COLORED_BLOCKS)) {
            ItemStack lastWool = insertAfter(event, new ItemStack(Items.PINK_WOOL), "wool");
            if (ModList.get().isLoaded("thermal")) {
                insertAfter(event, lastWool, "rockwool");
            }
            insertAfter(event, new ItemStack(Items.PINK_CARPET), "carpet");
            insertAfter(event, new ItemStack(Items.PINK_TERRACOTTA), "terracotta");
            insertAfter(event, new ItemStack(Items.PINK_CONCRETE), "concrete");
            insertAfter(event, new ItemStack(Items.PINK_CONCRETE_POWDER), "concrete_powder");
            insertAfter(event, new ItemStack(Items.PINK_GLAZED_TERRACOTTA), "glazed_terracotta");
            insertAfter(event, new ItemStack(Items.PINK_STAINED_GLASS), "stained_glass");
            insertAfter(event, new ItemStack(Items.PINK_STAINED_GLASS_PANE), "stained_glass_pane");
        }
        if (event.getTabKey().equals(CreativeModeTabs.COLORED_BLOCKS) || event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            insertAfter(event, new ItemStack(Items.PINK_SHULKER_BOX), "shulker_box");
            insertAfter(event, new ItemStack(Items.PINK_BED), "bed");
            insertAfter(event, new ItemStack(Items.PINK_CANDLE), "candle");
            insertAfter(event, new ItemStack(Items.PINK_BANNER), "banner");
        }
    }

    private static ItemStack insertAfter(BuildCreativeModeTabContentsEvent event, ItemStack anchor, String blockType) {
        return insertAfter(event, anchor, color -> BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get(blockType).get());
    }

    /**
     * Places one item per dyenamic color directly after {@code anchor}, keeping color order. Falls back to the end of the
     * tab when the anchor has been removed from it.
     *
     * @return the last inserted stack
     */
    private static ItemStack insertAfter(BuildCreativeModeTabContentsEvent event, ItemStack anchor, Function<DyenamicDyeColor, ItemLike> itemForColor) {
        ItemStack previous = anchor;
        for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
            ItemStack stack = new ItemStack(itemForColor.apply(color));
            if (event.getParentEntries().contains(previous) && event.getSearchEntries().contains(previous)) {
                event.insertAfter(previous, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            } else {
                event.accept(stack);
            }
            previous = stack;
        }
        return previous;
    }
}
