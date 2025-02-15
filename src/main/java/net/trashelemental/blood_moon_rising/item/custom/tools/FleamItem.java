package net.trashelemental.blood_moon_rising.item.custom.tools;

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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;
import net.trashelemental.blood_moon_rising.entity.event.MinionSpawnLogic;

import java.util.List;

public class FleamItem extends Item {
    public FleamItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.fleam").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        //If the player is crouching, consume all active morsels
        if (player.isCrouching()) {

            if (!level.isClientSide) {
                List<MorselEntity> morsels = level.getEntitiesOfClass(MorselEntity.class,
                        new AABB(player.blockPosition()).inflate(20),
                        morsel -> morsel.isOwnedBy(player));

                boolean consumedMorsel = false;

                for (MorselEntity morsel : morsels) {
                    player.heal(1.0F);
                    morsel.discard();
                    consumedMorsel = true;

                    int steps = 5;
                    double deltaX = player.getX() - morsel.getX();
                    double deltaY = (player.getY() - (morsel.getY() + morsel.getBbHeight() / 2));
                    double deltaZ = player.getZ() - morsel.getZ();

                    for (int i = 0; i <= steps; i++) {
                        double progress = i / (double) steps;
                        double particleX = morsel.getX() + deltaX * progress;
                        double particleY = morsel.getY() + deltaY * progress + 0.5;
                        double particleZ = morsel.getZ() + deltaZ * progress;

                        ((ServerLevel) level).sendParticles(ParticleTypes.DAMAGE_INDICATOR,
                                particleX, particleY, particleZ,
                                1, 0, 0, 0, 0);
                    }
                }

                if (consumedMorsel) {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.HUSK_CONVERTED_TO_ZOMBIE, player.getSoundSource(), 1.0F, 1.5F);

                    for (int i = 0; i < 10; i++) {
                        double offsetX = (level.random.nextDouble() - 0.5) * 2;
                        double offsetY = level.random.nextDouble();
                        double offsetZ = (level.random.nextDouble() - 0.5) * 2;

                        ((ServerLevel) level).sendParticles(ParticleTypes.DAMAGE_INDICATOR,
                                player.getX() + offsetX,
                                player.getY() + 0.5 + offsetY,
                                player.getZ() + offsetZ,
                                1,
                                0, 0, 0, 0);
                    }
                }

            }
        }

        //Otherwise, damage the player for 2 and create a morsel
        //also consume 1 durability and cooldown for 1 second
        else {

            if (!level.isClientSide) {
                if (player.getHealth() > 2.0F) {
                    Holder<DamageType> damageTypeHolder = level.registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(DamageTypes.GENERIC);

                    player.hurt(new DamageSource(damageTypeHolder), 2.0F);
                    MinionSpawnLogic.spawnMorsel((ServerLevel) level, player, -900);

                    for (int i = 0; i < 10; i++) {
                        double offsetX = (level.random.nextDouble() - 0.5) * 2;
                        double offsetY = level.random.nextDouble();
                        double offsetZ = (level.random.nextDouble() - 0.5) * 2;

                        ((ServerLevel) level).sendParticles(ParticleTypes.DAMAGE_INDICATOR,
                                player.getX() + offsetX,
                                player.getY() + 0.5 + offsetY,
                                player.getZ() + offsetZ,
                                1,
                                0, 0, 0, 0);
                    }

                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.HUSK_CONVERTED_TO_ZOMBIE, player.getSoundSource(), 0.5F, 0.8F);

                    ItemStack fleam = player.getItemInHand(usedHand);
                    fleam.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    player.getCooldowns().addCooldown(this, 20);
                } else {
                    player.displayClientMessage(Component.translatable("message.blood_moon_rising.fleam_fail"), true);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }
}

