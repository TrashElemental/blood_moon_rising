package net.trashelemental.blood_moon_rising.magic.effects.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

public class HemorrhageEffect extends MobEffect {
    public HemorrhageEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public ResourceLocation getIcon() {
        return BloodMoonRising.prefix( "textures/mob_effect/hemorrhage.png");
    }

}
