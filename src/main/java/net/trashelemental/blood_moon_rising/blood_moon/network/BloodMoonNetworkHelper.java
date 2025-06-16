package net.trashelemental.blood_moon_rising.blood_moon.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class BloodMoonNetworkHelper {

    public static void syncToAll(boolean isActive) {
        PacketDistributor.sendToAllPlayers(new BloodMoonSyncPayload(isActive));
    }

    public static void syncTo(ServerPlayer player, boolean isActive) {
        PacketDistributor.sendToPlayer(player, new BloodMoonSyncPayload(isActive));
    }
}
