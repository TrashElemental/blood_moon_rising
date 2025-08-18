package net.trashelemental.blood_moon_rising.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class KeepDistanceGoal<T extends Mob> extends Goal {
    private final T mob;
    private final double speed;
    private final float minDistance;
    private final float maxDistance;

    public KeepDistanceGoal(T mob, double speed, float minDistance, float maxDistance) {
        this.mob = mob;
        this.speed = speed;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null) return;

        double distSq = mob.distanceToSqr(target);

        if (distSq < (minDistance * minDistance)) {
            Vec3 away = mob.position().subtract(target.position()).normalize().scale(minDistance * 1.5);
            mob.getNavigation().moveTo(mob.getX() + away.x, mob.getY(), mob.getZ() + away.z, speed);
        } else if (distSq > (maxDistance * maxDistance)) {
            mob.getNavigation().moveTo(target, speed);
        } else {
            mob.getNavigation().stop();
        }
    }
}
