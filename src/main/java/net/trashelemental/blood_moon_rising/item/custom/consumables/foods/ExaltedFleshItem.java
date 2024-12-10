package net.trashelemental.blood_moon_rising.item.custom.consumables.foods;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ExaltedFleshItem extends Item {
    public ExaltedFleshItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.exalted_flesh_strength").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.exalted_flesh_hunger").withStyle(ChatFormatting.RED));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
