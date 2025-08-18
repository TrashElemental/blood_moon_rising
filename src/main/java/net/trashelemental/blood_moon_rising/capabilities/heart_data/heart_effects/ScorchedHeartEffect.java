package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/**
 * Gives fire immunity, causes attackers to be ignited, attacks may ignite enemies.
 */
public class ScorchedHeartEffect extends AbstractHeartEffect {

    private static final UUID UUID = java.util.UUID.fromString("9a4c2b1c-bf84-4f20-bf18-e063b14a12ec");

    public ScorchedHeartEffect() {
        super(UUID, "Scorched", -2.0);
    }

    @Override
    public void onHurt(Player player, LivingEntity attacker, DamageSource source, float amount) {
        attacker.setSecondsOnFire(3);
        super.onHurt(player, attacker, source, amount);
    }

    @Override
    public void onDamage(Player player, LivingEntity target, float amount) {
        if (player.getRandom().nextFloat() < 0.5f) {
            target.setSecondsOnFire(3);
        }
        super.onDamage(player, target, amount);
    }

    @Override
    public void onTick(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0, false, false));
        super.onTick(player);
    }
}
