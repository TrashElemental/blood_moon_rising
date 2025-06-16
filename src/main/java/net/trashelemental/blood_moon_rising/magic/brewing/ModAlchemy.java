package net.trashelemental.blood_moon_rising.magic.brewing;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;

@EventBusSubscriber
public class ModAlchemy {
    public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(Registries.POTION, BloodMoonRising.MOD_ID);

    //Health Boost
    public static final DeferredHolder<Potion, Potion> HEALTH_BOOST_POTION = REGISTRY.register("health_boost_potion", () ->
            new Potion(new MobEffectInstance(MobEffects.HEALTH_BOOST, 3600, 0, false, true)));
    public static final DeferredHolder<Potion, Potion> HEALTH_BOOST_POTION_LONG = REGISTRY.register("health_boost_potion_long", () ->
            new Potion(new MobEffectInstance(MobEffects.HEALTH_BOOST, 9600, 0, false, true)));
    public static final DeferredHolder<Potion, Potion> HEALTH_BOOST_POTION_STRONG = REGISTRY.register("health_boost_potion_strong", () ->
            new Potion(new MobEffectInstance(MobEffects.HEALTH_BOOST, 1800, 1, false, true)));

    //Absorption
    public static final DeferredHolder<Potion, Potion> ABSORPTION_POTION = REGISTRY.register("absorption_potion", () ->
            new Potion(new MobEffectInstance(MobEffects.ABSORPTION, 900, 0, false, true)));
    public static final DeferredHolder<Potion, Potion> ABSORPTION_POTION_LONG = REGISTRY.register("absorption_potion_long", () ->
            new Potion(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0, false, true)));
    public static final DeferredHolder<Potion, Potion> ABSORPTION_POTION_STRONG = REGISTRY.register("absorption_potion_strong", () ->
            new Potion(new MobEffectInstance(MobEffects.ABSORPTION, 440, 1, false, true)));

    //Health Down
    public static final DeferredHolder<Potion, Potion> HEALTH_DOWN_POTION = REGISTRY.register("health_down_potion", () ->
            new Potion(new MobEffectInstance(ModMobEffects.HEALTH_DOWN, 400, 0, false, true)));
    public static final DeferredHolder<Potion, Potion> HEALTH_DOWN_POTION_LONG = REGISTRY.register("health_down_potion_long", () ->
            new Potion(new MobEffectInstance(ModMobEffects.HEALTH_DOWN, 1200, 0, false, true)));
    public static final DeferredHolder<Potion, Potion> HEALTH_DOWN_POTION_STRONG = REGISTRY.register("health_down_potion_strong", () ->
            new Potion(new MobEffectInstance(ModMobEffects.HEALTH_DOWN, 200, 1, false, true)));

    //Corrosion
    public static final DeferredHolder<Potion, Potion> CORROSION_POTION = REGISTRY.register("corrosion_potion", () ->
            new Potion(new MobEffectInstance(ModMobEffects.CORROSION, 600, 0, false, true)));
    public static final DeferredHolder<Potion, Potion> CORROSION_POTION_LONG = REGISTRY.register("corrosion_potion_long", () ->
            new Potion(new MobEffectInstance(ModMobEffects.CORROSION, 1500, 0, false, true)));
    public static final DeferredHolder<Potion, Potion> CORROSION_POTION_STRONG = REGISTRY.register("corrosion_potion_strong", () ->
            new Potion(new MobEffectInstance(ModMobEffects.CORROSION, 300, 1, false, true)));



    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }


    @SubscribeEvent
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {


        //Potions

        //Health Boost
        PotionBrewing.Builder HealthBoostPotion = event.getBuilder();
        HealthBoostPotion.addMix(
                Potions.AWKWARD,           //Item in the bottom slot
                ModItems.HEART.get(),      //Item in the top slot / Ingredient
                HEALTH_BOOST_POTION        //Result
        );

        PotionBrewing.Builder HealthBoostPotionLong = event.getBuilder();
        HealthBoostPotionLong.addMix(HEALTH_BOOST_POTION, Items.REDSTONE, HEALTH_BOOST_POTION_LONG);

        PotionBrewing.Builder HealthBoostPotionStrong = event.getBuilder();
        HealthBoostPotionStrong.addMix(HEALTH_BOOST_POTION, Items.GLOWSTONE_DUST, HEALTH_BOOST_POTION_STRONG);


        //Absorption
        PotionBrewing.Builder AbsorptionPotion = event.getBuilder();
        AbsorptionPotion.addMix(Potions.AWKWARD, ModItems.HEART_OF_GOLD.get(), ABSORPTION_POTION);

        PotionBrewing.Builder AbsorptionPotionLong = event.getBuilder();
        AbsorptionPotionLong.addMix(ABSORPTION_POTION, Items.REDSTONE, ABSORPTION_POTION_LONG);

        PotionBrewing.Builder AbsorptionPotionStrong = event.getBuilder();
        AbsorptionPotionStrong.addMix(ABSORPTION_POTION, Items.GLOWSTONE_DUST, ABSORPTION_POTION_STRONG);


        //Health Down
        PotionBrewing.Builder HealthDownPotion = event.getBuilder();
        HealthDownPotion.addMix(HEALTH_BOOST_POTION, Items.FERMENTED_SPIDER_EYE, HEALTH_DOWN_POTION);

        PotionBrewing.Builder HealthDownPotionLong = event.getBuilder();
        HealthDownPotionLong.addMix(HEALTH_DOWN_POTION, Items.REDSTONE, HEALTH_DOWN_POTION_LONG);

        PotionBrewing.Builder HealthDownPotionStrong = event.getBuilder();
        HealthDownPotionStrong.addMix(HEALTH_DOWN_POTION, Items.GLOWSTONE_DUST, HEALTH_DOWN_POTION_STRONG);

        //Corrosion
        PotionBrewing.Builder CorrosionPotion = event.getBuilder();
        CorrosionPotion.addMix(Potions.AWKWARD, ModItems.CHYME.get(), CORROSION_POTION);

        PotionBrewing.Builder CorrosionPotionLong = event.getBuilder();
        CorrosionPotionLong.addMix(CORROSION_POTION, Items.REDSTONE, CORROSION_POTION_LONG);

        PotionBrewing.Builder CorrosionPotionStrong = event.getBuilder();
        CorrosionPotionStrong.addMix(CORROSION_POTION, Items.GLOWSTONE_DUST, CORROSION_POTION_STRONG);

        //Non-Potions






    }
}
