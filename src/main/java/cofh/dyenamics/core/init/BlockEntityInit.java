package cofh.dyenamics.core.init;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.blockentity.DyenamicBedBlockEntity;
import cofh.dyenamics.common.blockentity.DyenamicShulkerBoxBlockEntity;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Dyenamics.MOD_ID);

    public static final RegistryObject<BlockEntityType<DyenamicBedBlockEntity>> BED = BLOCK_ENTITY_TYPES.register("bed",
            () -> BlockEntityType.Builder.of(
                    DyenamicBedBlockEntity::new,
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.PEACH.getSerializedName()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.AQUAMARINE.getSerializedName()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.FLUORESCENT.getSerializedName()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.MINT.getSerializedName()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.MAROON.getSerializedName()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.BUBBLEGUM.getSerializedName()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.LAVENDER.getSerializedName()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.PERSIMMON.getSerializedName()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.CHERENKOV.getSerializedName()).get("bed").get()
            ).build(null));

    public static final RegistryObject<BlockEntityType<DyenamicShulkerBoxBlockEntity>> SHULKER_BOX = BLOCK_ENTITY_TYPES.register("shulker_box",
            () -> BlockEntityType.Builder.of(
                    DyenamicShulkerBoxBlockEntity::new,
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.PEACH.getSerializedName()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.AQUAMARINE.getSerializedName()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.FLUORESCENT.getSerializedName()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.MINT.getSerializedName()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.MAROON.getSerializedName()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.BUBBLEGUM.getSerializedName()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.LAVENDER.getSerializedName()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.PERSIMMON.getSerializedName()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.CHERENKOV.getSerializedName()).get("shulker_box").get()
            ).build(null));
}
