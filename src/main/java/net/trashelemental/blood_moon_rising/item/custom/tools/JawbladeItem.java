package net.trashelemental.blood_moon_rising.item.custom.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects.AstralHeartEffect;
import net.trashelemental.blood_moon_rising.components.ModComponents;
import net.trashelemental.blood_moon_rising.item.ModToolTiers;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;
import net.trashelemental.blood_moon_rising.util.item.PointsToolInteractions;

import java.util.List;

public class JawbladeItem extends SwordItem {
    public JawbladeItem(Tier tier, Properties properties) {
        super(ModToolTiers.BMR, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (Config.DISPLAY_POINTS.get()) {
            int currentPoints = getCurrentPoints(stack);
            tooltipComponents.add(Component.literal("Points: " + currentPoints + " / " + maxPoints));
        }

        if (Config.DISPLAY_TOOLTIPS.get()) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.jawblade").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private static final int maxPoints = 16;

    private int getCurrentPoints(ItemStack stack) {
        if (stack.has(ModComponents.POINTS)) {
            Integer points = stack.get(ModComponents.POINTS);
            return points != null ? points : 0;
        }
        return 0;
    }


    //When an entity is hurt with the weapon, it will add a point, up to the maximum.
    //Does not increase points if the user currently has the Berserk effect.
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

        if (!player.hasEffect(ModMobEffects.BERSERK)) {
            if (!stack.has(ModComponents.POINTS)) {
                stack.set(ModComponents.POINTS, 1);
            } else if (currentPoints < maxPoints) {
                stack.set(ModComponents.POINTS, Math.min(currentPoints + pointsToAdd, maxPoints));
            }
        }

        super.postHurtEnemy(stack, target, attacker);
    }

    //When used, sets points to zero, and gives the user the Berserk effect for 8 seconds.
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        ItemStack item = player.getItemInHand(usedHand);
        int currentPoints = getCurrentPoints(item);

        boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);
        int duration = astralHeart ? 240 : 160;
        int amplifier = astralHeart ? 1 : 0;

        if (!level.isClientSide) {

            PointsToolInteractions pointsToolInteractions = new PointsToolInteractions();
            if (pointsToolInteractions.canAddPoints(player) && !(currentPoints >= maxPoints)) {
                pointsToolInteractions.addPointsFromIchorOrChrismBottle(player, item, currentPoints, maxPoints);
                return InteractionResultHolder.success(item);
            }

            if (currentPoints == maxPoints) {
                if (!player.hasEffect(ModMobEffects.EXHAUSTION) && !player.hasEffect(ModMobEffects.BERSERK)) {
                    item.set(ModComponents.POINTS, 0);
                    player.addEffect(new MobEffectInstance(ModMobEffects.BERSERK, duration, amplifier));
                    level.playSound(null, player.blockPosition(), SoundEvents.POLAR_BEAR_WARNING, SoundSource.PLAYERS, 1f, 0.3f);
                }
            }
        }

        return super.use(level, player, usedHand);
    }
}
