package net.trashelemental.blood_moon_rising.item.custom.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects.AstralHeartEffect;
import net.trashelemental.blood_moon_rising.item.ModToolTiers;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;
import net.trashelemental.blood_moon_rising.util.item.PointsToolInteractions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ButchersCleaverItem extends AxeItem {
    public ButchersCleaverItem(Tier p_40521_, Properties p_40524_) {
        super(ModToolTiers.BMR, 5, -3.2f, new Item.Properties());
    }

    private static final int maxPoints = 12;
    private static final String POINTS_TAG = "Points";

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

//        int currentPoints = getCurrentPoints(stack);
//        tooltipComponents.add(Component.literal("Points: " + currentPoints + " / " + maxPoints));

        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.butchers_cleaver").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }

    private int getCurrentPoints(ItemStack stack) {
        if (stack.hasTag()) {
            return stack.getTag().getInt(POINTS_TAG);
        }
        return 0;
    }

    private void setCurrentPoints(ItemStack stack, int points) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        stack.getTag().putInt(POINTS_TAG, points);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.level().isClientSide) return false;

        int currentPoints = getCurrentPoints(stack);

        if (attacker instanceof Player player) {
            if (currentPoints < maxPoints) {
                int pointsToAdd = 1;
                if (AstralHeartEffect.hasAstralHeart(player)) {
                    if (player.getRandom().nextInt(2) == 0) {
                        pointsToAdd = 2;
                    }
                }
                setCurrentPoints(stack, Math.min(currentPoints + pointsToAdd, maxPoints));
            }
        }

        stack.hurtAndBreak(1, attacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });

        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack item = player.getItemInHand(usedHand);
        int currentPoints = getCurrentPoints(item);

        if (!level.isClientSide) {
            PointsToolInteractions checkForPointsAdd = new PointsToolInteractions();

            if (checkForPointsAdd.canAddPoints(player) && currentPoints < maxPoints) {
                checkForPointsAdd.addPointsFromIchorOrChrismBottle(player, item, maxPoints);
                return InteractionResultHolder.success(item);
            }
        }

        if (!level.isClientSide && currentPoints == maxPoints) {
            player.startUsingItem(usedHand);
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide && livingEntity instanceof Player player) {
            int currentPoints = getCurrentPoints(stack);
            boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);
            int duration = astralHeart ? 300 : 200;
            int amplifier = astralHeart ? 2 : 1;
            float healing = astralHeart ? 12 : 8;

            if (currentPoints == maxPoints) {
                setCurrentPoints(stack, 0);
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