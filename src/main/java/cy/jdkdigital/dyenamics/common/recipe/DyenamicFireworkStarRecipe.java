package cy.jdkdigital.dyenamics.common.recipe;

import com.google.common.collect.Maps;
import cy.jdkdigital.dyenamics.common.item.DyenamicDyeItem;
import cy.jdkdigital.dyenamics.core.init.RecipeSerializerInit;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

import java.util.Map;

public class DyenamicFireworkStarRecipe extends CustomRecipe
{
    private static final Ingredient SHAPE_INGREDIENT = Ingredient.of(Items.FIRE_CHARGE, Items.FEATHER, Items.GOLD_NUGGET, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.CREEPER_HEAD, Items.PLAYER_HEAD, Items.DRAGON_HEAD, Items.ZOMBIE_HEAD, Items.PIGLIN_HEAD);
    private static final Map<Item, FireworkExplosion.Shape> SHAPE_BY_ITEM = Util.make(Maps.newHashMap(), (map) -> {
        map.put(Items.FIRE_CHARGE, FireworkExplosion.Shape.LARGE_BALL);
        map.put(Items.FEATHER, FireworkExplosion.Shape.BURST);
        map.put(Items.GOLD_NUGGET, FireworkExplosion.Shape.STAR);
        map.put(Items.SKELETON_SKULL, FireworkExplosion.Shape.CREEPER);
        map.put(Items.WITHER_SKELETON_SKULL, FireworkExplosion.Shape.CREEPER);
        map.put(Items.CREEPER_HEAD, FireworkExplosion.Shape.CREEPER);
        map.put(Items.PLAYER_HEAD, FireworkExplosion.Shape.CREEPER);
        map.put(Items.DRAGON_HEAD, FireworkExplosion.Shape.CREEPER);
        map.put(Items.ZOMBIE_HEAD, FireworkExplosion.Shape.CREEPER);
        map.put(Items.PIGLIN_HEAD, FireworkExplosion.Shape.CREEPER);
    });

    public DyenamicFireworkStarRecipe(CraftingBookCategory pCategory) {
        super(pCategory);
    }

    @Override
    public boolean matches(CraftingInput pInv, Level pLevel) {
        boolean hasShapeIngredient = false;
        boolean hasFlickerIngredient = false;
        boolean hasTrailIngredient = false;
        boolean hasGunpowder = false;
        boolean hasDye = false;

        for (int i = 0; i < pInv.size(); ++i) {
            ItemStack itemstack = pInv.getItem(i);
            if (!itemstack.isEmpty()) {
                if (SHAPE_INGREDIENT.test(itemstack)) {
                    if (hasShapeIngredient) {
                        return false;
                    }

                    hasShapeIngredient = true;
                } else if (itemstack.is(Tags.Items.DUSTS_GLOWSTONE)) {
                    if (hasFlickerIngredient) {
                        return false;
                    }

                    hasFlickerIngredient = true;
                } else if (itemstack.is(Tags.Items.GEMS_DIAMOND)) {
                    if (hasTrailIngredient) {
                        return false;
                    }

                    hasTrailIngredient = true;
                } else if (itemstack.is(Tags.Items.GUNPOWDERS)) {
                    if (hasGunpowder) {
                        return false;
                    }

                    hasGunpowder = true;
                } else {
                    if (!(itemstack.is(Tags.Items.DYES))) {
                        return false;
                    }

                    hasDye = true;
                }
            }
        }

        return hasGunpowder && hasDye;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider p_335498_) {
        FireworkExplosion.Shape fireworkexplosion$shape = FireworkExplosion.Shape.SMALL_BALL;
        boolean flag = false;
        boolean flag1 = false;
        IntList intlist = new IntArrayList();

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack itemstack = craftingInput.getItem(i);
            if (!itemstack.isEmpty()) {
                if (SHAPE_INGREDIENT.test(itemstack)) {
                    fireworkexplosion$shape = SHAPE_BY_ITEM.get(itemstack.getItem());
                } else if (itemstack.is(Tags.Items.DUSTS_GLOWSTONE)) {
                    flag = true;
                } else if (itemstack.is(Tags.Items.GEMS_DIAMOND)) {
                    flag1 = true;
                } else if (itemstack.getItem() instanceof DyenamicDyeItem dyeItem) {
                    intlist.add(dyeItem.getDyeColor().getFireworkColor());
                } else if (itemstack.getItem() instanceof DyeItem dyeItem) {
                    intlist.add(dyeItem.getDyeColor().getFireworkColor());
                }
            }
        }

        ItemStack itemstack1 = new ItemStack(Items.FIREWORK_STAR);
        itemstack1.set(DataComponents.FIREWORK_EXPLOSION, new FireworkExplosion(fireworkexplosion$shape, intlist, IntList.of(), flag1, flag));
        return itemstack1;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return new ItemStack(Items.FIREWORK_STAR);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.FIREWORK_STAR.get();
    }
}