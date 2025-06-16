package net.trashelemental.blood_moon_rising.capabilities.hearts;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IHeartEffect {

    default void onTick(Player player) {}

    default void onHurt(Player player, LivingEntity target, DamageSource source, float amount) {}

    default void onDamage(Player player, LivingEntity target, float amount) {}

    default void onKilledEnemy(Player player, LivingEntity killed) {}

    default void onAdded(Player player) {}

    default void onRemoved(Player player) {}

    default void onInteract(Player player, LivingEntity entity) {}

    default void onConsumeFood(Player player, ItemStack food, int nutrition, float saturation) {}

}
