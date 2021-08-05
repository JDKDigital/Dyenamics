package cofh.dyenamics.core.init;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.blockentity.DyenamicBedBlockEntity;
import cofh.dyenamics.common.blockentity.DyenamicShulkerBoxBlockEntity;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockEntityInit
{
    public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Dyenamics.MOD_ID);

    public static final RegistryObject<TileEntityType<DyenamicBedBlockEntity>> BED = BLOCK_ENTITY_TYPES.register("bed",
            () -> TileEntityType.Builder.create(
                    DyenamicBedBlockEntity::new,
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.PEACH.getString()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.AQUAMARINE.getString()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.FLUORESCENT.getString()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.MINT.getString()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.MAROON.getString()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.BUBBLEGUM.getString()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.LAVENDER.getString()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.PERSIMMON.getString()).get("bed").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.CHERENKOV.getString()).get("bed").get()
            ).build(null));

    public static final RegistryObject<TileEntityType<DyenamicShulkerBoxBlockEntity>> SHULKER_BOX = BLOCK_ENTITY_TYPES.register("shulker_box",
            () -> TileEntityType.Builder.create(
                    DyenamicShulkerBoxBlockEntity::new,
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.PEACH.getString()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.AQUAMARINE.getString()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.FLUORESCENT.getString()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.MINT.getString()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.MAROON.getString()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.BUBBLEGUM.getString()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.LAVENDER.getString()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.PERSIMMON.getString()).get("shulker_box").get(),
                    BlockInit.DYED_BLOCKS.get(DyenamicDyeColor.CHERENKOV.getString()).get("shulker_box").get()
            ).build(null));
}
