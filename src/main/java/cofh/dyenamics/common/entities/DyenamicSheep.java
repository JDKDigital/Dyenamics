package cofh.dyenamics.common.entities;

import cofh.dyenamics.core.init.BlockInit;
import cofh.dyenamics.core.init.EntityInit;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class DyenamicSheep extends Sheep
{
    private static final EntityDataAccessor<Byte> DATA_WOOL_ID = SynchedEntityData.defineId(DyenamicSheep.class, EntityDataSerializers.BYTE);
    protected static final Map<DyenamicDyeColor, ItemLike> WOOL_BY_COLOR = Util.make(Maps.newEnumMap(DyenamicDyeColor.class), (map) -> {
        for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
            map.put(color, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get());
        }
    });
    private static final Map<DyeColor, ItemLike> VANILLA_WOOL_BY_COLOR = Util.make(Maps.newEnumMap(DyeColor.class), (map) -> {
        map.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
        map.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
        map.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
        map.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
        map.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
        map.put(DyeColor.LIME, Blocks.LIME_WOOL);
        map.put(DyeColor.PINK, Blocks.PINK_WOOL);
        map.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
        map.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
        map.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
        map.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
        map.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
        map.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
        map.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
        map.put(DyeColor.RED, Blocks.RED_WOOL);
        map.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
    });

    /**
     * Map from EnumDyenamicDyeColor to RGB values for passage to GlStateManager.color()
     */
    protected static final Map<DyenamicDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(Arrays.stream(DyenamicDyeColor.values()).collect(Collectors.toMap((DyenamicDyeColor color) -> color, DyenamicSheep::createSheepColor)));

    protected static float[] createSheepColor(DyenamicDyeColor dyeColorIn) {
        if (dyeColorIn == DyenamicDyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            float[] afloat = dyeColorIn.getColorComponentValues();
            float f = 0.75F;
            return new float[]{afloat[0] * f, afloat[1] * f, afloat[2] * f};
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static float[] getDyeRgb(DyenamicDyeColor dyeColor) {
        return DYE_TO_RGB.get(dyeColor);
    }

    public DyenamicSheep(EntityType<? extends DyenamicSheep> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D, Sheep.class));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_WOOL_ID, (byte)0);
    }

    public static void convertToVanilla(DyenamicSheep oldSheep, DyeColor color) {
        Level level = oldSheep.level;
        if (!level.isClientSide) {
            Sheep sheep = new Sheep(EntityType.SHEEP, level);
            sheep.setColor(color);
            convertSheep(oldSheep, sheep);
        }
    }

    public static void convertToDyenamics(Sheep oldSheep, DyenamicDyeColor color) {
        Level level = oldSheep.level;
        if (!level.isClientSide) {
            DyenamicSheep sheep = new DyenamicSheep(EntityInit.SHEEP.get(), level);
            sheep.setColor(color);
            convertSheep(oldSheep, sheep);
        }
    }

    private static void convertSheep(Sheep oldSheep, Sheep sheep) {
        Level level = oldSheep.level;
        if (!level.isClientSide) {
            oldSheep.remove(RemovalReason.DISCARDED);
            sheep.copyPosition(oldSheep);
            sheep.setAge(oldSheep.getAge());
            level.addFreshEntity(sheep);
        }
    }

    @Override
    public ResourceLocation getDefaultLootTable() {
        if (this.isSheared()) {
            return this.getType().getDefaultLootTable();
        }
        return EntityInit.SHEEP_LOOT.get(this.getDyenamicColor().getTranslationKey());
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getItem() instanceof DyeItem) {
            if (!this.level.isClientSide) {
                DyenamicSheep.convertToVanilla(this, ((DyeItem) itemstack.getItem()).getDyeColor());
                itemstack.shrink(1);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else if (itemstack.getItem().equals(Items.SHEEP_SPAWN_EGG)) {
            if (!this.level.isClientSide) {
                DyenamicSheep sheep = new DyenamicSheep(EntityInit.SHEEP.get(), this.level);
                sheep.copyPosition(this);
                sheep.setBaby(true);
                sheep.setColor(this.getDyenamicColor());
                this.level.addFreshEntity(sheep);
                itemstack.shrink(1);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public void shear(SoundSource category) {
        this.level.playSound(null, this, SoundEvents.SHEEP_SHEAR, category, 1.0F, 1.0F);
        this.setSheared(true);
        int i = 1 + this.random.nextInt(3);

        DyenamicDyeColor color = this.getDyenamicColor();

        for (int j = 0; j < i; ++j) {
            ItemEntity itementity;
            if (color.getId() > 15) {
                itementity = this.spawnAtLocation(WOOL_BY_COLOR.get(this.getDyenamicColor()), 1);
            } else {
                itementity = this.spawnAtLocation(VANILLA_WOOL_BY_COLOR.get(color.getVanillaColor()), 1);
            }
            if (itementity != null) {
                itementity.setDeltaMovement(itementity.getDeltaMovement().add((double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double) (this.random.nextFloat() * 0.05F), (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("Color", (byte) this.getDyenamicColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setColor(DyenamicDyeColor.byId(compound.getByte("Color")));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public DyenamicDyeColor getDyenamicColor() {
        return DyenamicDyeColor.byId(this.entityData.get(DATA_WOOL_ID) & 127);
    }

    @Override
    public void setColor(DyeColor color) {
        byte b0 = this.entityData.get(DATA_WOOL_ID);
        this.entityData.set(DATA_WOOL_ID, (byte) (b0 & -128 | color.getId() & 15));
    }

    public void setColor(DyenamicDyeColor color) {
        byte b0 = this.entityData.get(DATA_WOOL_ID);
        this.entityData.set(DATA_WOOL_ID, (byte) (b0 & -128 | (color.getId() & 127)));
    }

    @Override
    public boolean isSheared() {
        return this.entityData.get(DATA_WOOL_ID) < 0;
    }

    @Override
    public void setSheared(boolean sheared) {
        byte b0 = this.entityData.get(DATA_WOOL_ID);
        if (sheared) {
            this.entityData.set(DATA_WOOL_ID, (byte) (b0 | -128));
        } else {
            this.entityData.set(DATA_WOOL_ID, (byte) (b0 & 127));
        }
    }

    @Override
    public Sheep getBreedOffspring(ServerLevel world, AgeableMob mate) {
        if (mate instanceof DyenamicSheep parent) {
            DyenamicSheep child = EntityInit.SHEEP.get().create(world);
            child.setColor(this.getDyeColorMixFromParents(this, parent));
            return child;
        } else {
            Sheep parent = (Sheep) mate;
            DyenamicDyeColor color = this.getDyeColorMixFromParents(this, parent);
            if (color.getId() < 16) {
                Sheep child = EntityType.SHEEP.create(world);
                child.setColor(color.getAnalogue());
                return child;
            } else {
                DyenamicSheep child = EntityInit.SHEEP.get().create(world);
                child.setColor(color);
                return child;
            }

        }
    }

    @Override
    public boolean canMate(Animal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (otherAnimal instanceof Sheep) {
            return this.isInLove() && otherAnimal.isInLove();
        } else {
            return false;
        }
    }

    /**
     * Attempts to mix both parent sheep to come up with a mixed dye color.
     */
    protected DyenamicDyeColor getDyeColorMixFromParents(Sheep father, DyenamicSheep mother) {
        return this.level.random.nextBoolean() ? DyenamicDyeColor.byId(father.getColor().getId()) : mother.getDyenamicColor();
    }

    protected DyenamicDyeColor getDyeColorMixFromParents(DyenamicSheep mother, Sheep father) {
        return this.level.random.nextBoolean() ? DyenamicDyeColor.byId(father.getColor().getId()) : mother.getDyenamicColor();
    }

    protected DyenamicDyeColor getDyeColorMixFromParents(DyenamicSheep father, DyenamicSheep mother) {
        return this.level.random.nextBoolean() ? father.getDyenamicColor() : mother.getDyenamicColor();
    }

    @Override
    public List<ItemStack> onSheared(@Nullable Player player, ItemStack item, Level level, BlockPos pos, int fortune) {
        level.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
        if (!this.level.isClientSide) {
            this.setSheared(true);
            int i = 1 + this.random.nextInt(3);

            DyenamicDyeColor color = this.getDyenamicColor();
            ItemStack stack;
            if (color.getId() > 15) {
                stack = new ItemStack(WOOL_BY_COLOR.get(color));
            } else {
                stack = new ItemStack(VANILLA_WOOL_BY_COLOR.get(color.getVanillaColor()));
            }
            List<ItemStack> items = new ArrayList<>();
            for (int j = 0; j < i; ++j) {
                items.add(stack);
            }
            return items;
        }
        return Collections.emptyList();
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ForgeSpawnEggItem.fromEntityType(EntityType.SHEEP));
    }
}

