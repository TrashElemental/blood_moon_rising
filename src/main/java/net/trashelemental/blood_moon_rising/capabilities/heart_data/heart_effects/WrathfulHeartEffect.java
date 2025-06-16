package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.entity.event.MinionSpawnLogic;

import java.util.UUID;

/**
 * Causes the attacker to take damage whenever the player is damaged.
 */
public class WrathfulHeartEffect extends AbstractHeartEffect {

    private static final UUID UUID = java.util.UUID.fromString("9a4c2b1c-bf84-4f20-bf18-e063b14b12ed");

    public WrathfulHeartEffect() {
        super(UUID, "Wrathful", -2.0);
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
