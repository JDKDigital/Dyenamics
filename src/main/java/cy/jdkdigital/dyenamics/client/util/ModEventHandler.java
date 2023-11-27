package cy.jdkdigital.dyenamics.client.util;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.common.cap.DyenamicSwagProvider;
import cy.jdkdigital.dyenamics.common.network.PacketHandler;
import cy.jdkdigital.dyenamics.core.init.BlockInit;
import cy.jdkdigital.dyenamics.core.init.EntityInit;
import cy.jdkdigital.dyenamics.core.init.ItemInit;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Dyenamics.MOD_ID)
public class ModEventHandler
{
    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(EntityInit::setup);
        PacketHandler.register();
    }

    @SubscribeEvent
    public static void onEntityAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(EntityInit.SHEEP.get(), Sheep.createAttributes().build());
    }

    @SubscribeEvent
    public static void tabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
            ItemInit.DYE_ITEMS.forEach((s, registryObject) -> event.accept(registryObject.get()));
        }
        for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
            var blocks = BlockInit.DYED_BLOCKS.get(color.getSerializedName());
            if (event.getTabKey().equals(CreativeModeTabs.COLORED_BLOCKS)) {
                event.accept(blocks.get("terracotta"));
                event.accept(blocks.get("glazed_terracotta"));
                event.accept(blocks.get("concrete"));
                event.accept(blocks.get("concrete_powder"));
                event.accept(blocks.get("wool"));
                event.accept(blocks.get("stained_glass"));
                event.accept(blocks.get("stained_glass_pane"));
                if (ModList.get().isLoaded("thermal")) {
                    event.accept(blocks.get("rockwool"));
                }
                event.accept(blocks.get("carpet"));
            }
            if (event.getTabKey().equals(CreativeModeTabs.COLORED_BLOCKS) || event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
                event.accept(blocks.get("candle"));
                event.accept(blocks.get("bed"));
                event.accept(blocks.get("shulker_box"));
            }
        }
    }

    @SubscribeEvent
    public static void registerCap(RegisterCapabilitiesEvent event) {
        event.register(DyenamicSwagProvider.class);
    }
}
