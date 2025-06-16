package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SuspiciousStewItem;

import java.util.UUID;

/**
 * Provides the player with a burst of strength and haste when they eat meat and flesh. When the player
 * kills an enemy, they will consume them, gaining some health and hunger.
 */
public class FeralHeartEffect extends AbstractHeartEffect {

    private static final UUID UUID = java.util.UUID.fromString("9a4d3b1c-bf95-4f20-bf18-e063b14a12ed");

    public FeralHeartEffect() {
        super(UUID, "Feral", -2.0);
    }

    @Override
    public void onConsumeFood(Player player, ItemStack food, int nutrition, float saturation) {

        FoodProperties properties = food.getItem().getFoodProperties(food, player);

        if (properties != null && properties.isMeat()) {
            if (!player.hasEffect(MobEffects.DAMAGE_BOOST)) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0, false, true));
            }
            if (!player.hasEffect(MobEffects.DIG_SPEED)) {
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 0, false, true));
            }
            if (!player.hasEffect(MobEffects.REGENERATION)) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0, false, true));
            }
        }

        super.onConsumeFood(player, food, nutrition, saturation);
    }

    @Override
    public void onKilledEnemy(Player player, LivingEntity killed) {

        int playerFoodLevel = player.getFoodData().getFoodLevel();
        float playerSaturationLevel = player.getFoodData().getSaturationLevel();
        float targetMaxHealth = killed.getMaxHealth();

        int hungerRestoreAmount = (int) Math.max(targetMaxHealth / 4, 2);
        float saturationRestoreAmount = (float) Math.max(hungerRestoreAmount * 0.1, 0.2);

        int newFoodLevel = Math.min(playerFoodLevel + hungerRestoreAmount, 20);
        float newSaturation = Math.min(playerSaturationLevel + saturationRestoreAmount, newFoodLevel);

        player.getFoodData().setFoodLevel(newFoodLevel);
        player.getFoodData().setSaturation(newSaturation);

        float missingHealth = player.getMaxHealth() - player.getHealth();
        if (missingHealth > 0) {
            player.heal(Math.min(hungerRestoreAmount, missingHealth));
        }

        super.onKilledEnemy(player, killed);
    }
}
