package net.trashelemental.blood_moon_rising.item.custom.consumables.foods;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConsecratedFleshItem extends Item {
    public ConsecratedFleshItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.consecrated_flesh").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            if (!player.hasEffect(ModMobEffects.EXHAUSTION.get()) && !player.hasEffect(ModMobEffects.BERSERK.get())) {
                player.addEffect(new MobEffectInstance(ModMobEffects.BERSERK.get(), 240, 0));
                level.playSound(null, player.blockPosition(), SoundEvents.POLAR_BEAR_WARNING, SoundSource.PLAYERS, 1f, 0.3f);
            }
        }

        return super.finishUsingItem(stack, level, entity);
    }
}
