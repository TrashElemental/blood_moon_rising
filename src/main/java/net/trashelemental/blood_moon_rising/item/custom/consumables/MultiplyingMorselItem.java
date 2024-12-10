package net.trashelemental.blood_moon_rising.item.custom.consumables;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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
import net.trashelemental.blood_moon_rising.entity.ModEntities;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;

import java.util.List;

public class MultiplyingMorselItem extends Item {

    public MultiplyingMorselItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 16;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.multiplying_morsel").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);

        if (player.getFoodData().getFoodLevel() > 2) {
            player.startUsingItem(usedHand);
        } else {
            player.displayClientMessage(Component.translatable("message.blood_moon_rising.multiplying_morsel_fail"), true);
        }

        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {

        if (!level.isClientSide && livingEntity instanceof Player player) {

                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 2);

                int morselCount = 1 + level.getRandom().nextInt(2);
                for (int i = 0; i < morselCount; i++) {
                    spawnMorsel(level, player);
                }

                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.HUSK_CONVERTED_TO_ZOMBIE, player.getSoundSource(), 0.3F, 1.3F);

                stack.shrink(1);

        }
        return super.finishUsingItem(stack, level, livingEntity);
    }

    //Helper Methods
    private void spawnMorsel(Level level, Player player) {
        MorselEntity morsel = new MorselEntity(ModEntities.MORSEL.get(), level);
        morsel.moveTo(player.getX(), player.getY(), player.getZ(), 0, 0);
        morsel.setTame(true, false);
        morsel.setOwnerUUID(player.getUUID());
        morsel.setAge(-900);
        level.addFreshEntity(morsel);
    }
}
