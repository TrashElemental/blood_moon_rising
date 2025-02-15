package net.trashelemental.blood_moon_rising.util.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.trashelemental.blood_moon_rising.magic.effects.event.HemorrhageLogic;

import java.util.List;

public class NihilAttack {

    private static final double RADIUS = 15.0;

    public void performNihilOpening(LivingEntity caster) {
        if (caster == null || caster.level().isClientSide) {
            return;
        }

        BlockPos casterPos = caster.blockPosition();
        Level level = caster.level();

        List<Mob> nearbyMobs = level.getEntitiesOfClass(Mob.class, new AABB(casterPos).inflate(RADIUS));

        for (Mob mob : nearbyMobs) {
            if (mob.equals(caster) || (mob instanceof TamableAnimal && ((TamableAnimal) mob).getOwner() == caster)) {
                continue;
            }

            //Particles
            int particleCount = 3;
            double radius = 1.0;

            for (int i = 0; i < particleCount; i++) {
                double angle = Math.random() * Math.PI * 2;
                double offsetX = radius * Math.cos(angle);
                double offsetZ = radius * Math.sin(angle);
                double offsetY = Math.random() - 0.5;

                double particleX = mob.getX() + offsetX;
                double particleY = mob.getY() + mob.getBbHeight() / 2 + offsetY;
                double particleZ = mob.getZ() + offsetZ;

                ((ServerLevel) level).sendParticles(ParticleTypes.DAMAGE_INDICATOR,
                        particleX, particleY, particleZ,
                        1, 0, 0, 0, 0);
            }
        }
    }

    public void performNihilAttack(LivingEntity caster) {
        if (caster == null || caster.level().isClientSide) {
            return;
        }

        BlockPos casterPos = caster.blockPosition();
        Level level = caster.level();

        List<Mob> nearbyMobs = level.getEntitiesOfClass(Mob.class, new AABB(casterPos).inflate(RADIUS));

        for (Mob mob : nearbyMobs) {
            if (mob.equals(caster) || (mob instanceof TamableAnimal && ((TamableAnimal) mob).getOwner() == caster)) {
                continue;
            }

            //Hemorrhage all applicable mobs
            HemorrhageLogic.hemorrhageActivate(mob, caster);

            //Heal caster for each mob damaged or give Health Boost
            if (!(caster.getHealth() == caster.getMaxHealth())) {
                caster.heal(1.0F);
            } else if (!caster.hasEffect(MobEffects.HEALTH_BOOST)) {
                caster.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 300));
            }

            //Particles
            int steps = 5;
            double deltaX = caster.getX() - mob.getX();
            double deltaY = (caster.getY() - (mob.getY() + mob.getBbHeight() / 2));
            double deltaZ = caster.getZ() - mob.getZ();

            for (int i = 0; i <= steps; i++) {
                double progress = i / (double) steps;
                double particleX = mob.getX() + deltaX * progress;
                double particleY = mob.getY() + deltaY * progress + 0.5;
                double particleZ = mob.getZ() + deltaZ * progress;

                ((ServerLevel) level).sendParticles(ParticleTypes.DAMAGE_INDICATOR,
                        particleX, particleY, particleZ,
                        1, 0, 0, 0, 0);
            }


        }
    }
}
