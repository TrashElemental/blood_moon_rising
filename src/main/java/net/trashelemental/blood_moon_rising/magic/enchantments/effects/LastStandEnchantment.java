package net.trashelemental.blood_moon_rising.magic.enchantments.effects;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record LastStandEnchantment() implements EnchantmentEntityEffect {

    public static final MapCodec<LastStandEnchantment> CODEC = MapCodec.unit(LastStandEnchantment::new);

    @Override
    public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 vec3) {



    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
