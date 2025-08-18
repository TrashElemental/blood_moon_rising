package net.trashelemental.blood_moon_rising.item.custom.consumables;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.junkyard_lib.visual.particle.ParticleMethods;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * An item that can be used as a universal food item for mob breeding and growing, as well as a superior bonemeal alternative.
 */

public class BolusItem extends Item {
    public BolusItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (Config.DISPLAY_TOOLTIPS.get()) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.bolus").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {

        Level level = player.level();

        if (target instanceof Animal animal) {

            //For baby animals, causes them to become fully grown.
            if (animal.isBaby()) {
                if (!player.level().isClientSide) {
                    animal.setAge(0);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    target.level().playSound(null, target.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 0.8F, 1.0F);
                    ParticleMethods.ParticlesAroundServerSide(level, ParticleTypes.HAPPY_VILLAGER,
                            animal.getX(), animal.getY(), animal.getZ(), 10, 1);
                }
                return InteractionResult.SUCCESS;
            }

            //For adult animals, negates any breeding cooldown and immediately sets them to be in love.
            else if (!animal.isBaby()) {
                if (!player.level().isClientSide) {
                    animal.setAge(0);
                    animal.setInLove(player);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    target.level().playSound(null, target.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 0.8F, 1.0F);
                    ParticleMethods.ParticlesAroundServerSide(level, ParticleTypes.HAPPY_VILLAGER,
                            animal.getX(), animal.getY(), animal.getZ(), 10, 1);
                }
                return InteractionResult.SUCCESS;
            }
            player.swing(hand);
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        boolean applied = false;

        if (!level.isClientSide && !(player == null)) {
            for (int i = 0; i < 3; i++) {
                applied |= tryApplyBonemealEffects(stack, level, pos, player, context.getClickedFace());
                applied |= tryGrowNetherWart(level, pos);
            }

            player.swing(player.getUsedItemHand());
            if (applied && !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return applied ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
    }

    private static boolean tryApplyBonemealEffects(ItemStack stack, Level level, BlockPos pos, Player player, Direction clickedFace) {
        boolean applied = false;

        if (BoneMealItem.applyBonemeal(stack, level, pos, player)) {
            level.levelEvent(1505, pos, 0);
            applied = true;
        }

        BlockState state = level.getBlockState(pos);
        BlockPos above = pos.relative(clickedFace);
        if (state.isFaceSturdy(level, pos, clickedFace)) {
            if (BoneMealItem.growWaterPlant(stack, level, above, clickedFace)) {
                level.levelEvent(1505, above, 0);
                applied = true;
            }
        }

        return applied;
    }

    private static boolean tryGrowNetherWart(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof NetherWartBlock)) return false;

        int currentAge = state.getValue(NetherWartBlock.AGE);
        if (currentAge >= NetherWartBlock.MAX_AGE) return false;

        int newAge = Math.min(currentAge + 1, NetherWartBlock.MAX_AGE);
        BlockState newState = state.setValue(NetherWartBlock.AGE, newAge);
        level.setBlock(pos, newState, 2);

        level.levelEvent(1505, pos, 0);
        return true;
    }

}
