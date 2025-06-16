package net.trashelemental.blood_moon_rising.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {

    public static final FoodProperties COOKED_HEART = new FoodProperties.Builder().
            nutrition(8).
            saturationMod(0.8f).
            effect(new MobEffectInstance(MobEffects.HEAL, 1, 0), 1.0F).
            meat().
            build();

    public static final FoodProperties CURED_HEART = new FoodProperties.Builder().
            nutrition(4).
            saturationMod(1.2f).
            effect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 1.0F).
            meat().
            build();

    public static final FoodProperties HEART_OF_GOLD = new FoodProperties.Builder().
            nutrition(8).
            saturationMod(1.2F).
            effect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 0), 1.0F).
            alwaysEat().
            build();

    public static final FoodProperties EXALTED_FLESH = new FoodProperties.Builder().
            nutrition(3).
            saturationMod(0.3f).
            effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 1), 6.0F).
            effect(new MobEffectInstance(MobEffects.HUNGER, 300, 2), 6.0F).
            meat().
            build();

    public static final FoodProperties COOKED_FLESH = new FoodProperties.Builder().
            nutrition(10).
            saturationMod(0.8f).
            effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0), 1.0F).
            meat().
            build();

    public static final FoodProperties CURED_FLESH = new FoodProperties.Builder().
            nutrition(4).
            saturationMod(1.2f).
            effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 0), 1.0F).
            meat().
            build();

    public static final FoodProperties JERKY = new FoodProperties.Builder().
            nutrition(4).
            saturationMod(1.2f).
            meat().
            build();

    public static final FoodProperties CONSECRATED_FLESH = new FoodProperties.Builder().
            nutrition(10).
            saturationMod(1.2f).
            alwaysEat().
            build();

}
