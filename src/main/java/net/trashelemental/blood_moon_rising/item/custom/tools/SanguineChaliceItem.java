package net.trashelemental.blood_moon_rising.item.custom.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects.AstralHeartEffect;
import net.trashelemental.blood_moon_rising.components.ModComponents;
import net.trashelemental.blood_moon_rising.util.item.PointsToolInteractions;

import java.util.List;
import java.util.Optional;

//Charging for this item is handled in util.event.SanguineChaliceCharge.

public class SanguineChaliceItem extends Item {
    public SanguineChaliceItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (Config.DISPLAY_POINTS.get()) {
            int currentPoints = getCurrentPoints(stack);
            tooltipComponents.add(Component.literal("Points: " + currentPoints + " / " + maxPoints));
        }

        List<MobEffectInstance> effects = getStoredPotionEffects(stack);
        if (!effects.isEmpty()) {
            effects.forEach(effect -> {
                String effectName = getEffectName(effect);
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.sanguine_chalice_secondary_effect", effectName)
                        .withStyle(ChatFormatting.BLUE));
            });
        }

        if (Config.DISPLAY_TOOLTIPS.get()) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.sanguine_chalice").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private static final int maxPoints = 12;

    public static int getCurrentPoints(ItemStack stack) {
        if (stack.has(ModComponents.POINTS)) {
            Integer points = stack.get(ModComponents.POINTS);
            return points != null ? points : 0;
        }
        return 0;
    }

    private List<MobEffectInstance> getStoredPotionEffects(ItemStack stack) {
        PotionContents potionContents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return (List<MobEffectInstance>) potionContents.getAllEffects();
    }

    private static String getEffectName(MobEffectInstance effect) {
        return effect.getEffect().value().getDisplayName().getString();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }


    //If the player uses the item while holding a potion in their other hand, set the secondary effect.
    //If the item is at max points, start using the item.
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if (!level.isClientSide) {

            ItemStack stack = player.getItemInHand(usedHand);
            InteractionHand offhand = InteractionHand.OFF_HAND;
            ItemStack offhandStack = player.getItemInHand(offhand);
            int currentPoints = getCurrentPoints(stack);

            PointsToolInteractions pointsToolInteractions = new PointsToolInteractions();
            if (pointsToolInteractions.canAddPoints(player) && !(currentPoints >= maxPoints)) {
                pointsToolInteractions.addPointsFromIchorOrChrismBottle(player, stack, currentPoints, maxPoints);
                return InteractionResultHolder.success(stack);
            }

            //Read potion contents and set if the item is a potion, then return a glass bottle
            if (offhandStack.is(Items.POTION) || offhandStack.is(Items.LINGERING_POTION) || offhandStack.is(Items.SPLASH_POTION)) {

                List<MobEffectInstance> effects = getStoredPotionEffects(offhandStack);

                if (!effects.isEmpty()) {
                    effects.forEach(effect -> {
                        String effectName = getEffectName(effect);
                        player.displayClientMessage(Component.translatable("tooltip.blood_moon_rising.sanguine_chalice_secondary_effect_set", effectName), true);
                    });

                    PotionContents newPotionContents = new PotionContents(Optional.empty(), Optional.empty(), effects);
                    stack.set(DataComponents.POTION_CONTENTS, newPotionContents);

                    if (!player.isCreative()) {
                        player.setItemInHand(offhand, new ItemStack(Items.GLASS_BOTTLE));
                    }

                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 1.0F, 1.0F);

                } else {
                    player.displayClientMessage(Component.translatable("tooltip.blood_moon_rising.sanguine_chalice_secondary_effect_fail"), true);
                }
                return InteractionResultHolder.success(stack);
            }

            //Clear potion contents if the item is a milk bucket
            if (offhandStack.is(Items.MILK_BUCKET)) {

                stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.BUCKET_EMPTY_POWDER_SNOW, SoundSource.PLAYERS, 1.0F, 1.0F);

                player.displayClientMessage(Component.translatable("tooltip.blood_moon_rising.sanguine_chalice_secondary_effect_clear"), true);

                return InteractionResultHolder.success(stack);
            }

            if (getCurrentPoints(stack) == maxPoints) {
                player.startUsingItem(usedHand);
            }
        }
        return super.use(level, player, usedHand);
    }

    //When the player finishes using the item, they will receive healing or absorption depending on current health.
    //If a potion effect is stored, receive that potion effect for 15 seconds.
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {

        if (!level.isClientSide && entity instanceof Player player) {

            stack.set(ModComponents.POINTS, 0);

            boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);
            int amplifier = astralHeart ? 2 : 1;
            int healing = astralHeart ? 20 : 10;
            int baseDuration = astralHeart ? 300 : 600;

            if (player.getHealth() == player.getMaxHealth()) {
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 600, amplifier));
            } else {
                player.heal(healing);
            }

            List<MobEffectInstance> effects = getStoredPotionEffects(stack);

            for (MobEffectInstance effect : effects) {
                int duration = effect.getEffect() ==
                        MobEffects.HEAL || effect.getEffect() == MobEffects.HARM ? 1 : baseDuration;

                player.addEffect(new MobEffectInstance(effect.getEffect(), duration, effect.getAmplifier()));
            }
        }
        return stack;
    }
}
