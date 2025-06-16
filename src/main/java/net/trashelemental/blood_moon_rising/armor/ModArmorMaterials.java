package net.trashelemental.blood_moon_rising.armor;


import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.item.ModItems;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, BloodMoonRising.MOD_ID);


    public static final Holder<ArmorMaterial> VISCERAL = MATERIALS.register("visceral", () -> createArmorMaterial(
            new EnumMap<>(ArmorItem.Type.class) {{
                put(ArmorItem.Type.BOOTS, 3);
                put(ArmorItem.Type.LEGGINGS, 5);
                put(ArmorItem.Type.CHESTPLATE, 7);
                put(ArmorItem.Type.HELMET, 3);
            }},
            20,                             //Enchantment value
            SoundEvents.ARMOR_EQUIP_LEATHER,                //Equip sound
            1.0f,                                          //Toughness
            0f,                                          //Knockback resistance
            () -> Ingredient.of(ModItems.EXALTED_BONE),          //Repair ingredient
            List.of(new ArmorMaterial.Layer(BloodMoonRising.prefix("visceral"), "", false)) //Armor texture layers
    ));




    private static ArmorMaterial createArmorMaterial(
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient,
            List<ArmorMaterial.Layer> layers)
    {
        return new ArmorMaterial(defense, enchantmentValue, equipSound, repairIngredient, layers, toughness, knockbackResistance);
    }

    public static void register(IEventBus eventBus) {
        MATERIALS.register(eventBus);
    }

}


