package net.trashelemental.blood_moon_rising.magic.effects.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ExhaustionEffect extends MobEffect {
    public ExhaustionEffect() {
        super(MobEffectCategory.HARMFUL, 9264671);

        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                "3aaea4cb-d524-4bc2-b25e-9b3a45801e8d",
                -6.0D,
                AttributeModifier.Operation.ADDITION
        );
        this.addAttributeModifier(
                Attributes.ATTACK_SPEED,
                "d174f07e-5b23-4210-94f6-4ebd740a7789",
                -0.2D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "30c51d61-2a08-4fc4-9c7d-7fd8aab0438a",
                -0.3D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    public ResourceLocation getIcon() {
        return new ResourceLocation("blood_moon_rising", "textures/mob_effect/exhaustion.png");
    }

}
