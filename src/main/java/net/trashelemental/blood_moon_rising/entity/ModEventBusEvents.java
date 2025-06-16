package net.trashelemental.blood_moon_rising.entity;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.LeechEntity;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.MosquitoEntity;

@Mod.EventBusSubscriber(modid = BloodMoonRising.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

        event.put(ModEntities.MORSEL.get(), MorselEntity.createAttributes().build());
        event.put(ModEntities.LEECH.get(), LeechEntity.createAttributes().build());
        event.put(ModEntities.MOSQUITO.get(), MosquitoEntity.createAttributes().build());

    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {



    }

;





}
