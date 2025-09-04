package net.trashelemental.blood_moon_rising.entity.ai.limb;

import net.minecraft.world.entity.ai.goal.Goal;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.LimbEntity;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.OrganelleEntity;

import java.util.EnumSet;
import java.util.List;

public class PickUpOrganelleGoal extends Goal {
    private final LimbEntity limb;
    private OrganelleEntity target;
    private final double speed;
    private final double searchRadius = 8.0;

    public PickUpOrganelleGoal(LimbEntity limb, double speed) {
        this.limb = limb;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!limb.getPassengers().isEmpty()) return false;

        List<OrganelleEntity> organelles = limb.level().getEntitiesOfClass(
                OrganelleEntity.class,
                limb.getBoundingBox().inflate(searchRadius),
                e -> !e.isPassenger()
        );

        if (!organelles.isEmpty()) {
            target = organelles.get(0);
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && !target.isPassenger() && limb.distanceTo(target) > 0.2;
    }

    @Override
    public void start() {
        limb.getNavigation().moveTo(target, speed);
    }

    @Override
    public void tick() {
        if (target == null) return;

        limb.getLookControl().setLookAt(target);

        if (limb.distanceTo(target) <= 2) {
            target.discard();
            limb.setHasOrganelle(true);
            stop();
        } else {
            limb.getNavigation().moveTo(target, speed);
        }
    }

    @Override
    public void stop() {
        target = null;
        limb.getNavigation().stop();
    }
}