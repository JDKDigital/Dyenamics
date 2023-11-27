package cy.jdkdigital.dyenamics.common.cap;

import cy.jdkdigital.dyenamics.Dyenamics;
import cy.jdkdigital.dyenamics.common.network.PacketHandler;
import cy.jdkdigital.dyenamics.common.network.SwagPacket;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

public class DyenamicSwagImpl
{
    private static class DefaultImpl implements DyenamicSwagProvider
    {
        private final Entity entity;

        private int swag = -1;

        private DefaultImpl(Entity entity) {
            this.entity = entity;
        }

        @Override
        public int getSwagId() {
            return swag;
        }

        @Override
        public void sync(Entity entity) {
            PacketHandler.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new SwagPacket(entity.getId(), serializeNBT()));
        }

        @Override
        public void setSwag(int id) {
            swag = id;
            sync(entity);
        }

        @Override
        public void removeSwag() {
            swag = -1;
            sync(entity);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("swag", swag);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            swag = tag.getInt("swag");
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundTag>
    {
        public static final ResourceLocation NAME = new ResourceLocation(Dyenamics.MOD_ID, "dyenamic_swag");

        private final DefaultImpl impl;
        private final LazyOptional<DyenamicSwagProvider> cap;

        public Provider(Entity entity) {
            impl = new DefaultImpl(entity);
            cap = LazyOptional.of(() -> impl);
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
            if (capability == Dyenamics.DYENAMIC_SWAG) {
                return cap.cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return impl.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            impl.deserializeNBT(nbt);
        }
    }
}
