package net.trashelemental.blood_moon_rising.item.custom.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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

//        int currentPoints = getCurrentPoints(stack);
//        tooltipComponents.add(Component.literal("Points: " + currentPoints + " / " + maxPoints));

        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.jawblade").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
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

        int currentPoints = getCurrentPoints(stack);

        if (!attacker.hasEffect(ModMobEffects.BERSERK)) {
            if (!stack.has(ModComponents.POINTS)) {
                stack.set(ModComponents.POINTS, 1);
            } else if (currentPoints < maxPoints) {
                stack.set(ModComponents.POINTS, currentPoints + 1);
            }
        }

        super.postHurtEnemy(stack, target, attacker);
    }

    //When used, sets points to zero, and gives the user the Berserk effect for 8 seconds.
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

            if (currentPoints == maxPoints) {

                item.set(ModComponents.POINTS, 0);
                player.addEffect(new MobEffectInstance(ModMobEffects.BERSERK, 160, 0));

            }
        }

        return super.use(level, player, usedHand);
    }
}
