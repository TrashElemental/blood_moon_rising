package net.trashelemental.blood_moon_rising.magic.effects.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

public class HealthDownEffect extends MobEffect {
    public HealthDownEffect(MobEffectCategory category, int color) {
        super(category, color);

        this.addAttributeModifier(
                Attributes.MAX_HEALTH,
                BloodMoonRising.prefix( "health_down_health_reduction"),
                -4.0D,
                AttributeModifier.Operation.ADD_VALUE
        );

    }

    @Override
    public void removeAttributeModifiers(AttributeMap attributeMap) {
        super.removeAttributeModifiers(attributeMap);
    }

    public ResourceLocation getIcon() {
        return BloodMoonRising.prefix("textures/mob_effect/health_down.png");
    }
}
