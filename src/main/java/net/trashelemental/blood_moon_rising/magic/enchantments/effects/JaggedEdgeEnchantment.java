package net.trashelemental.blood_moon_rising.magic.enchantments.effects;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import net.trashelemental.blood_moon_rising.magic.effects.events.HemorrhageLogic;

public record JaggedEdgeEnchantment() implements EnchantmentEntityEffect {

    public static final MapCodec<JaggedEdgeEnchantment> CODEC = MapCodec.unit(JaggedEdgeEnchantment::new);

    //Attacks have a 40% chance to inflict Hemorrhage on a hit, with +20% per level.
    //However, durability decreases faster when inflicting hemorrhage.
    @Override
    public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity victim, Vec3 vec3) {

        int baseChance = 20;
        int additionalChancePerLevel = 20;
        int totalChance = baseChance + (additionalChancePerLevel * enchantmentLevel);
        LivingEntity owner = item.owner();

        int randomChance = serverLevel.random.nextInt(100);

        if (randomChance < totalChance) {
            HemorrhageLogic.applyHemorrhage((LivingEntity) victim, owner, 160);
            item.itemStack().hurtAndBreak(3, owner, EquipmentSlot.MAINHAND);
        }

    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
