package net.trashelemental.blood_moon_rising.blood_moon.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.trashelemental.blood_moon_rising.blood_moon.BloodMoonClientEvents;

public class BloodMoonNetwork {
    public static void handleSyncPayloadOnClient(BloodMoonSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            BloodMoonClientEvents.setBloodMoonActive(payload.isBloodMoon());
        });
    }
}
