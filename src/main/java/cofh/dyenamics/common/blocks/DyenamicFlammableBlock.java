package cofh.dyenamics.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class DyenamicFlammableBlock extends Block {

	int encouragement;
	int flammability;
	
	public DyenamicFlammableBlock(Properties properties) {
		this(properties, 30, 60);
	}
	
	public DyenamicFlammableBlock(Properties properties, int encouragementIn, int flammabilityIn) {
		super(properties);
		this.encouragement = encouragementIn;
		this.flammability = flammabilityIn;
	}
	
	@Override
	public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return true;
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face)
    {
        return encouragement;
    }
	
	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face)
    {
        return flammability;
    }
}
