package net.trashelemental.blood_moon_rising.item.custom.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.trashelemental.blood_moon_rising.magic.effects.events.HemorrhageLogic;
import net.trashelemental.blood_moon_rising.util.item.PointsToolInteractions;

import java.util.List;

public class SacrificialDaggerItem extends SwordItem {
    public SacrificialDaggerItem(Tier tier, Properties properties) {
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
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.sacrificial_dagger").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private static final int maxPoints = 18;

    private int getCurrentPoints(ItemStack stack) {
        if (stack.has(ModComponents.POINTS)) {
            Integer points = stack.get(ModComponents.POINTS);
            return points != null ? points : 0;
        }
        return 0;
    }

    private float getBonusDamage(ItemStack stack) {

        int currentPoints = getCurrentPoints(stack);

        if (currentPoints >= 10) {
            return 3.0f;
        } else if (currentPoints >= 1) {
            return 2.0f;
        }

        return 0.0f;
    }



    //On hurting an enemy, reduces the target's health by an amount that corresponds
    //to how many points the weapon currently has, calculated above.
    //Also has a scaling chance to apply Hemorrhage if it has at least 1 point.
    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (attacker.level().isClientSide) return;
        if (!(attacker instanceof Player player)) return;

        boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);
        boolean avoidPointLoss = astralHeart && player.level().random.nextBoolean();

        int currentPoints = getCurrentPoints(stack);
        float bonusDamage = getBonusDamage(stack);
        float newHealth = Math.max(target.getHealth() - bonusDamage, 0);

        target.setHealth(newHealth);

        if (currentPoints >= 10) {
            if (player.level().random.nextInt(2) == 0) {
                HemorrhageLogic.applyHemorrhage(target, player, 240);
            }
        } else if (currentPoints >= 1) {
            if (player.level().random.nextInt(3) == 0) {
                HemorrhageLogic.applyHemorrhage(target, player, 240);
            }
        }

        if (currentPoints > 0 && !(avoidPointLoss)) {
            stack.set(ModComponents.POINTS, currentPoints - 1);
        }

        super.postHurtEnemy(stack, target, attacker);
    }

    //When used, it will set the weapon's points to the maximum, and damage the player an amount
    //that corresponds to how many points the weapon currently had. Doesn't activate at max points.
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        ItemStack item = player.getItemInHand(usedHand);
        int currentPoints = getCurrentPoints(item);

        Holder<DamageType> damageTypeHolder = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.GENERIC);

        if (!level.isClientSide) {
            PointsToolInteractions pointsToolInteractions = new PointsToolInteractions();
            if (pointsToolInteractions.canAddPoints(player) && !(currentPoints >= maxPoints)) {
                pointsToolInteractions.addPointsFromIchorOrChrismBottle(player, item, currentPoints, maxPoints);
                return InteractionResultHolder.success(item);
            }
        }

        if (!level.isClientSide && !(currentPoints == maxPoints)) {

            item.set(ModComponents.POINTS, maxPoints);
            player.swing(usedHand);

            boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);
            float minDamage = astralHeart ? 1.5f : 3.0f;
            float medDamage = astralHeart ? 3.0f : 6.0f;
            float maxDamage = astralHeart ? 4.0f : 8.0f;

            if (currentPoints >= 10) {
                player.hurt(new DamageSource(damageTypeHolder), minDamage);
            } else if (currentPoints >= 1) {
                player.hurt(new DamageSource(damageTypeHolder), medDamage);
            } else if (currentPoints == 0) {
                player.hurt(new DamageSource(damageTypeHolder), maxDamage);
            }

        }

        return super.use(level, player, usedHand);
    }


}
