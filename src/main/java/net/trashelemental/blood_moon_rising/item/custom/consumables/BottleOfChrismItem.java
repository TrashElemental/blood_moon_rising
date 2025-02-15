package net.trashelemental.blood_moon_rising.item.custom.consumables;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BottleOfChrismItem extends Item {
    public BottleOfChrismItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.bottle_of_chrism").withStyle(ChatFormatting.BLUE));

        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);

        player.startUsingItem(usedHand);

        return InteractionResultHolder.pass(itemstack);
    }

    //When the item is used, it will fully heal the player, and return a bottle.
    //Absorption if a player is at max health already.
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {

        if (!level.isClientSide && livingEntity instanceof Player player) {

            if (!(player.getHealth() == player.getMaxHealth())) {
                player.setHealth(player.getMaxHealth());
            } else {
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 30 * 20, 1));
            }

            if (!player.isCreative()) {
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (!player.addItem(bottle)) {
                    player.drop(bottle, false);
                }
                stack.shrink(1);
            }

        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}