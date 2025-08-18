package net.trashelemental.blood_moon_rising.junkyard_lib.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class UtilMethods {

    /**
     * Easy method for damaging entities without having to create a holder in the class.
     */
    public static void damageEntity(LivingEntity target, ResourceKey<DamageType> damageType, float amount) {
        if (target.level().isClientSide) return;
        Holder<DamageType> holder = target.level().registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(damageType);

        target.hurt(new DamageSource(holder), amount);
    }

    /**
     * Methods for applying and removing attribute modifiers to entities. You will
     * still need to create the modifier to reference it in this method.
     */
    public static void applyModifier(LivingEntity entity, Attribute attribute, AttributeModifier modifier) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null && !instance.hasModifier(modifier)) {
            instance.addPermanentModifier(modifier);
        }
    }

    public static void removeModifier(LivingEntity entity, Attribute attribute, UUID uuid) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(uuid);
        }
    }
}
