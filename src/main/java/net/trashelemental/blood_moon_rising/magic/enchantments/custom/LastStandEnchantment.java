package net.trashelemental.blood_moon_rising.magic.enchantments.custom;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LastStandEnchantment extends Enchantment {

    public LastStandEnchantment(EquipmentSlot... slots) {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_CHEST, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }


}
