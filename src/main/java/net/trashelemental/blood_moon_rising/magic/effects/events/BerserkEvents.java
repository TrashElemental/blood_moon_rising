package net.trashelemental.blood_moon_rising.magic.effects.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;

/**
 * When the player kills an entity while they have the Berserk
 * effect, the duration of the effect will be extended. When the
 * Berserk effect runs out and isn't extended, the player is given
 * the exhaustion effect.
 */

@EventBusSubscriber
public class BerserkEvents {

    @SubscribeEvent
    public static void extendBerserk(LivingDeathEvent event) {

        int maxBerserkDuration = 240;
        int berserkBonusDuration = 60;

        Entity entity = event.getSource().getEntity();
        if (!(entity instanceof ServerPlayer player)) return;
        if (!player.hasEffect(ModMobEffects.BERSERK)) return;

        MobEffectInstance berserk = player.getEffect(ModMobEffects.BERSERK);
        if (berserk == null) return;

        int newDuration = Math.min(berserk.getDuration() + berserkBonusDuration, maxBerserkDuration);

        player.addEffect(new MobEffectInstance(
                ModMobEffects.BERSERK,
                newDuration,
                berserk.getAmplifier(),
                berserk.isAmbient(),
                berserk.isVisible(),
                berserk.showIcon()
        ));
    }


    @SubscribeEvent
    public static void onBerserkExpired(MobEffectEvent.Expired event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getEffectInstance().getEffect() != ModMobEffects.BERSERK) return;

        if (!player.hasEffect(ModMobEffects.EXHAUSTION)) {
            player.addEffect(new MobEffectInstance(ModMobEffects.EXHAUSTION, 100, 0, false, true));

            Level level = player.level();
            level.playSound(null, player.blockPosition(), SoundEvents.ZOMBIE_INFECT, SoundSource.PLAYERS, 1f, 1f);
        }
    }
}
