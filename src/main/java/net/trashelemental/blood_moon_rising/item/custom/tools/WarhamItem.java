package net.trashelemental.blood_moon_rising.item.custom.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects.AstralHeartEffect;
import net.trashelemental.blood_moon_rising.item.ModToolTiers;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WarhamItem extends AxeItem {
    public WarhamItem(Tier p_40521_, Properties p_40524_) {
        super(ModToolTiers.BMR, 5, -3.2f, new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (Config.DISPLAY_TOOLTIPS.get()) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.warham").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }



    //If the player can eat, they will start using the item.
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if (!level.isClientSide) {
            if (player.getFoodData().getFoodLevel() < 20) {
                player.startUsingItem(usedHand);
            }
        }

        return super.use(level, player, usedHand);
    }

    //When the player finishes using the item, they will get a substantial amount of food and saturation.
    //However, the durability of the weapon will take a significant hit.
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {

        if (!level.isClientSide && entity instanceof Player player) {

            boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);

            int hunger = astralHeart ? 16 : 8;
            float saturation = astralHeart ? 3 : 2.4f;
            int damage = astralHeart ? 136 : 272;

            player.getFoodData().eat(hunger, saturation);

            stack.hurtAndBreak(damage, player, (e) -> e.broadcastBreakEvent(player.getUsedItemHand()));

        }

        return stack;
    }
}
