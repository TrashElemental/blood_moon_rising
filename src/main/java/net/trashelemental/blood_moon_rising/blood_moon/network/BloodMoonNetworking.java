package net.trashelemental.blood_moon_rising.blood_moon.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

public class BloodMoonNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(BloodMoonRising.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;
    private static int nextId() {
        return packetId++;
    }

    public static void register() {
        CHANNEL.registerMessage(
                nextId(),
                BloodMoonStatusPacket.class,
                BloodMoonStatusPacket::encode,
                BloodMoonStatusPacket::decode,
                BloodMoonStatusPacket::handle
        );
    }
}