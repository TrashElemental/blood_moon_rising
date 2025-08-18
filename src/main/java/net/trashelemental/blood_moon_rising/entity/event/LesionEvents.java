package net.trashelemental.blood_moon_rising.entity.event;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.LesionEntity;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;

public class LesionEvents {

    public static void LesionHurtTarget(Entity entity) {
        if (entity instanceof LivingEntity livingEntity && !livingEntity.hasEffect(ModMobEffects.HEALTH_DOWN.get())) {
            livingEntity.addEffect(new MobEffectInstance(ModMobEffects.HEALTH_DOWN.get(),
                    300, 0, false, true));
        }
    }

    public static void LesionStealHeart(LesionEntity lesion, Player player) {
        ItemStack heart = new ItemStack(ModItems.HEART.get());
        Level level = player.level();

        player.playSound(SoundEvents.ITEM_PICKUP);
        if (!player.getInventory().add(heart))
            player.drop(heart, false);

        if (!level.isClientSide) {
            lesion.triggerAnim("specialDeathController", "special_death");
            lesion.setNoAi(true);
            LesionSpecialDeathVFX(lesion);
        }
    }

    public static void LesionSpecialDeathVFX(LesionEntity lesion) {

        BloodMoonRising.queueServerWork(4, () ->
                lesion.playSound(SoundEvents.ZOMBIE_HORSE_HURT, 1, 0.6f)
        );
        BloodMoonRising.queueServerWork(20, () ->
                lesion.playSound(SoundEvents.POLAR_BEAR_STEP, 5, 0.4f)
        );
       BloodMoonRising.queueServerWork(28, () ->
                        lesion.level().broadcastEntityEvent(lesion, (byte) 60)
        );

    }
}
