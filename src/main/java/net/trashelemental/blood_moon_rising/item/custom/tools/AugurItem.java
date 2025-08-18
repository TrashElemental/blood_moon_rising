package net.trashelemental.blood_moon_rising.item.custom.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.blood_moon.BloodMoonManager;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects.AstralHeartEffect;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.WoundMob;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AugurItem extends Item {
    public AugurItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (Config.DISPLAY_TOOLTIPS.get()) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.augur").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        int bloodMoonCountdown;
        boolean isBloodMoon;
        ItemStack stack = player.getItemInHand(hand);
        boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);
        int damage = astralHeart ? 0 : 1;

        if (level instanceof ServerLevel serverLevel) {
            ServerLevel overworld = serverLevel.getServer().getLevel(Level.OVERWORLD);

            if (overworld != null) {
                bloodMoonCountdown = BloodMoonManager.getCurrentCountdown(overworld);
                isBloodMoon = BloodMoonManager.isBloodMoon(overworld);

                if (bloodMoonCountdown != 0) {
                    readBloodMoonCountdown(player, bloodMoonCountdown);
                }

                if (isBloodMoon) {
                    highlightMobs(player);
                }
            }
            stack.hurtAndBreak(damage, player, (e) -> e.broadcastBreakEvent(player.getUsedItemHand()));
        }

        player.playSound(SoundEvents.ELDER_GUARDIAN_CURSE, 0.3f, 1f);
        player.swing(player.getUsedItemHand());
        player.getCooldowns().addCooldown(this, 100);


        return super.use(level, player, hand);
    }

    /**
     * Reads the number of nights until the next Blood Moon, displaying a different message
     * if a blood moon will happen that night.
     */
    public static void readBloodMoonCountdown(Player player, int bloodMoonCountdown) {
            if (bloodMoonCountdown == 1) {
                player.displayClientMessage(Component.translatable("message.blood_moon_rising.augur_read_countdown_one")
                        .withStyle(ChatFormatting.RED), false);
            }
            else {
                player.displayClientMessage(Component.translatable("message.blood_moon_rising.augur_read_countdown", bloodMoonCountdown)
                        .withStyle(ChatFormatting.RED), false);
            }
    }

    public static void highlightMobs(Player player) {
        if (player.level().isClientSide) return;

        double radius = 40.0;
        AABB area = new AABB(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );

        List<Mob> woundMobs = player.level().getEntitiesOfClass(Mob.class, area,
                mob -> mob instanceof WoundMob && mob.isAlive());

        for (Mob mob : woundMobs) {
            mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0));
        }
    }



}
