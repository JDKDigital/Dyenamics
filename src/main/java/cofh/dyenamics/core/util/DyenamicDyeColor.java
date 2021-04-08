package cofh.dyenamics.core.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import cofh.dyenamics.common.items.DyenamicDyeItem;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;


public enum DyenamicDyeColor implements IStringSerializable{
	//Vanilla Colors
	WHITE(0, "white", 16383998, MaterialColor.SNOW, 15790320, 16777215, 0),
	ORANGE(1, "orange", 16351261, MaterialColor.ADOBE, 15435844, 16738335, 0),
	MAGENTA(2, "magenta", 13061821, MaterialColor.MAGENTA, 12801229, 16711935, 0),
	LIGHT_BLUE(3, "light_blue", 3847130, MaterialColor.LIGHT_BLUE, 6719955, 10141901, 0),
	YELLOW(4, "yellow", 16701501, MaterialColor.YELLOW, 14602026, 16776960, 0),
	LIME(5, "lime", 8439583, MaterialColor.LIME, 4312372, 12582656, 0),
	PINK(6, "pink", 15961002, MaterialColor.PINK, 14188952, 16738740, 0),
	GRAY(7, "gray", 4673362, MaterialColor.GRAY, 4408131, 8421504, 0),
	LIGHT_GRAY(8, "light_gray", 10329495, MaterialColor.LIGHT_GRAY, 11250603, 13882323, 0),
	CYAN(9, "cyan", 1481884, MaterialColor.CYAN, 2651799, 65535, 0),
	PURPLE(10, "purple", 8991416, MaterialColor.PURPLE, 8073150, 10494192, 0),
	BLUE(11, "blue", 3949738, MaterialColor.BLUE, 2437522, 255, 0),
	BROWN(12, "brown", 8606770, MaterialColor.BROWN, 5320730, 9127187, 0),
	GREEN(13, "green", 6192150, MaterialColor.GREEN, 3887386, 65280, 0),
	RED(14, "red", 11546150, MaterialColor.RED, 11743532, 16711680, 0),
	BLACK(15, "black", 1908001, MaterialColor.BLACK, 1973019, 0, 0),
	
	//Dyenamics Colors
	PEACH(16, "peach", 12556403, MaterialColor.SAND, 12556403, 11570292, 0),
	AQUAMARINE(17, "aquamarine", 2915711, MaterialColor.CYAN_TERRACOTTA, 2915711, 2717304, 0),
	FLUORESCENT(18, "fluorescent", 15657398, MaterialColor.SAND, 15657398, 16117951, 15),
	MINT(19, "mint", 9038008, MaterialColor.GREEN, 9038008, 7917475, 0),
	MAROON(20, "maroon", 9502720, MaterialColor.RED, 9502720, 9502720, 0),
	BUBBLEGUM(21, "bubblegum", 16216518, MaterialColor.PINK, 16216518, 14574769, 0),
	LAVENDER(22, "lavender", 14522879, MaterialColor.PURPLE, 14522879, 13339371, 0),
	PERSIMMON(23, "persimmon", 13779225, MaterialColor.ORANGE_TERRACOTTA, 13779225, 12203795, 0),
	CHERENKOV(24, "cherenkov", 104655, MaterialColor.LIGHT_BLUE, 104655, 35002, 8);
	
	private static final DyenamicDyeColor[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(DyenamicDyeColor::getId)).toArray((colorId) -> {
		return new DyenamicDyeColor[colorId];});
	
   private static final Int2ObjectOpenHashMap<DyenamicDyeColor> BY_FIREWORK_COLOR = new Int2ObjectOpenHashMap<>(Arrays.stream(values()).collect(Collectors.toMap((color) -> {
      return color.fireworkColor;}, (color) -> {return color;})));
   
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
      System.out.println(translationKeyIn + " dye: " + this.colorComponentValues.toString());
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
   
   public int getLightValue() {
	  return this.lightValue;
   }   

   public DyeColor getVanillaColor() {
	   return DyeColor.byId(this.id % 16);
   }
   
   public static DyenamicDyeColor[] dyenamicValues() {
	   DyenamicDyeColor[] colors = DyenamicDyeColor.values();
	   return Arrays.copyOfRange(colors, 16, colors.length);
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

   
   @Nullable
   public static DyenamicDyeColor byFireworkColor(int fireworkColorIn) {
      return BY_FIREWORK_COLOR.get(fireworkColorIn);
   }
   

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
