package net.trashelemental.blood_moon_rising.magic.enchantments.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.trashelemental.blood_moon_rising.magic.effects.event.HemorrhageLogic;

public class JaggedEdgeEnchantment extends Enchantment {

    public JaggedEdgeEnchantment(EquipmentSlot... slots) {
        super(Rarity.RARE, EnchantmentCategory.WEAPON, slots);
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

        int baseChance = 20;
        int additionalChancePerLevel = 20;
        int totalChance = baseChance + (additionalChancePerLevel * pLevel);

        if (pTarget instanceof LivingEntity target) {
            ItemStack item = pAttacker.getItemInHand(pAttacker.getUsedItemHand());

            int randomChance = pAttacker.level().random.nextInt(100);

            if (randomChance < totalChance) {
                HemorrhageLogic.applyHemorrhage(target, pAttacker, 160);
                item.hurtAndBreak(1, pAttacker, (entity) -> {
                    entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                });

            }
        }
    }


}
