package net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;

import java.util.UUID;

/**
 * Immunity to the poison and corrosion effects (handled in HeartEventHandler). Attacks inflict poison on enemies.
 * Taking damage causes the player to receive the Leaking effect.
 */
public class TaintedHeartEffect extends AbstractHeartEffect {

    public TaintedHeartEffect() {
        super("tainted", -2);
    }

    @Override
    public void onDamage(Player player, LivingEntity target, float amount) {
        if (!target.hasEffect(MobEffects.POISON)) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0, false, true));
        }
        super.onDamage(player, target, amount);
    }

    @Override
    public void onHurt(Player player, LivingEntity attacker, DamageSource source, float amount) {
        player.addEffect(new MobEffectInstance(ModMobEffects.LEAKING, 100, 0, false, true));
        super.onHurt(player, attacker, source, amount);
    }
}
