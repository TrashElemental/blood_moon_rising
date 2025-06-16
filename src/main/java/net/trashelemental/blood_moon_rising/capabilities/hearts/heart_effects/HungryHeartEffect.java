package net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import java.util.UUID;

/**
 * Speeds up the time it takes to eat food, increases nutrition and saturation gained from food, and gives
 * the Health Boost effect if the nutrition and saturation are high enough. Also prevents adverse effects being
 * gained from eating 'unsafe' foods like Rotten Flesh.
 */
public class HungryHeartEffect extends AbstractHeartEffect {

    public HungryHeartEffect() {
        super("hungry", -2);
    }

    @Override
    public void onConsumeFood(Player player, ItemStack food, int nutrition, float saturation) {

        int playerFoodLevel = player.getFoodData().getFoodLevel();
        float playerSaturationLevel = player.getFoodData().getSaturationLevel();

        player.getFoodData().setFoodLevel(playerFoodLevel + 2);
        player.getFoodData().setSaturation(playerSaturationLevel + 0.2f);

        if ((nutrition >= 8) || saturation >= 12f) {
            if (!player.hasEffect(MobEffects.HEALTH_BOOST)) {
                player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 900, 0, false, false));
            }
        }

        FoodProperties properties = food.getItem().getFoodProperties(food, player);
        if (properties != null) {
            for (FoodProperties.PossibleEffect possible : properties.effects()) {
                MobEffectInstance instance = possible.effect();
                Holder<MobEffect> effectHolder = instance.getEffect();
                if (effectHolder.isBound() && effectHolder.value().getCategory() != MobEffectCategory.BENEFICIAL) {
                    if (player.hasEffect(effectHolder)) {
                        player.removeEffect(effectHolder);
                    }
                }
            }
        }

        //When the stew is sus
        if (food.getItem() instanceof SuspiciousStewItem) {
            SuspiciousStewEffects stewEffects = food.getOrDefault(DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY);
            for (SuspiciousStewEffects.Entry entry : stewEffects.effects()) {
                Holder<MobEffect> effectHolder = entry.effect();
                if (effectHolder.isBound() && effectHolder.value().getCategory() != MobEffectCategory.BENEFICIAL) {
                    player.removeEffect(effectHolder);
                }
            }
        }

        super.onConsumeFood(player, food, nutrition, saturation);
    }
}
