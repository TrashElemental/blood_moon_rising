package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;

import java.util.UUID;

/**
 * Speeds up the time it takes to eat food, increases nutrition and saturation gained from food, and gives
 * the Health Boost effect if the nutrition and saturation are high enough. Also prevents adverse effects being
 * gained from eating 'unsafe' foods like Rotten Flesh.
 */
public class HungryHeartEffect extends AbstractHeartEffect {

    private static final UUID UUID = java.util.UUID.fromString("9a4d3b1c-bf84-4f20-bf18-e063b14a12ed");

    public HungryHeartEffect() {
        super(UUID, "Hungry", -2.0);
    }

    @Override
    public void onConsumeFood(Player player, ItemStack food, int nutrition, float saturation) {

        int playerFoodLevel = player.getFoodData().getFoodLevel();
        float playerSaturationLevel = player.getFoodData().getSaturationLevel();

        player.getFoodData().setFoodLevel(playerFoodLevel + 2);
        player.getFoodData().setSaturation(playerSaturationLevel + 0.2f);

        if ((nutrition >= 8) || (saturation >= 0.8)) {
            if (!player.hasEffect(MobEffects.HEALTH_BOOST)) {
                player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 900, 0, false, false));
            }
        }

        FoodProperties properties = food.getItem().getFoodProperties(food, player);
        if (properties != null) {
            for (Pair<MobEffectInstance, Float> pair : properties.getEffects()) {
                MobEffectInstance instance = pair.getFirst();
                if (instance != null && !(instance.getEffect().isBeneficial())) {
                    if (player.hasEffect(instance.getEffect())) {
                        player.removeEffect(instance.getEffect());
                    }
                }
            }
        }

        //When the stew is sus
        if (food.getItem() instanceof SuspiciousStewItem) {
            CompoundTag tag = food.getTag();
            if (tag != null && tag.contains("Effects", Tag.TAG_LIST)) {
                ListTag effects = tag.getList("Effects", Tag.TAG_COMPOUND);
                for (int i = 0; i < effects.size(); i++) {
                    CompoundTag effectTag = effects.getCompound(i);
                    MobEffect effect = MobEffect.byId(effectTag.getInt("EffectId"));
                    effect = net.minecraftforge.common.ForgeHooks.loadMobEffect(effectTag, "forge:effect_id", effect);
                    if (effect != null && !effect.isBeneficial() && player.hasEffect(effect)) {
                        player.removeEffect(effect);
                    }
                }
            }
        }

        super.onConsumeFood(player, food, nutrition, saturation);
    }
}
