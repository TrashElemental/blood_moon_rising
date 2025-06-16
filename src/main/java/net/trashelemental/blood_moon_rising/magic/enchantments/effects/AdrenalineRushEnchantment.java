package net.trashelemental.blood_moon_rising.magic.enchantments.effects;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record AdrenalineRushEnchantment() implements EnchantmentEntityEffect {

    public static final MapCodec<AdrenalineRushEnchantment> CODEC = MapCodec.unit(AdrenalineRushEnchantment::new);

    //While the wielder of the enchanted weapon is at or below 6 health, deal 2 extra damage per level.
    //Extra damage increases to 3 at 4 health, and four at 2 health.
    @Override
    public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity victim, Vec3 vec3) {

        LivingEntity owner = item.owner();
        LivingEntity target = (LivingEntity) victim;
        assert owner != null;
        float ownerHealth = owner.getHealth();
        float targetHealth = target.getHealth();

        if (ownerHealth <= 6) {
            int bonusDamage;

            if (owner.getHealth() <= 2) {
                bonusDamage = 4 * enchantmentLevel;
            } else if (owner.getHealth() <= 4) {
                bonusDamage = 3 * enchantmentLevel;
            } else {
                bonusDamage = 2 * enchantmentLevel;
            }

            target.setHealth(targetHealth - bonusDamage);

        }

    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
