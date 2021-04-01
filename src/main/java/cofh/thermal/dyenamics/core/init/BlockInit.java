package cofh.thermal.dyenamics.core.init;

import cofh.thermal.dyenamics.ThermalDyenamics;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.GlazedTerracottaBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ThermalDyenamics.MOD_ID);

	//Terracotta
	public static final RegistryObject<Block> PEACH_TERRACOTTA = BLOCKS.register("peach_terracotta", () -> new Block(AbstractBlock.Properties.from(Blocks.TERRACOTTA)));
	public static final RegistryObject<Block> AQUAMARINE_TERRACOTTA = BLOCKS.register("aquamarine_terracotta", () -> new Block(AbstractBlock.Properties.from(Blocks.TERRACOTTA)));
	public static final RegistryObject<Block> FLUORESCENT_TERRACOTTA = BLOCKS.register("fluorescent_terracotta", () -> new Block(AbstractBlock.Properties.from(Blocks.TERRACOTTA)));
	
	//Glazed Terracotta
	public static final RegistryObject<Block> PEACH_GLAZED_TERRACOTTA = BLOCKS.register("peach_glazed_terracotta", () -> new GlazedTerracottaBlock(AbstractBlock.Properties.from(Blocks.WHITE_GLAZED_TERRACOTTA)));
	public static final RegistryObject<Block> AQUAMARINE_GLAZED_TERRACOTTA = BLOCKS.register("aquamarine_glazed_terracotta", () -> new GlazedTerracottaBlock(AbstractBlock.Properties.from(Blocks.WHITE_GLAZED_TERRACOTTA)));
	public static final RegistryObject<Block> FLUORESCENT_GLAZED_TERRACOTTA = BLOCKS.register("fluorescent_glazed_terracotta", () -> new GlazedTerracottaBlock(AbstractBlock.Properties.from(Blocks.WHITE_GLAZED_TERRACOTTA)));
	
	//Concrete
	public static final RegistryObject<Block> PEACH_CONCRETE = BLOCKS.register("peach_concrete", () -> new Block(AbstractBlock.Properties.from(Blocks.WHITE_CONCRETE)));
	public static final RegistryObject<Block> AQUAMARINE_CONCRETE = BLOCKS.register("aquamarine_concrete", () -> new Block(AbstractBlock.Properties.from(Blocks.WHITE_CONCRETE)));
	public static final RegistryObject<Block> FLUORESCENT_CONCRETE = BLOCKS.register("fluorescent_concrete", () -> new Block(AbstractBlock.Properties.from(Blocks.WHITE_CONCRETE)));
	
	//Concrete Powder
	public static final RegistryObject<Block> PEACH_CONCRETE_POWDER = BLOCKS.register("peach_concrete", () -> new ConcretePowderBlock(PEACH_CONCRETE.get(), AbstractBlock.Properties.from(Blocks.WHITE_CONCRETE_POWDER)));
	public static final RegistryObject<Block> AQUAMARINE_CONCRETE_POWDER = BLOCKS.register("aquamarine_concrete", () -> new ConcretePowderBlock(AQUAMARINE_CONCRETE.get(), AbstractBlock.Properties.from(Blocks.WHITE_CONCRETE_POWDER)));
	public static final RegistryObject<Block> FLUORESCENT_CONCRETE_POWDER = BLOCKS.register("fluorescent_concrete", () -> new ConcretePowderBlock(FLUORESCENT_CONCRETE.get(), AbstractBlock.Properties.from(Blocks.WHITE_CONCRETE_POWDER)));
	
	//Wool
	public static final RegistryObject<Block> PEACH_WOOL = BLOCKS.register("peach_wool", () -> new Block(AbstractBlock.Properties.from(Blocks.WHITE_WOOL)));
	public static final RegistryObject<Block> AQUAMARINE_WOOL = BLOCKS.register("aquamarine_wool", () -> new Block(AbstractBlock.Properties.from(Blocks.WHITE_WOOL)));
	public static final RegistryObject<Block> FLUORESCENT_WOOL = BLOCKS.register("fluorescent_wool", () -> new Block(AbstractBlock.Properties.from(Blocks.WHITE_WOOL)));
	
}
