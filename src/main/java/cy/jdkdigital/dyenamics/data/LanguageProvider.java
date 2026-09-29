package cy.jdkdigital.dyenamics.data;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.core.init.BlockInit;
import cy.jdkdigital.dyenamics.core.init.ItemInit;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.data.PackOutput;

public class LanguageProvider extends net.neoforged.neoforge.common.data.LanguageProvider
{
    public LanguageProvider(PackOutput output) {
        super(output, Dyenamics.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("entity.dyenamics.sheep", "Sheep");

        for (DyenamicDyeColor color: DyenamicDyeColor.dyenamicValues()) {
            var colorName = capName(color.getSerializedName());
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("bed").get(), colorName + " Bed");
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("banner").get(), colorName+ " Banner");
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("shulker_box").get(), colorName + " Shulker Box");
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("candle").get(), colorName + " Candle");
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get(), colorName + " Wool");
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("rockwool").get(), colorName + " Rockwool");
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("carpet").get(), colorName + " Carpet");
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("terracotta").get(), colorName + " Terracotta");
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("glazed_terracotta").get(), colorName + " Glazed Terracotta");
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("concrete").get(), colorName + " Concrete");
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("concrete_powder").get(), colorName + " Concrete Powder");
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass").get(), colorName + " Stained Glass");
            add(BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("stained_glass_pane").get(), colorName + " Stained Glass Pane");
            add(ItemInit.DYE_ITEMS.get(color.getSerializedName() + "_dye").get(), colorName + " Dye");
        }
    }

    @Override
    public String getName() {
        return "Dyenamics translation provider";
    }

    private String capName(String name) {
        String[] nameParts = name.split("_");

        for (int i = 0; i < nameParts.length; i++) {
            nameParts[i] = nameParts[i].substring(0, 1).toUpperCase() + nameParts[i].substring(1);
        }

        return String.join(" ", nameParts);
    }
}
