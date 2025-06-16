package net.trashelemental.blood_moon_rising.blood_moon.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

public record BloodMoonSyncPayload(boolean isBloodMoon) implements CustomPacketPayload {

    public static final Type<BloodMoonSyncPayload> TYPE =
            new Type<>(BloodMoonRising.prefix("sync_blood_moon"));

    public static final StreamCodec<ByteBuf, BloodMoonSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, BloodMoonSyncPayload::isBloodMoon,
                    BloodMoonSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
