package net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.capabilities.hearts.IHeartEffect;

/**
 * Causes the attacker to take damage whenever the player is damaged.
 */
public class SpitefulHeartEffect extends AbstractHeartEffect {

    public SpitefulHeartEffect() {
        super ("spiteful", -2);
    }

    @Override
    public void onHurt(Player player, LivingEntity target, DamageSource source, float amount) {

    Holder<DamageType> damageTypeHolder = player.level().registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.GENERIC);

        target.hurt(new DamageSource(damageTypeHolder), 2.0f);
    }
}
