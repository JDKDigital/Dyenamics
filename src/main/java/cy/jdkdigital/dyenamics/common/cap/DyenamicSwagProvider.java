package cy.jdkdigital.dyenamics.common.cap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.INBTSerializable;

public interface DyenamicSwagProvider extends INBTSerializable<CompoundTag>
{
    int getSwagId();

    void sync(Entity entity);

    void setSwag(int id);

    void removeSwag();
}
