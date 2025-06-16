package net.trashelemental.blood_moon_rising.capabilities;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.trashelemental.blood_moon_rising.capabilities.hearts.HeartData;

import java.util.function.Supplier;

public class AttachmentsRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "blood_moon_rising");

    public static final Supplier<AttachmentType<HeartData>> HEART_DATA = ATTACHMENT_TYPES.register("heart_data",
                    () -> AttachmentType.serializable(HeartData::new).copyOnDeath().build());


    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
