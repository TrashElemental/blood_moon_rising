package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;
import net.trashelemental.blood_moon_rising.junkyard_lib.visual.particle.ParticleMethods;

import java.util.UUID;

/**
 * After the player damages a target, the damage will 'echo' two seconds later, dealing half damage.
 */
public class EchoingHeartEffect extends AbstractHeartEffect {

    private static final UUID UUID = java.util.UUID.fromString("9a4c2b1c-bf84-4f20-cf18-e063b14a12ec");

    public EchoingHeartEffect() {
        super(UUID, "Echoing", -2.0);
    }

    @Override
    public void onDamage(Player player, LivingEntity target, float amount) {
        float echoDamage = Math.max((amount / 2), 1);

        BloodMoonRising.queueServerWork(40, () -> {
            if (target.isAlive()) {

                UtilMethods.damageEntity(target, DamageTypes.PLAYER_ATTACK, echoDamage);

                ParticleMethods.ParticlesAroundServerSide(player.level(), ParticleTypes.SONIC_BOOM,
                        target.getX(), target.getY() + 1, target.getZ(), 1, 0.5);
                target.playSound(SoundEvents.WARDEN_SONIC_BOOM, 0.5f, 1.7f);

            }
        });
    }
}
