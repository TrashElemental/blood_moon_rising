package net.trashelemental.blood_moon_rising.capabilities.hearts.event;

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
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.trashelemental.blood_moon_rising.capabilities.AttachmentsRegistry;
import net.trashelemental.blood_moon_rising.capabilities.hearts.HeartData;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;

/**
* Handles the events for the various heart effects.
 */
@EventBusSubscriber
public class HeartEventHandler {

    @SubscribeEvent
    public static void PlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);

        if (player.level().isClientSide) return;

        data.onTick(player);
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        if (target.level().isClientSide) return;

        if (target instanceof Player player && attacker instanceof LivingEntity entity) {
            HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);

            data.onHurt(player, entity, event.getSource(), event.getOriginalDamage());
        }

        if (attacker instanceof Player player && target instanceof LivingEntity entity && !(entity instanceof ArmorStand)) {
            HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);

            data.onDamage(player, target, event.getOriginalDamage());
        }
    }

    @SubscribeEvent
    public static void onKilledEnemy(LivingDeathEvent event) {
        LivingEntity killed = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        if (killed.level().isClientSide) return;

        if (attacker instanceof Player player && !(killed instanceof ArmorStand)) {
            HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);

            data.onKilledEnemy(player, killed);
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
        HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);

        data.onInteract(player, entity);
    }

    @SubscribeEvent
    public static void preDamage(LivingDamageEvent.Pre event) {
        LivingEntity target = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        if (target.level().isClientSide) return;

        if (target instanceof Player player && attacker instanceof LivingEntity) {
            HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);

            if (data.hasHeart(ModItems.ELUSIVE_HEART.get())) {
                float chance = data.getElusiveDodgeChance();

                if (player.getRandom().nextFloat() < chance) {
                    event.setNewDamage(0);
                    data.resetElusiveDodgeChance();
                } else {
                    data.increaseElusiveDodgeChance();
                }
            }
        }

        if (target instanceof Player player && event.getSource().is(DamageTypes.FREEZE)) {
            HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);

            if (data.hasHeart(ModItems.FROZEN_HEART.get())) {
                event.setNewDamage(0);
            }
        }

    }

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        MobEffectInstance effect = event.getEffectInstance();
        if (effect == null) return;
        MobEffect type = effect.getEffect().value();
        if (!(type.equals(MobEffects.POISON.value()) || type.equals(ModMobEffects.CORROSION.value()))) return;

        HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);
        if (data.hasHeart(ModItems.TAINTED_HEART.get())) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    @SubscribeEvent
    public static void onStartUsingItem(LivingEntityUseItemEvent.Start event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack item = event.getItem();
        int duration = event.getDuration();
        FoodProperties food = item.getFoodProperties(player);
        HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);

        if (food != null && data.hasHeart(ModItems.HUNGRY_HEART.get())) {
            event.setDuration(duration / 2);
        }

    }

    @SubscribeEvent
    public static void onFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack item = event.getItem();
        FoodProperties food = item.getFoodProperties(player);

        if (food != null) {
            int nutrition = food.nutrition();
            float saturation = food.saturation();
            HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);

            data.onConsumeFood(player, item, nutrition, saturation);
        }
    }

}
