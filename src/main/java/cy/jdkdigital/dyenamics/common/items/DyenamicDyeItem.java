package cy.jdkdigital.dyenamics.common.items;

import com.google.common.collect.Maps;
import cy.jdkdigital.dyenamics.common.entities.DyenamicSheep;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class DyenamicDyeItem extends Item
{
    private static final Map<DyenamicDyeColor, DyenamicDyeItem> COLOR_DYE_ITEM_MAP = Maps.newEnumMap(DyenamicDyeColor.class);
    private final DyenamicDyeColor dyeColor;

    public DyenamicDyeItem(DyenamicDyeColor dyeColorIn, Item.Properties builder) {
        super(builder);
        this.dyeColor = dyeColorIn;
        COLOR_DYE_ITEM_MAP.put(dyeColorIn, this);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (target instanceof DyenamicSheep dyenamicSheep) {
            if (dyenamicSheep.isAlive() && !dyenamicSheep.isSheared() && dyenamicSheep.getDyenamicColor() != this.dyeColor) {
                if (!playerIn.level().isClientSide) {
                    dyenamicSheep.setColor(this.dyeColor);
                    stack.shrink(1);
                }
                return InteractionResult.sidedSuccess(playerIn.level().isClientSide);
            }
        } else if (target instanceof Sheep sheep) {
            if (sheep.isAlive() && !sheep.isSheared() && sheep.getColor().getId() != this.dyeColor.getId()) {
                if (!playerIn.level().isClientSide) {
                    DyenamicSheep.convertToDyenamics(sheep, dyeColor);
                    stack.shrink(1);
                }
                return InteractionResult.sidedSuccess(playerIn.level().isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    public DyenamicDyeColor getDyeColor() {
        return this.dyeColor;
    }

    public static DyenamicDyeItem getItem(DyenamicDyeColor color) {
        return COLOR_DYE_ITEM_MAP.get(color);
    }
}
