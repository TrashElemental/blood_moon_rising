package net.trashelemental.blood_moon_rising.magic.enchantments;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.magic.enchantments.custom.AdrenalineRushEnchantment;
import net.trashelemental.blood_moon_rising.magic.enchantments.custom.JaggedEdgeEnchantment;
import net.trashelemental.blood_moon_rising.magic.enchantments.custom.LastStandEnchantment;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(Registries.ENCHANTMENT, BloodMoonRising.MOD_ID);

    public static final RegistryObject<Enchantment> JAGGED_EDGE = REGISTRY.register("jagged_edge", JaggedEdgeEnchantment::new);

    public static final RegistryObject<Enchantment> ADRENALINE_RUSH = REGISTRY.register("adrenaline_rush", AdrenalineRushEnchantment::new);
    public static final RegistryObject<Enchantment> LAST_STAND = REGISTRY.register("last_stand", LastStandEnchantment::new);


    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
