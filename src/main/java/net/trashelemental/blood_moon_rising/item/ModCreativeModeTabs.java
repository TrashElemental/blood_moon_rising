package net.trashelemental.blood_moon_rising.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BloodMoonRising.MOD_ID);

    public static final Supplier<CreativeModeTab> BLOOD_MOON_RISING_TAB = CREATIVE_MODE_TAB.register("blood_moon_rising_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.HEART.get()))
                    .title(Component.translatable("creativetab.blood_moon_rising.bmr_tab"))
                    .displayItems(((itemDisplayParameters, output) -> {

                        output.accept(ModItems.FLEAM.get());
                        output.accept(ModItems.AUGUR.get());
                        output.accept(ModItems.BUTCHERS_CLEAVER.get());
                        output.accept(ModItems.JAWBLADE.get());
                        output.accept(ModItems.SACRED_SPEAR.get());
                        output.accept(ModItems.SACRIFICIAL_DAGGER.get());
                        output.accept(ModItems.SANGUINE_CHALICE.get());
                        output.accept(ModItems.WARHAM.get());

                        output.accept(ModItems.EXALTED_BONE.get());
                        output.accept(ModItems.ICHOR_CLOT.get());
                        output.accept(ModItems.CHYME.get());
                        output.accept(ModItems.LIGAMENT.get());
                        output.accept(ModItems.HEART.get());

                        output.accept(ModItems.COOKED_HEART.get());
                        output.accept(ModItems.CURED_HEART.get());
                        output.accept(ModItems.HEART_OF_GOLD.get());
                        output.accept(ModItems.EXALTED_FLESH.get());
                        output.accept(ModItems.COOKED_FLESH.get());
                        output.accept(ModItems.CURED_FLESH.get());
                        output.accept(ModItems.CONSECRATED_FLESH.get());
                        output.accept(ModItems.JERKY.get());
                        output.accept(ModItems.MULTIPLYING_MORSEL.get());

                        output.accept(ModItems.BOTTLE_OF_ICHOR.get());
                        output.accept(ModItems.BOTTLE_OF_CHRISM.get());
                        output.accept(ModItems.BOLUS.get());
                        output.accept(ModItems.PARASITE_EGGS.get());


                        output.accept(ModItems.SCORCHED_HEART.get());
                        output.accept(ModItems.TAINTED_HEART.get());
                        output.accept(ModItems.FROZEN_HEART.get());
                        output.accept(ModItems.ECHOING_HEART.get());
                        output.accept(ModItems.FERAL_HEART.get());
                        output.accept(ModItems.HEAVY_HEART.get());
                        output.accept(ModItems.ELUSIVE_HEART.get());
                        output.accept(ModItems.HUNGRY_HEART.get());
                        output.accept(ModItems.SELFLESS_HEART.get());
                        output.accept(ModItems.BROKEN_HEART.get());
                        output.accept(ModItems.DIVIDING_HEART.get());
                        output.accept(ModItems.SPITEFUL_HEART.get());
                        output.accept(ModItems.FRANTIC_HEART.get());
                        output.accept(ModItems.WRATHFUL_HEART.get());
                        output.accept(ModItems.ASTRAL_HEART.get());

                        output.accept(ModItems.LEECH_SPAWN_EGG.get());
                        output.accept(ModItems.MOSQUITO_SPAWN_EGG.get());

                    })).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
