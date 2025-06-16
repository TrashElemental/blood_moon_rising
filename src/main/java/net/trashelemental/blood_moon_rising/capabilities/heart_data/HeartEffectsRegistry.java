package net.trashelemental.blood_moon_rising.capabilities.heart_data;

import net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects.*;
import net.trashelemental.blood_moon_rising.item.ModItems;

public class HeartEffectsRegistry {

    public static void register() {
        HeartEffects.register(ModItems.ASTRAL_HEART.get(), new AstralHeartEffect());
        HeartEffects.register(ModItems.BROKEN_HEART.get(), new BrokenHeartEffect());
        HeartEffects.register(ModItems.DIVIDING_HEART.get(), new DividingHeartEffect());
        HeartEffects.register(ModItems.ECHOING_HEART.get(), new EchoingHeartEffect());
        HeartEffects.register(ModItems.ELUSIVE_HEART.get(), new ElusiveHeartEffect());
        HeartEffects.register(ModItems.FERAL_HEART.get(), new FeralHeartEffect());
        HeartEffects.register(ModItems.FRANTIC_HEART.get(), new FranticHeartEffect());
        HeartEffects.register(ModItems.FROZEN_HEART.get(), new FrozenHeartEffect());
        HeartEffects.register(ModItems.HEAVY_HEART.get(), new HeavyHeartEffect());
        HeartEffects.register(ModItems.HUNGRY_HEART.get(), new HungryHeartEffect());
        HeartEffects.register(ModItems.SCORCHED_HEART.get(), new ScorchedHeartEffect());
        HeartEffects.register(ModItems.SELFLESS_HEART.get(), new SelflessHeartEffect());
        HeartEffects.register(ModItems.SPITEFUL_HEART.get(), new SpitefulHeartEffect());
        HeartEffects.register(ModItems.TAINTED_HEART.get(), new TaintedHeartEffect());
        HeartEffects.register(ModItems.WRATHFUL_HEART.get(), new WrathfulHeartEffect());
    }

}

//    public static final RegistryObject<Item> ASTRAL_HEART = ITEMS.register("astral_heart",
//            () -> new Item(new Item.Properties()));