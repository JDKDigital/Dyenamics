package cy.jdkdigital.dyenamics.common.network;

import cy.jdkdigital.dyenamics.Dyenamics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SwagPacket
{
    private final CompoundTag nbt;
    private final int entityID;

    public SwagPacket(int entityID, CompoundTag nbt)
    {
        this.nbt = nbt;
        this.entityID = entityID;
    }

    public static void encode(SwagPacket msg, FriendlyByteBuf buf)
    {
        buf.writeInt(msg.entityID);
        buf.writeNbt(msg.nbt);
    }

    public static SwagPacket decode(FriendlyByteBuf buf)
    {
        return new SwagPacket(buf.readInt(), buf.readNbt());
    }

    public static class Handler
    {
        public static void handle(final SwagPacket message, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() ->
            {
                ClientLevel world = Minecraft.getInstance().level;

                if (world != null) {
                    Entity entity = world.getEntity(message.entityID);

                    if (entity != null) {
                        entity.getCapability(Dyenamics.DYENAMIC_SWAG).ifPresent(swagProvider -> {
                            swagProvider.deserializeNBT(message.nbt);
                        });
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
