package net.trashelemental.blood_moon_rising.entity.ai;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.OrganelleEntity;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.TamableWoundMob;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.WoundMob;
import net.trashelemental.blood_moon_rising.junkyard_lib.visual.particle.ParticleMethods;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;

import java.util.Comparator;
import java.util.List;

public class OrganelleMoveTowardsTargetGoal extends Goal {
    private final PathfinderMob mob;
    private LivingEntity target;
    private final double speed;
    private final double radius;

    public OrganelleMoveTowardsTargetGoal(PathfinderMob mob, double speed, double radius) {
        this.mob = mob;
        this.speed = speed;
        this.radius = radius;
    }

    @Override
    public boolean canUse() {
        List<LivingEntity> candidates = mob.level().getEntitiesOfClass(LivingEntity.class,
                mob.getBoundingBox().inflate(radius),
                e -> e.isAlive() && mob.hasLineOfSight(e) && getHealingPriority(e) != -1);


        candidates.sort(Comparator.comparingInt(this::getHealingPriority));
        if (!candidates.isEmpty()) {
            this.target = candidates.get(0);
            return true;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() && target.distanceTo(mob) > 2.0;
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(target, speed);
    }

    @Override
    public void tick() {
        if (target != null && target.isAlive()) {
            mob.getNavigation().moveTo(target, speed);
            mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

            if (mob.distanceToSqr(target) < 2.5D) {
                float healAmount = Math.min(10f, target.getMaxHealth() / 2f);
                target.heal(healAmount);

                mob.level().playSound(null, mob.blockPosition(), SoundEvents.FROGSPAWN_HATCH,
                        SoundSource.NEUTRAL, 2.0f, 1.5f);
                ParticleMethods.ParticlesAroundServerSide(mob.level(), ParticleTypes.HEART,
                        target.getX(), target.getY() + 1, target.getZ(), 5, 2);

                mob.discard();
            }
        }
    }

    @Override
    public void stop() {
        target = null;
        mob.getNavigation().stop();
    }

    private int getHealingPriority(LivingEntity entity) {
        if (!isValidForHealing(entity) || !entity.isAlive()) return -1;

        float healthRatio = entity.getHealth() / entity.getMaxHealth();

        if (healthRatio >= 0.8f) return -1;

        if (healthRatio <= 0.25f) return 0;
        if (healthRatio <= 0.5f) return 1;
        return 2;
    }

    private boolean isValidForHealing(LivingEntity entity) {
        boolean isAlly = entity instanceof WoundMob
                || entity instanceof TamableWoundMob
                || entity.hasEffect(ModMobEffects.KINSHIP);

        return isAlly && !(entity instanceof OrganelleEntity);
    }
}