package cofh.thermal.dyenamics.core.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import cofh.thermal.dyenamics.common.items.DyenamicDyeItem;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;


public enum DyenamicDyeColor implements IStringSerializable{
	
	PEACH(0, "peach", 12556403, MaterialColor.SAND, 12556403, 11570292, 0),
	AQUAMARINE(1, "aquamarine", 2915711, MaterialColor.CYAN_TERRACOTTA, 2915711, 2717304, 0),
	FLUORESCENT(2, "fluorescent", 15657398, MaterialColor.SAND, 15657398, 16117951, 15),
	MINT(3, "mint", 8772031, MaterialColor.GREEN, 8772031, 7914669, 0);
	
	private static final DyenamicDyeColor[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(DyenamicDyeColor::getId)).toArray((colorId) -> {
		return new DyenamicDyeColor[colorId];
    });
	
   //private static final Int2ObjectOpenHashMap<DyenamicDyeColor> BY_FIREWORK_COLOR = new Int2ObjectOpenHashMap<>(Arrays.stream(values()).collect(Collectors.toMap((color) -> {
   //   return color.fireworkColor;}, (color) -> {return color;})));
   
   private final int id;
   private final String translationKey;
   private final MaterialColor mapColor;
   private final int colorValue;
   private final int lightValue;
   private final int swappedColorValue;
   private final float[] colorComponentValues;
   private final int fireworkColor;
   private final net.minecraftforge.common.Tags.IOptionalNamedTag<Item> tag;
   private final int textColor;

   private DyenamicDyeColor(int idIn, String translationKeyIn, int colorValueIn, MaterialColor mapColorIn, int fireworkColorIn, int textColorIn, int lightValueIn) {
      this.id = idIn;
      this.translationKey = translationKeyIn;
      this.colorValue = colorValueIn;
      this.mapColor = mapColorIn;
      this.textColor = textColorIn;
      this.lightValue = lightValueIn;
      int i = (colorValueIn & 16711680) >> 16;
      int j = (colorValueIn & '\uff00') >> 8;
      int k = (colorValueIn & 255) >> 0;
      this.swappedColorValue = k << 16 | j << 8 | i << 0;
      this.tag = net.minecraft.tags.ItemTags.createOptional(new net.minecraft.util.ResourceLocation("forge", "dyes/" + translationKeyIn));
      this.colorComponentValues = new float[]{(float)i / 255.0F, (float)j / 255.0F, (float)k / 255.0F};
      this.fireworkColor = fireworkColorIn;
   }

   public int getId() {
      return this.id;
   }

   public String getTranslationKey() {
      return this.translationKey;
   }

   /**
    * Gets an array containing 3 floats ranging from 0.0 to 1.0: the red, green, and blue components of the
    * corresponding color.
    */
   public float[] getColorComponentValues() {
      return this.colorComponentValues;
   }

   public MaterialColor getMapColor() {
      return this.mapColor;
   }

   public int getFireworkColor() {
      return this.fireworkColor;
   }

   public int getTextColor() {
      return this.textColor;
   }

   public static DyenamicDyeColor byId(int colorId) {
      if (colorId < 0 || colorId >= VALUES.length) {
         colorId = 0;
      }

      return VALUES[colorId];
   }

   public static DyenamicDyeColor byTranslationKey(String translationKeyIn, DyenamicDyeColor fallback) {
      for(DyenamicDyeColor DyenamicDyeColor : values()) {
         if (DyenamicDyeColor.translationKey.equals(translationKeyIn)) {
            return DyenamicDyeColor;
         }
      }

      return fallback;
   }

   /*
   @Nullable
   public static DyenamicDyeColor byFireworkColor(int fireworkColorIn) {
      return BY_FIREWORK_COLOR.get(fireworkColorIn);
   }
   */

   public String toString() {
      return this.translationKey;
   }

   public String getString() {
      return this.translationKey;
   }

   public int getColorValue() {
      return colorValue;
   }

   public net.minecraftforge.common.Tags.IOptionalNamedTag<Item> getTag() {
      return tag;
   }

   @Nullable
   public static DyenamicDyeColor getColor(ItemStack stack) {
      if (stack.getItem() instanceof DyenamicDyeItem)
         return ((DyenamicDyeItem)stack.getItem()).getDyeColor();

      for (DyenamicDyeColor color : VALUES) {
         if (stack.getItem().isIn(color.getTag()))
             return color;
      }

      return null;
   }

}
