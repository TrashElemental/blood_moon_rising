package net.trashelemental.blood_moon_rising.magic.effects.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BerserkEffect extends MobEffect {
    public BerserkEffect() {
        super(MobEffectCategory.BENEFICIAL, 14588946);

        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                "3aaea4cb-d524-4bc2-b25e-9b3a45801e7d",
                6.0D,
                AttributeModifier.Operation.ADDITION
        );
        this.addAttributeModifier(
                Attributes.ATTACK_SPEED,
                "d174f07e-5b23-4210-94f6-4ebd740a7759",
                0.1D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "30c51d61-2a08-4fc4-9c7d-7fd8aab0437a",
                0.4D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    public ResourceLocation getIcon() {
        return new ResourceLocation("blood_moon_rising", "textures/mob_effect/berserk.png");
    }
}
