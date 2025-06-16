package net.trashelemental.blood_moon_rising.magic.effects.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class HealthDownEffect extends MobEffect {
    public HealthDownEffect() {
        super(MobEffectCategory.HARMFUL, 0xA0A74E);

        this.addAttributeModifier(
                Attributes.MAX_HEALTH,
                "1cb317fc-9e20-4bb3-9e2e-baeab9961199",
                -4.0D,
                AttributeModifier.Operation.ADDITION
        );
    }

    public ResourceLocation getIcon() {
        return new ResourceLocation("blood_moon_rising", "textures/mob_effect/health_down.png");
    }

}
