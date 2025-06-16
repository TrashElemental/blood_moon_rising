package net.trashelemental.blood_moon_rising.entity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.LeechEntity;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.MosquitoEntity;

@EventBusSubscriber(modid = BloodMoonRising.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEntityAttributeRegistry {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

        event.put(ModEntities.MORSEL.get(), MorselEntity.createAttributes().build());
        event.put(ModEntities.LEECH.get(), LeechEntity.createAttributes().build());
        event.put(ModEntities.MOSQUITO.get(), MosquitoEntity.createAttributes().build());


    }
}
