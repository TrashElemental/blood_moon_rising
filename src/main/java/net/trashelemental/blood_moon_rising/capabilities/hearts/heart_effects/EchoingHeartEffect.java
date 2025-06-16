package net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;

import java.util.UUID;

/**
 * After the player damages a target, the damage will 'echo' two seconds later, dealing half damage.
 */
public class EchoingHeartEffect extends AbstractHeartEffect {

    public EchoingHeartEffect() {
        super("echoing", -2);
    }

    @Override
    public void onDamage(Player player, LivingEntity target, float amount) {
        float echoDamage = Math.max((amount / 2), 1);

        BloodMoonRising.queueServerWork(40, () -> {
            if (target.isAlive()) {
                UtilMethods.damageEntity(target, DamageTypes.PLAYER_ATTACK, echoDamage);
            }
        });
    }
}
