package net.trashelemental.blood_moon_rising.item.custom.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
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

public class JawbladeItem extends SwordItem {
    public JawbladeItem(Tier tier, Properties properties) {
        super(ModToolTiers.BMR, 3, -2.4f, new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

//        int currentPoints = getCurrentPoints(stack);
//        tooltipComponents.add(Component.literal("Points: " + currentPoints + " / " + maxPoints));

        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.jawblade").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }

    private static final int maxPoints = 16;
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


    //When an entity is hurt with the weapon, it will add a point, up to the maximum of 15.
    //Does not increase points if the user currently has the Berserk effect.
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (attacker.level().isClientSide) return false;

        int currentPoints = getCurrentPoints(stack);

        if (attacker instanceof Player player && !player.hasEffect(ModMobEffects.BERSERK.get())) {
            if (currentPoints < maxPoints) {
                int pointsToAdd = 1;
                if (AstralHeartEffect.hasAstralHeart(player)) {
                    if (player.getRandom().nextInt(3) == 0) {
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

    //When used, sets points to zero, and gives the user the Berserk effect for 8 seconds.
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);
        ItemStack item = player.getItemInHand(usedHand);
        int currentPoints = getCurrentPoints(item);

        if (!level.isClientSide) {
            PointsToolInteractions checkForPointsAdd = new PointsToolInteractions();

            if (checkForPointsAdd.canAddPoints(player) && currentPoints < maxPoints) {
                checkForPointsAdd.addPointsFromIchorOrChrismBottle(player, item, maxPoints);
                return InteractionResultHolder.success(item);
            }

            if (currentPoints == maxPoints) {
                if (!player.hasEffect(ModMobEffects.EXHAUSTION.get()) && !player.hasEffect(ModMobEffects.BERSERK.get())) {
                    setCurrentPoints(item, 0);

                    int duration = astralHeart ? 240 : 160;
                    int amplifier = astralHeart ? 1 : 0;

                    player.addEffect(new MobEffectInstance(ModMobEffects.BERSERK.get(), duration, amplifier));
                    level.playSound(null, player.blockPosition(), SoundEvents.POLAR_BEAR_WARNING, SoundSource.PLAYERS, 1f, 0.3f);
                }
            }
        }

        return super.use(level, player, usedHand);
    }
}
