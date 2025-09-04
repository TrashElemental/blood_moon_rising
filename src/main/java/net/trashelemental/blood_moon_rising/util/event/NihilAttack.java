package net.trashelemental.blood_moon_rising.util.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.trashelemental.blood_moon_rising.junkyard_lib.visual.particle.ParticleMethods;
import net.trashelemental.blood_moon_rising.magic.effects.events.HemorrhageLogic;
import org.joml.Vector3f;

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
            if (mob.equals(caster) || (mob instanceof TamableAnimal tamable && tamable.getOwner() == caster) || !HemorrhageLogic.hemorrhageCanApply(mob)) {
                continue;
            }

            ParticleMethods.ParticlesAroundServerSide(level, getParticles(),
                    mob.getX(), mob.getEyeY(), mob.getZ(), 5, 2);
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
            if (mob.equals(caster) || (mob instanceof TamableAnimal tamable && tamable.getOwner() == caster) || !HemorrhageLogic.hemorrhageCanApply(mob)) {
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

            ParticleMethods.ParticleTrailEntityToEntity(level, getParticles(), mob, caster, 5);
        }
    }

    protected ParticleOptions getParticles() {
        return new DustParticleOptions(new Vector3f(0.6f, 0.02f, 0.02f), 1.1f);
    }
}
