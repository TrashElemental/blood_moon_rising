package net.trashelemental.blood_moon_rising.item.custom.consumables.foods;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CookedFleshItem extends Item {
    public CookedFleshItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.cooked_flesh").withStyle(ChatFormatting.BLUE));

        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }


}
