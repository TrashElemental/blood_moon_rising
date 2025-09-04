package net.trashelemental.blood_moon_rising.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class DestroyBlocksGoal extends Goal {
    private final PathfinderMob mob;
    private final double speed;
    private final int cooldown;
    private final TagKey<Block> acceptedBlocks;
    private BlockPos targetBlock;
    private int actionCooldown;

    public DestroyBlocksGoal(PathfinderMob mob, double speed, int cooldownTicks, TagKey<Block> acceptedBlocks) {
        this.mob = mob;
        this.speed = speed;
        this.cooldown = cooldownTicks;
        this.acceptedBlocks = acceptedBlocks;
    }

    @Override
    public boolean canUse() {
        if (actionCooldown > 0) {
            actionCooldown--;
            return false;
        }

        int range = 8;
        BlockPos mobPos = mob.blockPosition();
        Level level = mob.level();

        outer:
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos checkPos = mobPos.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);
                    if (state.is(acceptedBlocks)) {
                        targetBlock = checkPos;
                        break outer;
                    }
                }
            }
        }

        return targetBlock != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetBlock == null) return false;
        return mob.level().getBlockState(targetBlock).is(acceptedBlocks);
    }

    @Override
    public void start() {
        moveToTarget();
    }

    @Override
    public void tick() {
        if (targetBlock == null) return;

        mob.getLookControl().setLookAt(Vec3.atCenterOf(targetBlock));

        double reach = 1.5 + mob.getBbWidth();
        double distanceSq = mob.distanceToSqr(Vec3.atCenterOf(targetBlock));

        if (distanceSq <= reach * reach) {
            destroyBlock();
        } else {
            moveToTarget();
        }
    }

    @Override
    public void stop() {
        targetBlock = null;
        mob.getNavigation().stop();
    }

    private void moveToTarget() {
        if (targetBlock != null) {
            mob.getNavigation().moveTo(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), speed);
        }
    }

    private void destroyBlock() {
        Level level = mob.level();
        if (targetBlock != null && level.getBlockState(targetBlock).is(acceptedBlocks)) {
            level.destroyBlock(targetBlock, true);
            actionCooldown = cooldown;
            doSideEffects();
        }
        targetBlock = null;
    }

    public void doSideEffects() {

    }
}