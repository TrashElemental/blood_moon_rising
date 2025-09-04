package net.trashelemental.blood_moon_rising.junkyard_lib.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.junkyard_lib.visual.particle.ParticleMethods;
import org.jetbrains.annotations.Nullable;

/**
 * A 'minion' mob that has a limited lifespan and will despawn when it runs out.
 * Does not send a death message when killed (handled in MinionDeathEvent).
 */

@SuppressWarnings("Deprecated")
public class MinionEntity extends TamableAnimal {

    /**
     * A persistent minion will not despawn via lifespan.
     */
    private boolean isPersistent;
    /**
     * The particles that appear when it is summoned or despawns via lifespan.
     */
    private final ParticleOptions particles;
    /**
     * The sound that plays when it despawns via lifespan.
     */
    private final SoundEvent DespawnSound;

    public MinionEntity(EntityType<? extends TamableAnimal> entityType, Level level, ParticleOptions particles, SoundEvent despawnSound) {
        super(entityType, level);
        this.isPersistent = false;
        this.particles = particles;
        this.DespawnSound = despawnSound;
    }

    public static boolean isTame(MinionEntity entity) {
        return entity.isTame();
    }


    /**
     * Sound Events.
     */
    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.AXOLOTL_IDLE_AIR;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.AXOLOTL_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.AXOLOTL_DEATH;
    }


    /**
     * Cannot breed.
     */
    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }


    /**
     * Minions will not drop experience.
     */
    @Override
    public boolean shouldDropExperience() {
        return false;
    }

    /**
     * Minions will not take damage from owners unless the owner is crouching.
     */
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof LivingEntity attacker) {
            if (this.getOwnerUUID() != null && this.getOwnerUUID().equals(attacker.getUUID())) {
                if (!attacker.isCrouching()) {
                    return false;
                }
            }
        }
        return super.hurt(pSource, pAmount);
    }


    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        ParticleMethods.ParticlesAround(this.level(), particles, this, 10, 0.5);
    }


    /**
     * Unable to collide with its owner.
     */
    @Override
    protected void doPush(Entity other) {
        if (other instanceof Player player && this.isOwnedBy(player)) {
            return;
        }
        super.push(other);
    }


    /**
     * Lifespan, ticking, and despawning behavior.
     */
    private int lifespan = 300;

    public void setLifespan(int lifespan, boolean isPersistent) {
        this.lifespan = lifespan;
        this.isPersistent = isPersistent;
    }

    @Override
    public void tick() {
        super.tick();

        if (!isPersistent && !(this.level().isClientSide)) {
            if (this.lifespan <= 0) {
                ParticleMethods.ParticlesBurst(this.level(), particles, this.getX(), this.getY(), this.getZ(), 5, 0.5);

                this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        DespawnSound, this.getSoundSource(), 1.0F, 3.0F);

                this.discard();
            } else {
                lifespan--;
            }
        }
    }

}
