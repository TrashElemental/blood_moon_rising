package net.trashelemental.blood_moon_rising.junkyard_lib.entity.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.trashelemental.blood_moon_rising.junkyard_lib.entity.MinionEntity;

/**
 * Prevents minions from sending death messages when killed, since they're
 * designed to be summoned at low health and sometimes in large numbers.
 */

@EventBusSubscriber
public class MinionDeathEvent {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event != null && event.getEntity() != null) {

            if (event.getEntity() instanceof MinionEntity minionEntity && minionEntity.isTame()) {
                minionEntity.setOwnerUUID(null);
            }

        }
    }

}
