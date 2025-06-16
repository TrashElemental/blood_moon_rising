package net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/**
 * Gives the player a level of Speed and Haste whenever they are attacked.
 */
public class FranticHeartEffect extends AbstractHeartEffect {

    public FranticHeartEffect() {
        super("frantic", -2);
    }

    @Override
    public void onHurt(Player player, LivingEntity attacker, DamageSource source, float amount) {
        if (!player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0, false, true));
        }
        if (!player.hasEffect(MobEffects.DIG_SPEED)) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 100, 0, false, true));
        }
    }
}
