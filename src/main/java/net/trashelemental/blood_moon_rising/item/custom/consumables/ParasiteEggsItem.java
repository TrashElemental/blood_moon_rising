package net.trashelemental.blood_moon_rising.item.custom.consumables;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.entity.event.MinionSpawnLogic;

import java.util.List;

public class ParasiteEggsItem extends Item {
    public ParasiteEggsItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 16;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (Config.DISPLAY_TOOLTIPS.get()) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.parasite_eggs").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack item = player.getItemInHand(usedHand);
        Holder<DamageType> damageTypeHolder = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.GENERIC);

        int parasiteCount = 2 + level.getRandom().nextInt(2);
        for (int i = 0; i < parasiteCount; i++) {
            MinionSpawnLogic.spawnParasite(level, player, 400, true);
        }

        if (!player.isCreative()) {
            item.shrink(1);
        }

        player.swing(usedHand);

        if (player.getHealth() > 1) {
            player.hurt(new DamageSource(damageTypeHolder), 1.0F);
        }

        player.getCooldowns().addCooldown(this, 60);

        return InteractionResultHolder.sidedSuccess(item, level.isClientSide());
    }


}
