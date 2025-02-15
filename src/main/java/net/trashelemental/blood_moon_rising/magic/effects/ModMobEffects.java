package net.trashelemental.blood_moon_rising.magic.effects;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.magic.effects.custom.BerserkEffect;
import net.trashelemental.blood_moon_rising.magic.effects.custom.ExhaustionEffect;
import net.trashelemental.blood_moon_rising.magic.effects.custom.HemorrhageEffect;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, BloodMoonRising.MOD_ID);

    public static final Holder<MobEffect> HEMORRHAGE = MOB_EFFECTS.register("hemorrhage",
            () -> new HemorrhageEffect(MobEffectCategory.HARMFUL, 10027008));

    public static final Holder<MobEffect> BERSERK = MOB_EFFECTS.register("berserk",
        () -> new BerserkEffect(MobEffectCategory.BENEFICIAL, 14588946));
    public static final Holder<MobEffect> EXHAUSTION = MOB_EFFECTS.register("exhaustion",
            () -> new ExhaustionEffect(MobEffectCategory.HARMFUL, 9264671));



    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
