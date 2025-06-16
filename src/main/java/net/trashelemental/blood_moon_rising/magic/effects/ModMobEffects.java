package net.trashelemental.blood_moon_rising.magic.effects;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.magic.effects.custom.*;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, BloodMoonRising.MOD_ID);

    public static final Holder<MobEffect> HEMORRHAGE = MOB_EFFECTS.register("hemorrhage",
            () -> new HemorrhageEffect(MobEffectCategory.HARMFUL, 10027008));

    public static final Holder<MobEffect> BERSERK = MOB_EFFECTS.register("berserk",
        () -> new BerserkEffect(MobEffectCategory.BENEFICIAL, 14588946));
    public static final Holder<MobEffect> EXHAUSTION = MOB_EFFECTS.register("exhaustion",
            () -> new ExhaustionEffect(MobEffectCategory.HARMFUL, 9264671));

    public static final Holder<MobEffect> HEALTH_DOWN = MOB_EFFECTS.register("health_down",
            () -> new HealthDownEffect(MobEffectCategory.HARMFUL, 0xA0A74E));
    public static final Holder<MobEffect> CORROSION = MOB_EFFECTS.register("corrosion",
            () -> new CorrosionEffect(MobEffectCategory.HARMFUL, 7566610));

    public static final Holder<MobEffect> KINSHIP = MOB_EFFECTS.register("kinship",
            () -> new KinshipEffect(MobEffectCategory.BENEFICIAL, 14588946));
    public static final Holder<MobEffect> SCORN = MOB_EFFECTS.register("scorn",
            () -> new ScornEffect(MobEffectCategory.BENEFICIAL, 14588946));

    public static final Holder<MobEffect> LEAKING = MOB_EFFECTS.register("leaking",
            () -> new LeakingEffect(MobEffectCategory.BENEFICIAL, 14588946));



    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
