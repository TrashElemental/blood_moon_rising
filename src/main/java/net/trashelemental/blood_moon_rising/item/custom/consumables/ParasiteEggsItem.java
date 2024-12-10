package net.trashelemental.blood_moon_rising.item.custom.consumables;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.entity.ModEntities;

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

        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.parasite_eggs").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
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
            spawnParasite(level, player);
        }

        if (!player.isCreative()) {
            item.shrink(1);
        }

        player.swing(usedHand);

        if (player.getHealth() > 1) {
            player.hurt(new DamageSource(damageTypeHolder), 1.0F);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.FROGSPAWN_HATCH, player.getSoundSource(), 1F, 0.8F);

        player.getCooldowns().addCooldown(this, 60);

        return InteractionResultHolder.sidedSuccess(item, level.isClientSide());
    }

    //Helper Methods
    private static void spawnParasite(Level level, LivingEntity player) {
        if (level instanceof ServerLevel serverLevel) {

            boolean isInWater = player.isInWater();
            var entityType = isInWater ? ModEntities.LEECH : ModEntities.MOSQUITO;
            var entity = entityType.get().create(serverLevel);

            if (entity != null) {
                entity.moveTo(player.getX(), player.getY(), player.getZ(), level.getRandom().nextFloat() * 360F, 0);
                entity.setTame(true, false);
                entity.setOwnerUUID(player.getUUID());
                entity.setAge(400);
                serverLevel.addFreshEntity(entity);

                serverLevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR, player.getX(), player.getY(), player.getZ(),
                        3, 0.5, 0.5, 0.5, 0.1);
            }
        }
    }

}
