package net.trashelemental.blood_moon_rising.item.custom.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects.AstralHeartEffect;
import net.trashelemental.blood_moon_rising.item.ModToolTiers;
import net.trashelemental.blood_moon_rising.magic.effects.event.HemorrhageLogic;
import net.trashelemental.blood_moon_rising.util.item.PointsToolInteractions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SacrificialDaggerItem extends SwordItem {
    public SacrificialDaggerItem(Tier tier, Properties properties) {
        super(ModToolTiers.BMR, 1, -1.5f, new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

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

        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }

    private static final int maxPoints = 18;
    private static final String POINTS_TAG = "Points";

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
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (attacker.level().isClientSide) return false;
        if (!(attacker instanceof Player player)) return false;

        boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);
        boolean avoidPointLoss = astralHeart && player.level().random.nextBoolean();

        int currentPoints = getCurrentPoints(stack);
        float bonusDamage = getBonusDamage(stack);
        float newHealth = Math.max(target.getHealth() - bonusDamage, 0);

        target.setHealth(newHealth);

        if (currentPoints >= 10) {
            if (player.level().random.nextInt(2) == 0) {
                HemorrhageLogic.applyHemorrhage(target, player, 160);
            }
        } else if (currentPoints >= 1) {
            if (player.level().random.nextInt(3) == 0) {
                HemorrhageLogic.applyHemorrhage(target, player, 160);
            }
        }

        if (currentPoints > 0 && !(avoidPointLoss)) {
            setCurrentPoints(stack, currentPoints - 1);
        }

        stack.hurtAndBreak(1, player, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });

        return false;
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
            PointsToolInteractions checkForPointsAdd = new PointsToolInteractions();

            if (checkForPointsAdd.canAddPoints(player) && currentPoints < maxPoints) {
                checkForPointsAdd.addPointsFromIchorOrChrismBottle(player, item, maxPoints);
                return InteractionResultHolder.success(item);
            }
        }

        if (!level.isClientSide && !(currentPoints == maxPoints)) {

            setCurrentPoints(item, maxPoints);
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