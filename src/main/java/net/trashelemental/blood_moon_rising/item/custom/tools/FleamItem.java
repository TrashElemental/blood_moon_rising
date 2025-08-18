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
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects.AstralHeartEffect;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;
import net.trashelemental.blood_moon_rising.entity.event.MinionSpawnLogic;
import net.trashelemental.blood_moon_rising.junkyard_lib.visual.particle.ParticleMethods;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FleamItem extends Item {
    public FleamItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (Config.DISPLAY_TOOLTIPS.get()) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.fleam").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
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

                    ParticleMethods.ParticleTrailEntityToEntity(level, ParticleTypes.DAMAGE_INDICATOR, morsel, player, 5);
                }

                if (consumedMorsel) {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.HUSK_CONVERTED_TO_ZOMBIE, player.getSoundSource(), 1.0F, 1.5F);
                }

            }
        }

        //Otherwise, damage the player for 2 and create a morsel
        //also consume 1 durability and cooldown for 1 second
        else {

            if (!level.isClientSide) {

                boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);
                boolean avoidDamage = astralHeart && level.random.nextBoolean();

                if (player.getHealth() > 2.0F) {
                    Holder<DamageType> damageTypeHolder = level.registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(DamageTypes.GENERIC);

                    MinionSpawnLogic.spawnMorsel((ServerLevel) level, player, 900, true);

                    ParticleMethods.ParticlesAroundServerSide(level, ParticleTypes.DAMAGE_INDICATOR,
                            player.getX(), player.getY() + 0.5, player.getZ(), 5, 1.5);

                    if (!avoidDamage) {
                        player.hurt(new DamageSource(damageTypeHolder), 2.0F);
                        ItemStack fleam = player.getItemInHand(usedHand);
                        fleam.hurtAndBreak(1, player, (entity) -> {
                            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                        });
                    }

                    player.getCooldowns().addCooldown(this, 20);
                } else {
                    player.displayClientMessage(Component.translatable("message.blood_moon_rising.fleam_fail"), true);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }
}
