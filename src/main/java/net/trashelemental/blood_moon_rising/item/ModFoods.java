package net.trashelemental.blood_moon_rising.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {

    public static final FoodProperties COOKED_HEART = new FoodProperties.Builder().
            nutrition(8).
            saturationModifier(1.6f).
            effect(new MobEffectInstance(MobEffects.HEAL, 1, 0), 1.0F).
            build();

    public static final FoodProperties CURED_HEART = new FoodProperties.Builder().
            nutrition(4).
            saturationModifier(2.4f).
            effect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 1.0F).
            build();

    public static final FoodProperties HEART_OF_GOLD = new FoodProperties.Builder().
            nutrition(8).
            saturationModifier(2.4f).
            effect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 0), 1.0F).
            alwaysEdible().
            build();

    public static final FoodProperties EXALTED_FLESH = new FoodProperties.Builder().
            nutrition(3).
            saturationModifier(0.6f).
            effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 1), 6.0F).
            effect(new MobEffectInstance(MobEffects.HUNGER, 300, 2), 6.0F).
            build();

    public static final FoodProperties COOKED_FLESH = new FoodProperties.Builder().
            nutrition(10).
            saturationModifier(1.6f).
            effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0), 1.0F).
            build();

    public static final FoodProperties CURED_FLESH = new FoodProperties.Builder().
            nutrition(4).
            saturationModifier(2.4f).
            effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 0), 1.0F).
            build();

    public static final FoodProperties JERKY = new FoodProperties.Builder().
            nutrition(4).
            saturationModifier(2.4f).
            build();

}
