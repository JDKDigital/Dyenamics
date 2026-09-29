package cy.jdkdigital.dyenamics.core.util;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.common.item.DyenamicDyeItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = Dyenamics.MODID)
public class EventHandler
{
    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getItemStack().getItem() instanceof DyenamicDyeItem dyeItem) {
            if (event.getTarget() instanceof Wolf wolf && wolf.isTame() && wolf.isOwnedBy(event.getEntity())) {
                var colorId = wolf.getEntityData().get(Wolf.DATA_COLLAR_COLOR);
                if (!event.getLevel().isClientSide && colorId != dyeItem.getDyeColor().getId()) {
                    wolf.getEntityData().set(Wolf.DATA_COLLAR_COLOR, dyeItem.getDyeColor().getId());
                    if (!event.getEntity().hasInfiniteMaterials()) {
                        event.getItemStack().shrink(1);
                    }
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
            }
            if (event.getTarget() instanceof Cat cat && cat.isTame() && cat.isOwnedBy(event.getEntity())) {
                var colorId = cat.getEntityData().get(Cat.DATA_COLLAR_COLOR);
                if (!event.getLevel().isClientSide && colorId != dyeItem.getDyeColor().getId()) {
                    cat.getEntityData().set(Cat.DATA_COLLAR_COLOR, dyeItem.getDyeColor().getId());
                    if (!event.getEntity().hasInfiniteMaterials()) {
                        event.getItemStack().shrink(1);
                    }
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
            }
        }
    }
}
