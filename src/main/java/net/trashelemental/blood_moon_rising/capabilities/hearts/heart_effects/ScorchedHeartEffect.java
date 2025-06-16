package net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.capabilities.hearts.IHeartEffect;

/**
 * Gives fire immunity, causes attackers to be ignited, attacks may ignite enemies.
 */
public class ScorchedHeartEffect extends AbstractHeartEffect {

    public ScorchedHeartEffect() {
        super("scorched", -2);
    }

    @Override
    public void onHurt(Player player, LivingEntity target, DamageSource source, float amount) {
        target.igniteForSeconds(3);
        super.onHurt(player, target, source, amount);
    }

    @Override
    public void onDamage(Player player, LivingEntity target, float amount) {
        if (player.getRandom().nextFloat() < 0.5f) {
            target.igniteForSeconds(3);
        }
        super.onDamage(player, target, amount);
    }

    @Override
    public void onTick(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0, false, false));
        super.onTick(player);
    }
}
