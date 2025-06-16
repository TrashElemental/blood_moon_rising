package net.trashelemental.blood_moon_rising.magic.brewing;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;

public class ModPotions {
    public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, BloodMoonRising.MOD_ID);

    public static final RegistryObject<Potion> HEALTH_BOOST_POTION = REGISTRY.register("health_boost_potion", () ->
            new Potion(new MobEffectInstance(MobEffects.HEALTH_BOOST, 3600, 0, false, true)));
    public static final RegistryObject<Potion> HEALTH_BOOST_POTION_LONG = REGISTRY.register("health_boost_potion_long", () ->
            new Potion(new MobEffectInstance(MobEffects.HEALTH_BOOST, 9600, 0, false, true)));
    public static final RegistryObject<Potion> HEALTH_BOOST_POTION_STRONG = REGISTRY.register("health_boost_potion_strong", () ->
            new Potion(new MobEffectInstance(MobEffects.HEALTH_BOOST, 1800, 1, false, true)));

    public static final RegistryObject<Potion> ABSORPTION_POTION = REGISTRY.register("absorption_potion", () ->
            new Potion(new MobEffectInstance(MobEffects.ABSORPTION, 900, 0, false, true)));
    public static final RegistryObject<Potion> ABSORPTION_POTION_LONG = REGISTRY.register("absorption_potion_long", () ->
            new Potion(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0, false, true)));
    public static final RegistryObject<Potion> ABSORPTION_POTION_STRONG = REGISTRY.register("absorption_potion_strong", () ->
            new Potion(new MobEffectInstance(MobEffects.ABSORPTION, 440, 1, false, true)));

    public static final RegistryObject<Potion> HEALTH_DOWN_POTION = REGISTRY.register("health_down_potion", () ->
            new Potion(new MobEffectInstance(ModMobEffects.HEALTH_DOWN.get(), 400, 0, false, true)));
    public static final RegistryObject<Potion> HEALTH_DOWN_POTION_LONG = REGISTRY.register("health_down_potion_long", () ->
            new Potion(new MobEffectInstance(ModMobEffects.HEALTH_DOWN.get(), 1200, 0, false, true)));
    public static final RegistryObject<Potion> HEALTH_DOWN_POTION_STRONG = REGISTRY.register("health_down_potion_strong", () ->
            new Potion(new MobEffectInstance(ModMobEffects.HEALTH_DOWN.get(), 200, 1, false, true)));

    public static final RegistryObject<Potion> CORROSION_POTION = REGISTRY.register("corrosion_potion", () ->
            new Potion(new MobEffectInstance(ModMobEffects.CORROSION.get(), 600, 0, false, true)));
    public static final RegistryObject<Potion> CORROSION_POTION_LONG = REGISTRY.register("corrosion_potion_long", () ->
            new Potion(new MobEffectInstance(ModMobEffects.CORROSION.get(), 1500, 0, false, true)));
    public static final RegistryObject<Potion> CORROSION_POTION_STRONG = REGISTRY.register("corrosion_potion_strong", () ->
            new Potion(new MobEffectInstance(ModMobEffects.CORROSION.get(), 300, 1, false, true)));



    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
