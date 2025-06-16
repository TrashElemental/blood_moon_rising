package net.trashelemental.blood_moon_rising.blood_moon.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.trashelemental.blood_moon_rising.blood_moon.BloodMoonClientEvents;

import java.util.function.Supplier;

public class BloodMoonStatusPacket {
    private final boolean isActive;

    public BloodMoonStatusPacket(boolean isActive) {
        this.isActive = isActive;
    }

    public static void encode(BloodMoonStatusPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.isActive);
    }

    public static BloodMoonStatusPacket decode(FriendlyByteBuf buf) {
        return new BloodMoonStatusPacket(buf.readBoolean());
    }

    public static void handle(BloodMoonStatusPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> BloodMoonClientEvents.setBloodMoonActive(msg.isActive));
        ctx.get().setPacketHandled(true);
    }
}
