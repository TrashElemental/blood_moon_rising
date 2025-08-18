package net.trashelemental.blood_moon_rising.entity.event;

import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.ArteryEntity;

@EventBusSubscriber
public class ArteryEvents {

    @SubscribeEvent
    public static void preventKnockback(LivingKnockBackEvent event) {
        if (!(event.getEntity() instanceof ArteryEntity artery)) return;

        if (artery.isMimicking() || artery.isTransforming()) {
            event.setCanceled(true);
        }
    }

    //Whenever we get blood particles, add them in here.
    public static void ArteryTransformVFX(ArteryEntity entity) {
        //Enraged
        entity.playSound(SoundEvents.SHULKER_AMBIENT, 2, 0.2f);
        //Legs Erupting
        BloodMoonRising.queueServerWork(4, () ->
                entity.playSound(SoundEvents.SKELETON_HORSE_HURT, 3, 0.4f)
        );
        //Legs hit ground
        BloodMoonRising.queueServerWork(10, () ->
                entity.playSound(SoundEvents.PIGLIN_BRUTE_STEP, 3, 0.6f)
        );
        //Body Erupting
        BloodMoonRising.queueServerWork(20, () ->
                entity.playSound(SoundEvents.NETHER_WART_BREAK, 3, 0.1f)
        );
        //Neck Erupting
        BloodMoonRising.queueServerWork(30, () ->
                entity.playSound(SoundEvents.NETHER_WART_BREAK, 3, 0.2f)
        );
        //Head rights itself
        BloodMoonRising.queueServerWork(37, () ->
                entity.playSound(SoundEvents.FROGSPAWN_HATCH, 3, 0.6f)
        );
        //End

    }
}
