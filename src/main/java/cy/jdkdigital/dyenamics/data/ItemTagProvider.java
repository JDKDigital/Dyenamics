package cy.jdkdigital.dyenamics.data;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.core.init.BlockInit;
import cy.jdkdigital.dyenamics.core.init.ItemInit;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends ItemTagsProvider
{
    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> provider, ExistingFileHelper helper) {
        super(output, future, provider, Dyenamics.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var shulkerBoxes = tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath(Dyenamics.MODID, "shulker_boxes")));
        shulkerBoxes.add(Items.SHULKER_BOX,
                Items.WHITE_SHULKER_BOX,
                Items.ORANGE_SHULKER_BOX,
                Items.MAGENTA_SHULKER_BOX,
                Items.LIGHT_BLUE_SHULKER_BOX,
                Items.YELLOW_SHULKER_BOX,
                Items.LIME_SHULKER_BOX,
                Items.PINK_SHULKER_BOX,
                Items.GRAY_SHULKER_BOX,
                Items.LIGHT_GRAY_SHULKER_BOX,
                Items.CYAN_SHULKER_BOX,
                Items.PURPLE_SHULKER_BOX,
                Items.BLUE_SHULKER_BOX,
                Items.BROWN_SHULKER_BOX,
                Items.GREEN_SHULKER_BOX,
                Items.RED_SHULKER_BOX,
                Items.BLACK_SHULKER_BOX);

        for (DyenamicDyeColor color: DyenamicDyeColor.dyenamicValues()) {
            copy(BlockTags.BANNERS, ItemTags.BANNERS);
            copy(BlockTags.BEDS, ItemTags.BEDS);
            copy(BlockTags.CANDLES, ItemTags.CANDLES);
            copy(BlockTags.WOOL, ItemTags.WOOL);
            copy(BlockTags.WOOL_CARPETS, ItemTags.WOOL_CARPETS);
            copy(BlockTags.TERRACOTTA, ItemTags.TERRACOTTA);
            copy(BlockTags.CONCRETE_POWDER, Tags.Items.CONCRETE_POWDERS);
            copy(Tags.Blocks.CONCRETES, Tags.Items.CONCRETES);
            copy(Tags.Blocks.GLAZED_TERRACOTTAS, Tags.Items.GLAZED_TERRACOTTAS);

            shulkerBoxes.add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("shulker_box").get().asItem());

            copy(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS);
            copy(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);
            copy(Tags.Blocks.GLASS_BLOCKS_TINTED, Tags.Items.GLASS_BLOCKS_TINTED);
//            copy(Tags.Blocks.STAINED_GLASS_PANES, Tags.Items.STAINED_GLASS_PANES);
            tag(ItemTags.create(ResourceLocation.parse("c:glass/" + color.getSerializedName()))).add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass").get().asItem());
            tag(ItemTags.create(ResourceLocation.parse("c:glass_panes/" + color.getSerializedName()))).add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass_pane").get().asItem());

            tag(Tags.Items.DYES).add(ItemInit.DYE_ITEMS.get(color.getSerializedName() + "_dye").get());
            tag(ItemTags.create(ResourceLocation.parse("c:dyes/" + color.getSerializedName()))).add(ItemInit.DYE_ITEMS.get(color.getSerializedName() + "_dye").get());

            copy(BlockTags.create(ResourceLocation.parse("thermal:rockwool")), ItemTags.create(ResourceLocation.parse("thermal:rockwool")));
        }
    }

    @Override
    public String getName() {
        return "Dyenamics Item Tags Provider";
    }
}
