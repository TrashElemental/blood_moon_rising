package net.trashelemental.blood_moon_rising.magic.enchantments;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.magic.enchantments.effects.AdrenalineRushEnchantment;
import net.trashelemental.blood_moon_rising.magic.enchantments.effects.JaggedEdgeEnchantment;

public class ModEnchantments {

    public static final ResourceKey<Enchantment> JAGGED_EDGE = ResourceKey.create(Registries.ENCHANTMENT,
            BloodMoonRising.prefix("jagged_edge"));
    public static final ResourceKey<Enchantment> LAST_STAND = ResourceKey.create(Registries.ENCHANTMENT,
            BloodMoonRising.prefix("last_stand"));
    public static final ResourceKey<Enchantment> ADRENALINE_RUSH = ResourceKey.create(Registries.ENCHANTMENT,
            BloodMoonRising.prefix("adrenaline_rush"));


    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup(Registries.ITEM);

        //Jagged Edge is incompatible with Fire Aspect due to fire damage clearing hemorrhage.
        register(context, JAGGED_EDGE, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),                                //Enchantable items
                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),                                //Preferred items
                1,                                                                                  //Weight
                3,                                                                                  //Max level
                Enchantment.dynamicCost(5, 7),                                        //Enchanting Table cost (min)
                Enchantment.dynamicCost(25, 7),                                       //Enchanting Table cost (max)
                2,                                                                                  //Anvil cost
                EquipmentSlotGroup.MAINHAND))                                                       //Equipment slot
                .exclusiveWith(enchantments.getOrThrow(EnchantmentTags.SMELTS_LOOT))                //Incompatible Enchantments
                .withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER,    //Type of effect, should link to its class
                        EnchantmentTarget.VICTIM, new JaggedEdgeEnchantment()));

        //Doing this one through an event handler because I'm a dumb idiot baby who can't read code and couldn't figure things out
        register(context, LAST_STAND, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        2,
                        3,
                        Enchantment.dynamicCost(5, 7),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.ARMOR)));

        register(context, ADRENALINE_RUSH, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        2,
                        3,
                        Enchantment.dynamicCost(5, 7),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.MAINHAND))
                .withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM, new AdrenalineRushEnchantment()));
    }


    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }

}

//Remember to add them to the in enchanting table tag.
