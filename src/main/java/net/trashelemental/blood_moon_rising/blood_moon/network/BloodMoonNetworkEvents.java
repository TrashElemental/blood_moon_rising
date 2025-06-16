package net.trashelemental.blood_moon_rising.blood_moon.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

@EventBusSubscriber(modid = BloodMoonRising.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class BloodMoonNetworkEvents {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0.0");

        registrar.playToClient(
                BloodMoonSyncPayload.TYPE,
                BloodMoonSyncPayload.STREAM_CODEC,
                BloodMoonNetwork::handleSyncPayloadOnClient
        );
    }
}
