package net.trashelemental.blood_moon_rising.magic.effects.events;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;
import net.trashelemental.blood_moon_rising.util.ModTags;

import javax.annotation.Nullable;

@EventBusSubscriber
public class HemorrhageLogic {

    //If a target receives the hemorrhage effect, the game will check if it already has the effect.
    //If the target has hemorrhage 1, the game will clear it and reapply hemorrhage at level 2.
    //If the target has hemorrhage 2, the game will clear it and deal a chunk of damage to the target.
    //The damage dealt is 1/4 of the mob's max HP, or 15, whichever is lower.

    public static void applyHemorrhage(LivingEntity target, @Nullable Entity attacker, int duration) {

        MobEffectInstance hemorrhage = target.getEffect(ModMobEffects.HEMORRHAGE);

        if (!hemorrhageCanApply(target)) {
            return;
        }

        if (hemorrhage != null) {
            int currentLevel = hemorrhage.getAmplifier();

            if (currentLevel == 0) {
                target.removeEffect(ModMobEffects.HEMORRHAGE);
                target.addEffect(new MobEffectInstance(ModMobEffects.HEMORRHAGE, duration, 1));
            } else if (currentLevel == 1) {
                hemorrhageActivate(target, attacker);
            }
        } else {
            target.addEffect(new MobEffectInstance(ModMobEffects.HEMORRHAGE, duration, 0));
        }
    }

    public static void hemorrhageActivate(LivingEntity target, Entity attacker) {

        if (!hemorrhageCanApply(target)) {
            return;
        }

        float damageAmount = Math.min(target.getMaxHealth() * 0.25f, 15.0f);
        Holder<DamageType> damage = target.level().registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.GENERIC);

        target.removeEffect(ModMobEffects.HEMORRHAGE);
        target.hurt(new DamageSource(damage, attacker), damageAmount);
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2));

        target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.PLAYERS,
                0.4F, 0.6F
        );

        //Add blood particles later

    }

    public static boolean hemorrhageCanApply(LivingEntity target) {
        return !target.isOnFire()
                && !target.isInLava()
                && !target.getType().is(ModTags.EntityTags.HEMORRHAGE_IMMUNE);
    }

    @SubscribeEvent
    public static void clearHemorrhageOnFireDamage(LivingDamageEvent.Post event) {

        LivingEntity target = event.getEntity();
        DamageSource source = event.getSource();

        if (target.hasEffect(ModMobEffects.HEMORRHAGE) && isFireTypeDamage(source)) {
            target.removeEffect(ModMobEffects.HEMORRHAGE);
        }
    }

    public static boolean isFireTypeDamage(DamageSource source) {
        return source.is(DamageTypes.LAVA)
                || source.is(DamageTypes.IN_FIRE)
                || source.is(DamageTypes.ON_FIRE);
    }

}
