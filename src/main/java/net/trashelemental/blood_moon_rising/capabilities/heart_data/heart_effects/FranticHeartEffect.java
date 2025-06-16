package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

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

    private static final UUID UUID = java.util.UUID.fromString("9a4c2b1c-bf84-4f20-bf18-e063b14e12ed");

    public FranticHeartEffect() {
        super(UUID, "Frantic", -2.0);
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
