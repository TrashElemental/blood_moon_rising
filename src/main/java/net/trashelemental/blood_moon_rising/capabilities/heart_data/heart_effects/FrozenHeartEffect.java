package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

/**
 * Gives freezing immunity, attacks and taking damage inflict freezing, crouching will rapidly freeze nearby water.
 */
public class FrozenHeartEffect extends AbstractHeartEffect {

    private static final UUID UUID = java.util.UUID.fromString("9a4c2b1c-bf84-4f11-bf18-e063b14a12ec");

    public FrozenHeartEffect() {
        super(UUID, "Frozen", -2.0);
    }

    @Override
    public void onHurt(Player player, LivingEntity attacker, DamageSource source, float amount) {
        int freezeTicks = attacker.getTicksFrozen();
        attacker.setTicksFrozen(freezeTicks + 280);
        if (freezeTicks > 500 && (!attacker.hasEffect(MobEffects.MOVEMENT_SLOWDOWN))) {
            attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1, false, true));
        }
        super.onHurt(player, attacker, source, amount);
    }

    @Override
    public void onDamage(Player player, LivingEntity target, float amount) {
        int freezeTicks = target.getTicksFrozen();
        target.setTicksFrozen(freezeTicks + 280);
        if (freezeTicks > 500 && (!target.hasEffect(MobEffects.MOVEMENT_SLOWDOWN))) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1, false, true));
        }
        super.onDamage(player, target, amount);
    }

    @Override
    public void onTick(Player player) {
        if (player.getTicksFrozen() > 1) {
            player.setTicksFrozen(0);
        }

        if (!player.level().isClientSide && player.isCrouching()) {
            if (player.tickCount % 10 != 0) return;

            BlockPos center = player.blockPosition();
            int radius = 4;
            RandomSource random = player.getRandom();

            for (BlockPos pos : BlockPos.betweenClosed(
                    center.offset(-radius, -1, -radius),
                    center.offset(radius, 1, radius))) {

                BlockState state = player.level().getBlockState(pos);

                if (state.is(Blocks.WATER) && random.nextFloat() < 0.50f) {
                    player.level().setBlockAndUpdate(pos, Blocks.FROSTED_ICE.defaultBlockState());
                } else if (state.is(Blocks.FROSTED_ICE)) {
                    BlockState refreshed = state.setValue(FrostedIceBlock.AGE, 0);
                    if (state != refreshed) {
                        player.level().setBlockAndUpdate(pos, refreshed);
                    }
                }
            }
        }

        super.onTick(player);
    }
}
