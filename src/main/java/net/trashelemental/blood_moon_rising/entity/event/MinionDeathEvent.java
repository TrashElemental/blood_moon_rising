package net.trashelemental.blood_moon_rising.entity.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.LeechEntity;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.MosquitoEntity;

import javax.annotation.Nullable;

//Prevents minions from sending death messages
@Mod.EventBusSubscriber
public class MinionDeathEvent {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event != null && event.getEntity() != null) {
            execute(event, event.getEntity().level(), event.getEntity());
        }
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null || world == null) {
            return;
        }

        if (entity instanceof MorselEntity || entity instanceof MosquitoEntity || entity instanceof LeechEntity) {

            TamableAnimal minion = (TamableAnimal) entity;

            if (minion.isTame()) {
                minion.setOwnerUUID(null);
            }
        }
    }


}
