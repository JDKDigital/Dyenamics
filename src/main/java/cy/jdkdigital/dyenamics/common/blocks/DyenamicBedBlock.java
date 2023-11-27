package cy.jdkdigital.dyenamics.common.blocks;

import cy.jdkdigital.dyenamics.common.blockentity.DyenamicBedBlockEntity;
import cy.jdkdigital.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DyenamicBedBlock extends BedBlock
{
    private final DyenamicDyeColor color;

    public DyenamicBedBlock(DyenamicDyeColor colorIn, Properties properties) {
        super(DyeColor.WHITE, properties);
        color = colorIn;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DyenamicBedBlockEntity(this.color, pPos, pState);
    }

    public DyenamicDyeColor getDyenamicColor() {
        return color;
    }
}
