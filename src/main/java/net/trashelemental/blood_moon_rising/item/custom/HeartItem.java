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
import net.trashelemental.blood_moon_rising.capabilities.ModCapabilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeartItem extends Item {
    public HeartItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

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

        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);

        if (level.isClientSide) {
            return InteractionResultHolder.pass(itemstack);
        }

        if (player.getCapability(ModCapabilities.HEART_DATA).map(data -> data.hasHeart(this)).orElse(false)) {
            player.displayClientMessage(Component.translatable("message.blood_moon_rising.heart_equip_fail"), true);
            return InteractionResultHolder.fail(itemstack);
        }

        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!(entity instanceof Player player)) return stack;

        if (!level.isClientSide) {
            player.getCapability(ModCapabilities.HEART_DATA).ifPresent(data -> {
                if (!data.hasHeart(this)) {
                    data.addHeart(this, player);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
            });
        }

        player.playSound(SoundEvents.EVOKER_CAST_SPELL, 0.5f, 0.8f);

        return stack;
    }
}
