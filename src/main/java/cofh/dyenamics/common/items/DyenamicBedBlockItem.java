package cofh.dyenamics.common.items;

import cofh.dyenamics.client.render.item.DyenamicBedItemStackRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class DyenamicBedBlockItem extends BlockItem
{
    public DyenamicBedBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties()
        {
            final BlockEntityWithoutLevelRenderer myRenderer = new DyenamicBedItemStackRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer()
            {
                return myRenderer;
            }
        });
    }
}
