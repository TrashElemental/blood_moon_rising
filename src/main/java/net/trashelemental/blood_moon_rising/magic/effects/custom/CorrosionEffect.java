package net.trashelemental.blood_moon_rising.magic.effects.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

public class CorrosionEffect extends MobEffect {
    public CorrosionEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public ResourceLocation getIcon() {
        return BloodMoonRising.prefix( "textures/mob_effect/corrosion.png");
    }
}
