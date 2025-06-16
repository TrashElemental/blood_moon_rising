package net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/**
 * Causes the attacker to take damage whenever the player is damaged.
 */
public class WrathfulHeartEffect extends AbstractHeartEffect {


    public WrathfulHeartEffect() {
        super("wrathful", -2);
    }

    @Override
    public void onKilledEnemy(Player player, LivingEntity killed) {

        if (!player.hasEffect(MobEffects.DAMAGE_BOOST)) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0, false, true));
        }
        if (!player.hasEffect(MobEffects.DIG_SPEED)) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 100, 0, false, true));
        }

        super.onKilledEnemy(player, killed);
    }
}
