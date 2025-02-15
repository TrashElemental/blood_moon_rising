package net.trashelemental.blood_moon_rising.magic.enchantments.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class AdrenalineRushEnchantment extends Enchantment {

    public AdrenalineRushEnchantment(EquipmentSlot... slots) {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, slots);
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {

        if (pTarget instanceof LivingEntity target) {
            float ownerHealth = pAttacker.getHealth();
            float targetHealth = target.getHealth();

            if (ownerHealth <= 6) {
                int bonusDamage;

                if (pAttacker.getHealth() <= 2) {
                    bonusDamage = 4 * pLevel;
                } else if (pAttacker.getHealth() <= 4) {
                    bonusDamage = 3 * pLevel;
                } else {
                    bonusDamage = 2 * pLevel;
                }

                target.setHealth(targetHealth - bonusDamage);
            }
        }
    }


}
