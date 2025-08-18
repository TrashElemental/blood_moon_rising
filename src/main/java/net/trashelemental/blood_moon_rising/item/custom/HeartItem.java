package net.trashelemental.blood_moon_rising.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.capabilities.AttachmentsRegistry;
import net.trashelemental.blood_moon_rising.capabilities.hearts.HeartData;

import java.util.List;

public class HeartItem extends Item {
    public HeartItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (Config.DISPLAY_TOOLTIPS.get()) {
            if (Screen.hasShiftDown()) {
                ResourceLocation id = BuiltInRegistries.ITEM.getKey(this);
                String tooltipKey = "tooltip." + id.getNamespace() + "." + id.getPath() + "_desc";
                tooltipComponents.add(Component.translatable(tooltipKey)
                        .withStyle(ChatFormatting.ITALIC)
                        .withStyle(ChatFormatting.DARK_GRAY));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);
        if (level.isClientSide) return InteractionResultHolder.pass(stack);

        if (data.hasHeart(this)) {
            player.displayClientMessage(Component.translatable("message.blood_moon_rising.heart_equip_fail"), true);
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!(entity instanceof Player player) || level.isClientSide) return stack;
        HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);

        if (!data.hasHeart(this)) {
            data.addHeart(this, player);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            player.playSound(SoundEvents.EVOKER_CAST_SPELL, 0.5f, 0.8f);
        }
        return stack;
    }
}