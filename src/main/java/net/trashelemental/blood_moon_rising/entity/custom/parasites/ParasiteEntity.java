package net.trashelemental.blood_moon_rising.entity.custom.parasites;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.entity.ai.ModFollowOwnerGoal;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.junkyard_lib.entity.MinionEntity;
import org.joml.Vector3f;

public class ParasiteEntity extends MinionEntity {

    private static final EntityDataAccessor<Boolean> IS_FULL = SynchedEntityData.defineId(ParasiteEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_RARE_SPAWN = SynchedEntityData.defineId(ParasiteEntity.class, EntityDataSerializers.BOOLEAN);

    public ParasiteEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level,
                new DustParticleOptions(new Vector3f(0.6f, 0.02f, 0.02f), 1.1f),
                SoundEvents.BEEHIVE_ENTER);
        this.entityData.set(IS_RARE_SPAWN, false);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_FULL, false);
        this.entityData.define(IS_RARE_SPAWN, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isFull", this.entityData.get(IS_FULL));
        compound.putBoolean("isRareSpawn", this.entityData.get(IS_RARE_SPAWN));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(IS_FULL, compound.getBoolean("isFull"));
        this.entityData.set(IS_RARE_SPAWN, compound.getBoolean("isRareSpawn"));
    }

    public boolean isFull() {
        return this.entityData.get(IS_FULL);
    }

    public void setFull(boolean full) {
        this.entityData.set(IS_FULL, full);
    }

    /**
     * There is a small chance for a 'rare' parasite to spawn
     * that will flee players on sight. If slain, it will drop a heart.
     * They cannot spawn from player-caused means, only natural spawning from Wound mobs.
     */
    public boolean isRareSpawn() { return this.entityData.get(IS_RARE_SPAWN); }

    public void setRareSpawn(boolean rareSpawn) { this.entityData.set(IS_RARE_SPAWN, rareSpawn); }


    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 3)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.ATTACK_KNOCKBACK, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        /*
          Return to owner immediately if tamed and an entity has been hit already.
         */
        this.goalSelector.addGoal(1, new ModFollowOwnerGoal(this, 1.0, 1.0F, 1.0F, canFly()) {
            @Override
            public boolean canUse() {
                return super.canUse() && isTame() && isFull();
            }
        });

        /*
          Return to owner immediately if tamed and an entity has been hit already.
         */
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 12.0F, 1.2, 1.5,
                entity -> isRareSpawn()));

        /*
          Owner defense/offense goals
         */
        this.goalSelector.addGoal(2, new OwnerHurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && !isFull();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !isFull();
            }
        });

        this.targetSelector.addGoal(3, new OwnerHurtTargetGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && !isFull();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !isFull();
            }
        });

        /*
          Non-tame attack goal, will attack players
         */
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, false, false) {
            @Override
            public boolean canUse() { return super.canUse() && !isTame(); }
        });

        /*
          Tame attack goal, will attack monsters
         */
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Monster.class, false, false) {
            @Override
            public boolean canUse() {
                return super.canUse() && isTame() && !isFull();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !isFull();
            }
        });

        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.2, false));

        /*
          Normal follow owner goal.
         */
        this.goalSelector.addGoal(7, new ModFollowOwnerGoal(this, 1.0, 10.0F, 5.0F, canFly()) {
            @Override
            public boolean canUse() {
                return super.canUse() && isTame() && !isFull();
            }
        });

        /*
          Idle Behaviors
         */
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    public boolean canFly() { return false; }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !this.isFull() && super.canAttack(target);
    }

    /**
     * When a tamed parasite damages an entity, marks the parasite as full,
     * which causes it to return to its owner.
     */
    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean success = super.doHurtTarget(entity);
        if (success && this.isTame()) {
            this.setFull(true);
            this.setTarget(null);
        }
        return success;
    }

    /**
     * When returning to its owner after hurting an entity, it will despawn after reaching a
     * certain proximity, and heal the owner.
     */
    @Override
    public void tick() {
        super.tick();

        if (this.isFull() && this.getOwner() != null) {

            double dx = this.getX() - this.getOwner().getX();
            double dz = this.getZ() - this.getOwner().getZ();
            double dy = Math.abs(this.getY() - this.getOwner().getY());

            if (dy < 1.5D && (dx * dx + dz * dz) < 1.0D) {
                LivingEntity owner = this.getOwner();
                if (owner != null) {
                    owner.heal(3.0F);
                }

                this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.BEEHIVE_ENTER, this.getSoundSource(), 0.5F, 1F);
                this.discard();
            }
        }
    }

    /**
     * If a 'rare' Parasite dies, it will drop a Heart.
     */
    @Override
    public void onRemovedFromWorld() {

        if (this.getRemovalReason() == RemovalReason.KILLED && this.isRareSpawn()) {
            spawnAtLocation(ModItems.HEART.get());
        }

        super.onRemovedFromWorld();
    }
}
