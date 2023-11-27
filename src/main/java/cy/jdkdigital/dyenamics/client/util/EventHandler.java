package cy.jdkdigital.dyenamics.client.util;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.common.blocks.DyenamicCarpetBlock;
import cy.jdkdigital.dyenamics.common.cap.DyenamicSwagImpl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Dyenamics.MOD_ID)
public class EventHandler
{
    @SubscribeEvent
    public static void attachCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Llama llama) {
            event.addCapability(DyenamicSwagImpl.Provider.NAME, new DyenamicSwagImpl.Provider(llama));
        }
    }

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel && event.getEntity() instanceof Llama llama) {
            llama.inventory.addListener(container -> llama.getCapability(Dyenamics.DYENAMIC_SWAG).ifPresent(swagProvider -> {
                ItemStack swagItem = container.getItem(1);
                if (swagItem.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof DyenamicCarpetBlock carpetBlock) {
                    swagProvider.setSwag(carpetBlock.getDyenamicColor().getId());
                } else {
                    swagProvider.removeSwag();
                }
            }));
        }
    }
}
