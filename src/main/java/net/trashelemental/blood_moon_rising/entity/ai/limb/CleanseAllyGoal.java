package net.trashelemental.blood_moon_rising.entity.ai.limb;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.LimbEntity;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.OrganelleEntity;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.TamableWoundMob;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.WoundMob;
import net.trashelemental.blood_moon_rising.junkyard_lib.visual.particle.ParticleMethods;

import java.util.ArrayList;
import java.util.List;

public class CleanseAllyGoal extends Goal {
    private final LimbEntity limb;
    private LivingEntity target;
    private final double speed;

    private boolean isParasiteTarget = false;
    private boolean isHealingTarget = false;
    private int scanCooldown = 0;
    private int lingerTime = 0;

    private static final int SCAN_INTERVAL = 40;
    private static final double RANGE = 10.0D;
    private static final double CLEANSING_DISTANCE = 2.0D;
    private static final int LINGER_TICKS = 20;

    public CleanseAllyGoal(LimbEntity limb, double speed) {
        this.limb = limb;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        if (scanCooldown > 0) {
            scanCooldown--;
            return false;
        }
        scanCooldown = SCAN_INTERVAL;

        List<LivingEntity> healingCandidates = new ArrayList<>();
        List<LivingEntity> cleanseCandidates = new ArrayList<>();
        List<WoundMob> parasiteCandidates = new ArrayList<>();

        for (LivingEntity e : limb.level().getEntitiesOfClass(LivingEntity.class, limb.getBoundingBox().inflate(RANGE))) {
            if (e == limb) continue;

            boolean isAlly = false;

            if (limb.isTame() && limb.getOwner() != null) {
                if (e == limb.getOwner()) {
                    isAlly = true;
                } else if (e instanceof TamableAnimal ta && ta.getOwner() == limb.getOwner()) {
                    isAlly = true;
                }
            } else {
                if (e instanceof WoundMob || e instanceof TamableWoundMob) {
                    isAlly = true;
                }
            }

            if (limb.hasOrganelle() && isAlly && e.getHealth() <= e.getMaxHealth() * 0.8f) {
                healingCandidates.add(e);
            }

            if (isAlly && hasHarmfulEffect(e)) {
                cleanseCandidates.add(e);
            }

            if (!limb.isTame() && e instanceof WoundMob wm && wm.getParasiteChance() > 0 && !(wm instanceof OrganelleEntity)) {
                parasiteCandidates.add(wm);
            }
        }

        if (!healingCandidates.isEmpty()) {
            target = healingCandidates.get(limb.getRandom().nextInt(healingCandidates.size()));
            isHealingTarget = true;
            isParasiteTarget = false;
            return true;
        }

        if (!cleanseCandidates.isEmpty()) {
            target = cleanseCandidates.get(limb.getRandom().nextInt(cleanseCandidates.size()));
            isHealingTarget = false;
            isParasiteTarget = false;
            return true;
        }

        if (!parasiteCandidates.isEmpty()) {
            target = parasiteCandidates.get(limb.getRandom().nextInt(parasiteCandidates.size()));
            isHealingTarget = false;
            isParasiteTarget = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (target == null || !target.isAlive()) return false;
        if (isHealingTarget && !limb.hasOrganelle()) return false;
        return isParasiteTarget || isHealingTarget || hasHarmfulEffect(target);
    }

    @Override
    public void start() {
        lingerTime = 0;
    }

    @Override
    public void stop() {
        target = null;
        isParasiteTarget = false;
        isHealingTarget = false;
        limb.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (target == null) return;

        limb.getLookControl().setLookAt(target, 30.0F, 30.0F);
        double distanceSq = limb.distanceToSqr(target);

        if (distanceSq > CLEANSING_DISTANCE * CLEANSING_DISTANCE) {
            limb.getNavigation().moveTo(target, speed);
        } else {
            lingerTime++;
            if (lingerTime % 10 == 0) {
                Vec3 pos = target.position();
                double angle = limb.getRandom().nextDouble() * 2 * Math.PI;
                double radius = CLEANSING_DISTANCE * 0.8;
                Vec3 moveTo = new Vec3(
                        pos.x + Math.cos(angle) * radius,
                        pos.y,
                        pos.z + Math.sin(angle) * radius
                );
                limb.getNavigation().moveTo(moveTo.x, moveTo.y, moveTo.z, speed * 0.8);
            }

            if (lingerTime >= LINGER_TICKS) {
                if (isHealingTarget && limb.hasOrganelle()) {
                    target.heal(target.getMaxHealth() / 2f);
                    limb.setHasOrganelle(false);
                } else if (isParasiteTarget && target instanceof WoundMob wm) {
                    wm.setParasiteChance(0f);
                } else if (!isHealingTarget && !isParasiteTarget) {
                    cleanse(target);
                }

                ParticleMethods.ParticlesAroundServerSide(
                        limb.level(), ParticleTypes.HAPPY_VILLAGER,
                        target.getX(), target.getY() + 1, target.getZ(), 10, 2
                );
                stop();
            }
        }
    }

    private void cleanse(LivingEntity entity) {
        List<MobEffectInstance> toRemove = new ArrayList<>();
        for (MobEffectInstance effect : entity.getActiveEffects()) {
            if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                toRemove.add(effect);
            }
        }
        for (MobEffectInstance effect : toRemove) {
            entity.removeEffect(effect.getEffect());
        }
    }

    private boolean hasHarmfulEffect(LivingEntity entity) {
        for (MobEffectInstance effect : entity.getActiveEffects()) {
            if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                return true;
            }
        }
        return false;
    }
}