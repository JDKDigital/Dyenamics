package cofh.dyenamics.common.items;

import cofh.dyenamics.common.entities.DyenamicSheepEntity;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

import java.util.Map;

public class DyenamicDyeItem extends Item {
   private static final Map<DyenamicDyeColor, DyenamicDyeItem> COLOR_DYE_ITEM_MAP = Maps.newEnumMap(DyenamicDyeColor.class);
   private final DyenamicDyeColor dyeColor;

   public DyenamicDyeItem(DyenamicDyeColor dyeColorIn, Item.Properties builder) {
      super(builder);
      this.dyeColor = dyeColorIn;
      COLOR_DYE_ITEM_MAP.put(dyeColorIn, this);
   }

   /**
    * Returns true if the item can be used on the given entity, e.g. shears on sheep.
    */
   public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
      if (target instanceof DyenamicSheepEntity) {
    	 DyenamicSheepEntity sheepentity = (DyenamicSheepEntity)target;
         if (sheepentity.isAlive() && !sheepentity.getSheared() && sheepentity.getDyenamicFleeceColor() != this.dyeColor) {
            if (!playerIn.world.isRemote) {
               sheepentity.setFleeceColor(this.dyeColor);
               stack.shrink(1);
            }
            return ActionResultType.func_233537_a_(playerIn.world.isRemote);
         }
      }
      else if (target instanceof SheepEntity) {
     	 SheepEntity sheepentity = (SheepEntity)target;
          if (sheepentity.isAlive() && !sheepentity.getSheared() && sheepentity.getFleeceColor().getId() != this.dyeColor.getId()) {
             if (!playerIn.world.isRemote) {
            	DyenamicSheepEntity.convertToDyenamics(sheepentity, dyeColor);
                stack.shrink(1);
             }
             return ActionResultType.func_233537_a_(playerIn.world.isRemote);
          }
       }
      return ActionResultType.PASS;
   }

   public DyenamicDyeColor getDyeColor() {
      return this.dyeColor;
   }

   public static DyenamicDyeItem getItem(DyenamicDyeColor color) {
      return COLOR_DYE_ITEM_MAP.get(color);
   }
}
