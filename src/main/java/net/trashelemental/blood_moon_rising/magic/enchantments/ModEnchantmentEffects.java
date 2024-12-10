package net.trashelemental.blood_moon_rising.magic.enchantments;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.magic.enchantments.effects.AdrenalineRushEnchantment;
import net.trashelemental.blood_moon_rising.magic.enchantments.effects.JaggedEdgeEnchantment;

import java.util.function.Supplier;

public class ModEnchantmentEffects {

    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_ENCHANTMENT_EFFECTS =
            DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, BloodMoonRising.MOD_ID);


    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> JAGGED_EDGE =
            ENTITY_ENCHANTMENT_EFFECTS.register("jagged_edge", () -> JaggedEdgeEnchantment.CODEC);
    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> ADRENALINE_RUSH =
            ENTITY_ENCHANTMENT_EFFECTS.register("adrenaline_rush", () -> AdrenalineRushEnchantment.CODEC);

    //Last Stand is done through an event handler atm because I'm an idiot baby who can't read code
    //and couldn't figure out how to make a damage reduction effect that worked the way I wanted


    public static void register(IEventBus eventBus) {
        ENTITY_ENCHANTMENT_EFFECTS.register(eventBus);
    }
}
