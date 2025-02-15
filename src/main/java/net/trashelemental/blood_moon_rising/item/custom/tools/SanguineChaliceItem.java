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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.util.item.PointsToolInteractions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SanguineChaliceItem extends Item {
    public SanguineChaliceItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        int currentPoints = getCurrentPoints(stack);
        tooltipComponents.add(Component.literal("Points: " + currentPoints + " / " + maxPoints));

        List<MobEffectInstance> effects = getStoredPotionEffects(stack);
        if (!effects.isEmpty()) {
            effects.forEach(effect -> {
                String effectName = getEffectName(effect);
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.sanguine_chalice_secondary_effect", effectName)
                        .withStyle(ChatFormatting.BLUE));
            });
        }

        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.sanguine_chalice").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }

    private static final int maxPoints = 12;
    private static final int healing = 10;
    private static final String POINTS_TAG = "Points";

    public static int getCurrentPoints(ItemStack stack) {
        if (stack.hasTag()) {
            return stack.getTag().getInt(POINTS_TAG);
        }
        return 0;
    }

    public static void setCurrentPoints(ItemStack stack, int points) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        stack.getTag().putInt(POINTS_TAG, points);
    }

    private List<MobEffectInstance> getStoredPotionEffects(ItemStack stack) {
        return PotionUtils.getMobEffects(stack);
    }

    private static String getEffectName(MobEffectInstance effect) {
        return effect.getEffect().getDisplayName().getString();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }


    //If the player uses the item while holding a potion in their other hand, set the secondary effect.
    //If the item is at max points, start using the item.
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if (!level.isClientSide) {

            ItemStack item = player.getItemInHand(usedHand);
            InteractionHand offhand = InteractionHand.OFF_HAND;
            ItemStack offhandStack = player.getItemInHand(offhand);
            int currentPoints = getCurrentPoints(item);


            PointsToolInteractions checkForPointsAdd = new PointsToolInteractions();

            if (checkForPointsAdd.canAddPoints(player) && currentPoints < maxPoints) {
                checkForPointsAdd.addPointsFromIchorOrChrismBottle(player, item, maxPoints);
                return InteractionResultHolder.success(item);
            }


            //Read potion contents and set if the item is a potion, then return a glass bottle
            if (offhandStack.is(Items.POTION) || offhandStack.is(Items.LINGERING_POTION) || offhandStack.is(Items.SPLASH_POTION)) {

                List<MobEffectInstance> effects = getStoredPotionEffects(offhandStack);

                if (!effects.isEmpty()) {
                    for (MobEffectInstance effect : effects) {
                        String effectName = getEffectName(effect);
                        player.displayClientMessage(Component.translatable("tooltip.blood_moon_rising.sanguine_chalice_secondary_effect_set", effectName), true);
                    }
                    PotionUtils.setCustomEffects(item, effects);

                    if (!player.isCreative()) {
                        player.setItemInHand(offhand, new ItemStack(Items.GLASS_BOTTLE));
                    }

                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 1.0F, 1.0F);

                } else {
                    player.displayClientMessage(Component.translatable("tooltip.blood_moon_rising.sanguine_chalice_secondary_effect_fail"), true);
                }
            }

            //Clear potion contents if the item is a milk bucket
            if (offhandStack.is(Items.MILK_BUCKET)) {
                CompoundTag tag = item.getTag();
                if (tag != null) {
                    tag.remove("CustomPotionEffects");
                    tag.remove("Potion");
                }
                player.displayClientMessage(Component.translatable("tooltip.blood_moon_rising.sanguine_chalice_secondary_effect_clear"), true);
            }

            if (currentPoints == maxPoints) {
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

            setCurrentPoints(stack, 0);

            if (player.getHealth() == player.getMaxHealth()) {
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 1));
            } else {
                player.heal(healing);
            }

            List<MobEffectInstance> effects = getStoredPotionEffects(stack);

            for (MobEffectInstance effect : effects) {
                int duration = effect.getEffect() ==
                        MobEffects.HEAL || effect.getEffect() == MobEffects.HARM ? 1 : 300;

                player.addEffect(new MobEffectInstance(effect.getEffect(), duration, effect.getAmplifier()));
            }
        }
        return stack;
    }
}
