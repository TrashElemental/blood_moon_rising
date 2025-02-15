package net.trashelemental.blood_moon_rising.magic.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.magic.effects.custom.BerserkEffect;
import net.trashelemental.blood_moon_rising.magic.effects.custom.ExhaustionEffect;
import net.trashelemental.blood_moon_rising.magic.effects.custom.HemorrhageEffect;

public class ModMobEffects {

    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BloodMoonRising.MOD_ID);

    public static final RegistryObject<MobEffect> HEMORRHAGE = REGISTRY.register("hemorrhage", HemorrhageEffect::new);

    public static final RegistryObject<MobEffect> BERSERK = REGISTRY.register("berserk", BerserkEffect::new);
    public static final RegistryObject<MobEffect> EXHAUSTION = REGISTRY.register("exhaustion", ExhaustionEffect::new);


    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
