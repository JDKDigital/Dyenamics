package cy.jdkdigital.dyenamics.core.util;

import cy.jdkdigital.dyenamics.common.items.DyenamicDyeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;


public enum DyenamicDyeColor implements StringRepresentable
{
    //Vanilla Colors
    WHITE(0, "white", 16383998, DyeColor.WHITE, MapColor.SNOW, 15790320, 16777215, 0),
    ORANGE(1, "orange", 16351261, DyeColor.ORANGE, MapColor.COLOR_ORANGE, 15435844, 16738335, 0),
    MAGENTA(2, "magenta", 13061821, DyeColor.MAGENTA, MapColor.COLOR_MAGENTA, 12801229, 16711935, 0),
    LIGHT_BLUE(3, "light_blue", 3847130, DyeColor.LIGHT_BLUE, MapColor.COLOR_LIGHT_BLUE, 6719955, 10141901, 0),
    YELLOW(4, "yellow", 16701501, DyeColor.YELLOW, MapColor.COLOR_YELLOW, 14602026, 16776960, 0),
    LIME(5, "lime", 8439583, DyeColor.LIME, MapColor.COLOR_LIGHT_GREEN, 4312372, 12582656, 0),
    PINK(6, "pink", 15961002, DyeColor.PINK, MapColor.COLOR_PINK, 14188952, 16738740, 0),
    GRAY(7, "gray", 4673362, DyeColor.GRAY, MapColor.COLOR_GRAY, 4408131, 8421504, 0),
    LIGHT_GRAY(8, "light_gray", 10329495, DyeColor.LIGHT_GRAY, MapColor.COLOR_LIGHT_GRAY, 11250603, 13882323, 0),
    CYAN(9, "cyan", 1481884, DyeColor.CYAN, MapColor.COLOR_CYAN, 2651799, 65535, 0),
    PURPLE(10, "purple", 8991416, DyeColor.PURPLE, MapColor.COLOR_PURPLE, 8073150, 10494192, 0),
    BLUE(11, "blue", 3949738, DyeColor.BLUE, MapColor.COLOR_BLUE, 2437522, 255, 0),
    BROWN(12, "brown", 8606770, DyeColor.BROWN, MapColor.COLOR_BROWN, 5320730, 9127187, 0),
    GREEN(13, "green", 6192150, DyeColor.GREEN, MapColor.COLOR_GREEN, 3887386, 65280, 0),
    RED(14, "red", 11546150, DyeColor.RED, MapColor.COLOR_RED, 11743532, 16711680, 0),
    BLACK(15, "black", 1908001, DyeColor.BLACK, MapColor.COLOR_BLACK, 1973019, 0, 0),

    //Dyenamics Colors
    PEACH(16, "peach", 12556403, DyeColor.ORANGE, MapColor.SAND, 12556403, 11570292, 0),
    AQUAMARINE(17, "aquamarine", 2915711, DyeColor.CYAN, MapColor.TERRACOTTA_CYAN, 2915711, 2717304, 0),
    FLUORESCENT(18, "fluorescent", 15657398, DyeColor.YELLOW, MapColor.SAND, 15657398, 16117951, 15),
    MINT(19, "mint", 9038008, DyeColor.LIME, MapColor.COLOR_GREEN, 9038008, 7917475, 0),
    MAROON(20, "maroon", 9502720, DyeColor.RED, MapColor.COLOR_RED, 9502720, 9502720, 0),
    BUBBLEGUM(21, "bubblegum", 16216518, DyeColor.PINK, MapColor.COLOR_PINK, 16216518, 14574769, 0),
    LAVENDER(22, "lavender", 14522879, DyeColor.MAGENTA, MapColor.COLOR_PURPLE, 14522879, 13339371, 0),
    PERSIMMON(23, "persimmon", 13779225, DyeColor.ORANGE, MapColor.TERRACOTTA_ORANGE, 13779225, 12203795, 0),
    CHERENKOV(24, "cherenkov", 104655, DyeColor.LIGHT_BLUE, MapColor.COLOR_LIGHT_BLUE, 104655, 35002, 8);

    private static final DyenamicDyeColor[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(DyenamicDyeColor::getId)).toArray(DyenamicDyeColor[]::new);

    private static final Map<Integer, DyenamicDyeColor> BY_FIREWORK_COLOR = Arrays.stream(values()).collect(Collectors.toMap((color) -> color.fireworkColor, (color) -> color));

    private final int id;
    private final String translationKey;
    private final DyeColor analogue;
    private final MapColor mapColor;
    private final int colorValue;
    private final int lightValue;
    private final float[] colorComponentValues;
    private final int fireworkColor;
    private final TagKey<Item> tag;
    private final int textColor;

    DyenamicDyeColor(int idIn, String translationKeyIn, int colorValueIn, DyeColor analogueIn, MapColor mapColorIn, int fireworkColorIn, int textColorIn, int lightValueIn) {
        this.id = idIn;
        this.translationKey = translationKeyIn;
        this.colorValue = colorValueIn;
        this.analogue = analogueIn;
        this.mapColor = mapColorIn;
        this.textColor = textColorIn;
        this.lightValue = lightValueIn;
        int i = (colorValueIn & 16711680) >> 16;
        int j = (colorValueIn & '\uff00') >> 8;
        int k = colorValueIn & 255;
        this.tag = ItemTags.create(new ResourceLocation("forge", "dyes/" + translationKeyIn));
        this.colorComponentValues = new float[]{(float) i / 255.0F, (float) j / 255.0F, (float) k / 255.0F};
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

    public DyeColor getAnalogue() {
        return this.analogue;
    }

    public MapColor getMapColor() {
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
        for (DyenamicDyeColor DyenamicDyeColor : values()) {
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

    @Override
    public String toString() {
        return getTranslationKey();
    }

    public int getColorValue() {
        return colorValue;
    }

    public TagKey<Item> getTag() {
        return tag;
    }

    @Nullable
    public static DyenamicDyeColor getColor(ItemStack stack) {
        return getColor(stack.getItem());
    }

    @Nullable
    public static DyenamicDyeColor getColor(Item item) {
        if (item instanceof DyenamicDyeItem) {
            return ((DyenamicDyeItem) item).getDyeColor();
        }
        for (DyenamicDyeColor color : VALUES) {
            if (item.builtInRegistryHolder().is(color.getTag())) {
                return color;
            }
        }
        return null;
    }

    @Nullable
    public static DyenamicDyeColor getColor(DyeColor colorIn) {
        for (DyenamicDyeColor color : VALUES) {
            if (color.analogue.equals(colorIn)) {
                return color;
            }
        }
        return null;
    }

    @Override
    public String getSerializedName() {
        return toString();
    }
}
