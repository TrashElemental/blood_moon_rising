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

public class BottleOfIchorItem extends Item {
    public BottleOfIchorItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.bottle_of_ichor_health").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.bottle_of_ichor_hunger").withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.bottle_of_ichor_poison").withStyle(ChatFormatting.RED));

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

    //When the player uses the item, it will restore 6 health, inflict hunger II for 15 seconds,
    //and it has a chance to inflict poison II for 7 seconds. And it will return a bottle.
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {

        if (!level.isClientSide && livingEntity instanceof Player player) {

            player.heal(6);
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 15 * 20, 1));

            if (level.getRandom().nextFloat() < 0.25f) {
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 7 * 20, 1));
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