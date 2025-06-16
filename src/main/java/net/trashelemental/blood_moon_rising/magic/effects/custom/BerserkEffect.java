package net.trashelemental.blood_moon_rising.magic.effects.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

public class BerserkEffect extends MobEffect {
    public BerserkEffect(MobEffectCategory category, int color) {
        super(category, color);

        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                BloodMoonRising.prefix( "berserk_attack_damage"),
                6.0D,
                AttributeModifier.Operation.ADD_VALUE
        );
        this.addAttributeModifier(
                Attributes.ATTACK_SPEED,
                BloodMoonRising.prefix("berserk_attack_speed"),
                0.1D,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                BloodMoonRising.prefix( "berserk_movement_speed"),
                0.4D,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

    }

    public ResourceLocation getIcon() {
        return BloodMoonRising.prefix( "textures/mob_effect/berserk.png");
    }
}
