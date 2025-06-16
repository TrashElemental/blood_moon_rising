package net.trashelemental.blood_moon_rising.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.LeechEntity;

public class LeechPathNavigation extends PathNavigation {
    public LeechPathNavigation(LeechEntity leech, Level level) {
        super(leech, level);
    }

    protected PathFinder createPathFinder(int p_217792_) {
        this.nodeEvaluator = new AmphibiousNodeEvaluator(false);
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, p_217792_);
    }

    /**
     * If on ground or swimming and can swim
     */
    protected boolean canUpdatePath() {
        return true;
    }

    protected Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), this.mob.getY(0.5D), this.mob.getZ());
    }

    protected double getGroundY(Vec3 p_217794_) {
        return p_217794_.y;
    }

    /**
     * Checks if the specified entity can safely walk to the specified location.
     */
    protected boolean canMoveDirectly(Vec3 p_217796_, Vec3 p_217797_) {
        return this.mob.isInWater() && isClearForMovementBetween(this.mob, p_217796_, p_217797_, false);
    }

    public boolean isStableDestination(BlockPos p_217799_) {
        return !this.level.getBlockState(p_217799_.below()).isAir();
    }

    public void setCanFloat(boolean p_217801_) {
    }
}