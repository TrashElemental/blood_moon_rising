package net.trashelemental.blood_moon_rising.capabilities.heart_data.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.trashelemental.blood_moon_rising.capabilities.ModCapabilities;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.HeartEffects;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.IHeartEffect;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class HeartEventHandler {


    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
            forEachHeartEffect(player, effect -> effect.onTick(player));
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        LivingEntity target = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        if (target.level().isClientSide) return;

        if (attacker instanceof Player player && !(target instanceof ArmorStand)) {
            forEachHeartEffect(player, effect -> effect.onDamage(player, target, event.getAmount()));
        }

        if (target instanceof Player player && attacker instanceof LivingEntity entity) {
            forEachHeartEffect(player, effect -> effect.onHurt(player, entity, event.getSource(), event.getAmount()));

            player.getCapability(ModCapabilities.HEART_DATA).ifPresent(data -> {
                if (data.hasHeart(ModItems.ELUSIVE_HEART.get())) {
                    float chance = data.getElusiveDodgeChance();

                    if (player.getRandom().nextFloat() < chance) {
                        event.setCanceled(true);
                        data.resetElusiveDodgeChance();

                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0f, 1.0f);
                    } else {
                        data.increaseElusiveDodgeChance();
                    }
                }
            });
        }

        if (target instanceof Player player) {
            player.getCapability(ModCapabilities.HEART_DATA).ifPresent(data -> {
                if (data.hasHeart(ModItems.FROZEN_HEART.get()) && event.getSource().is(DamageTypes.FREEZE)) {
                    event.setCanceled(true);
                }

                if (data.hasHeart(ModItems.ELUSIVE_HEART.get()) && event.getSource().is(DamageTypes.FALL)) {
                    event.setCanceled(true);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onKillEnemy(LivingDeathEvent event) {
        LivingEntity killed = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        if (killed.level().isClientSide) return;

        if (attacker instanceof Player player && !(killed instanceof ArmorStand)) {
            forEachHeartEffect(player, effect -> effect.onKilledEnemy(player, killed));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onMobInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (!(event.getTarget() instanceof LivingEntity entity)) return;
        if (!entity.isAlive()) return;
        if (!player.isCrouching()) return;
        if (player.level().isClientSide) return;

        forEachHeartEffect(player, effect -> effect.onInteract(player, entity));
    }

    @SubscribeEvent
    public static void onEffectGained(MobEffectEvent.Applicable event) {
        MobEffectInstance effect = event.getEffectInstance();
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        MobEffect type = effect.getEffect();
        if (!(type.equals(MobEffects.POISON) || type.equals(ModMobEffects.CORROSION.get()))) return;

        player.getCapability(ModCapabilities.HEART_DATA).ifPresent(data -> {
            if (data.hasHeart(ModItems.TAINTED_HEART.get())) {
                event.setResult(Event.Result.DENY);
            }
        });
    }

    @SubscribeEvent
    public static void onFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack item = event.getItem();

        if (item.isEdible()) {
            FoodProperties food = item.getFoodProperties(player);
            int nutrition = food.getNutrition();
            float saturation = food.getSaturationModifier();
            forEachHeartEffect(player, effect -> effect.onConsumeFood(player, item, nutrition, saturation));
        }
    }

    /**
     * Halves eating time if the player has Hungry Heart Equipped
     */
    @SubscribeEvent
    public static void onStartUsingItem(LivingEntityUseItemEvent.Start event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack item = event.getItem();
        int duration = event.getDuration();

        player.getCapability(ModCapabilities.HEART_DATA).ifPresent(data -> {
            if (data.hasHeart(ModItems.HUNGRY_HEART.get())) {
                if (item.isEdible()) {
                    event.setDuration(duration / 2);
                }
            }
        });
    }


    /**
     * Helper methods
     */
    public static void forEachHeartEffect(Player player, Consumer<IHeartEffect> action) {
        player.getCapability(ModCapabilities.HEART_DATA).ifPresent(data -> {
            for (ResourceLocation id : data.getHearts()) {
                IHeartEffect effect = HeartEffects.getEffect(ForgeRegistries.ITEMS.getValue(id));
                if (effect != null) {
                    action.accept(effect);
                }
            }
        });
    }

}
