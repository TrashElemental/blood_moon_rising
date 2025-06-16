package net.trashelemental.blood_moon_rising.capabilities.hearts;

import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects.*;
import net.trashelemental.blood_moon_rising.item.ModItems;

public class HeartEffectsRegistry {

    public static void register() {
        HeartEffects.register(BloodMoonRising.prefix("astral_heart"), ModItems.ASTRAL_HEART.get(), new AstralHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix("broken_heart"), ModItems.BROKEN_HEART.get(), new BrokenHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix("dividing_heart"), ModItems.DIVIDING_HEART.get(), new DividingHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix("echoing_heart"), ModItems.ECHOING_HEART.get(), new EchoingHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix("elusive_heart"), ModItems.ELUSIVE_HEART.get(), new ElusiveHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix("feral_heart"), ModItems.FERAL_HEART.get(), new FeralHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix("frantic_heart"), ModItems.FRANTIC_HEART.get(), new FranticHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix("frozen_heart"), ModItems.FROZEN_HEART.get(), new FrozenHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix("heavy_heart"), ModItems.HEAVY_HEART.get(), new HeavyHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix("hungry_heart"), ModItems.HUNGRY_HEART.get(), new HungryHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix( "scorched_heart"), ModItems.SCORCHED_HEART.get(), new ScorchedHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix( "scorched_heart"), ModItems.SELFLESS_HEART.get(), new SelflessHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix( "spiteful_heart"), ModItems.SPITEFUL_HEART.get(), new SpitefulHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix( "tainted_heart"), ModItems.TAINTED_HEART.get(), new TaintedHeartEffect());
        HeartEffects.register(BloodMoonRising.prefix("wrathful_heart"), ModItems.WRATHFUL_HEART.get(), new WrathfulHeartEffect());

    }
}