package net.trashelemental.blood_moon_rising.item.custom.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects.AstralHeartEffect;
import net.trashelemental.blood_moon_rising.components.ModComponents;
import net.trashelemental.blood_moon_rising.item.ModToolTiers;
import net.trashelemental.blood_moon_rising.util.item.PointsToolInteractions;

import java.util.List;

public class ButchersCleaverItem extends AxeItem {
    public ButchersCleaverItem(Tier p_40521_, Properties p_40524_) {
        super(ModToolTiers.BMR, p_40524_);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

//        int currentPoints = getCurrentPoints(stack);
//        tooltipComponents.add(Component.literal("Points: " + currentPoints + " / " + maxPoints));

        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.butchers_cleaver").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private static final int maxPoints = 12;
    private static final int healing = 8;

    private int getCurrentPoints(ItemStack stack) {
        if (stack.has(ModComponents.POINTS)) {
            Integer points = stack.get(ModComponents.POINTS);
            return points != null ? points : 0;
        }
        return 0;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }



    //Dealing damage to enemies fills the points for this weapon.
    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (attacker.level().isClientSide) return;
        if (!(attacker instanceof Player player)) return;

        int currentPoints = getCurrentPoints(stack);

        int pointsToAdd = 1;
        if (AstralHeartEffect.hasAstralHeart(player)) {
            if (player.getRandom().nextInt(3) == 0) {
                pointsToAdd = 2;
            }
        }

        if (!stack.has(ModComponents.POINTS)) {
            stack.set(ModComponents.POINTS, 1);
        } else if (currentPoints < maxPoints) {
            stack.set(ModComponents.POINTS, currentPoints + Math.min(currentPoints + pointsToAdd, maxPoints));
        }

        super.postHurtEnemy(stack, target, attacker);
    }

    //When the player uses the item while it is at max points, they will start using the item.
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        ItemStack item = player.getItemInHand(usedHand);
        int currentPoints = getCurrentPoints(item);

        if (!level.isClientSide) {
            PointsToolInteractions pointsToolInteractions = new PointsToolInteractions();
            if (pointsToolInteractions.canAddPoints(player) && !(currentPoints >= maxPoints)) {
                pointsToolInteractions.addPointsFromIchorOrChrismBottle(player, item, currentPoints, maxPoints);
                return InteractionResultHolder.success(item);
            }
        }

        if (!level.isClientSide) {
            if (currentPoints == maxPoints) {
                player.startUsingItem(usedHand);
            }
        }

        return super.use(level, player, usedHand);
    }

    //When the player finishes using the item, they will be healed for 8 health, and receive strength and resistance for a short time.
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {

        if (!level.isClientSide && livingEntity instanceof Player player) {

            int currentPoints = getCurrentPoints(stack);
            boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);
            int duration = astralHeart ? 300 : 200;
            int amplifier = astralHeart ? 2 : 1;
            float healing = astralHeart ? 12 : 8;

            if (currentPoints == maxPoints) {
                stack.set(ModComponents.POINTS, 0);
                player.heal(healing);

                if (!player.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, amplifier));
                }

                if (!player.hasEffect(MobEffects.DAMAGE_BOOST)) {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, amplifier));
                }

            }
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
