package net.trashelemental.blood_moon_rising.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.*;

public class LeechEntity extends TamableAnimal implements GeoEntity {

    private static final EntityDataAccessor<Boolean> IS_FULL = SynchedEntityData.defineId(LeechEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_MOVING;

    static {
        DATA_ID_MOVING = SynchedEntityData.defineId(LeechEntity.class, EntityDataSerializers.BOOLEAN);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isFull", this.isFull);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.isFull = compound.getBoolean("isFull");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_FULL, false);
        builder.define(DATA_ID_MOVING, false);
    }

    public LeechEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.isTame = false;
        this.entityData.set(IS_FULL, false);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new FollowOwnerGoal(this, 1.0, 1.0F, 1.0F) {
            @Override
            public boolean canUse() {
                return super.canUse() && isTame && isFull;
            }
        });

        this.goalSelector.addGoal(2, new OwnerHurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && !isFull;
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !isFull;
            }
        });

        this.targetSelector.addGoal(3, new OwnerHurtTargetGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && !isFull;
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !isFull;
            }
        });

        //Non-Tame attack goal
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, false, false) {
            @Override
            public boolean canUse() { return super.canUse() && !isTame; }
        });

        //Tame attack goal
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Monster.class, false, false) {
            @Override
            public boolean canUse() {
                return super.canUse() && isTame && !isFull;
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !isFull;
            }
        });

        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.2, false));

        this.goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.0, 10.0F, 5.0F) {
            @Override
            public boolean canUse() {
                return super.canUse() && isTame && !isFull;
            }
        });

        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    public static boolean isTame(LeechEntity entity) {
        return entity.isTame;
    }

    public static boolean isFull(LeechEntity entity) { return entity.isFull; }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !isFull && super.canAttack(target);
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()

                .add(Attributes.MAX_HEALTH, 3)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.ATTACK_KNOCKBACK, 0);

    }


    //Swimming



    //Sound Events
    @Override
    public SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.15F, 1.0F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.SILVERFISH_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }


    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }


    //Taming
    private boolean isTame;

    @Override
    public boolean isTame() {
        return this.isTame;
    }

    @Override
    public void setTame(boolean tame, boolean applyTamingSideEffects) {
        super.setTame(tame, applyTamingSideEffects);
        this.isTame = tame;
    }


    //Custom Behaviors
    //Owners can't Friendly Fire their Minions unless they're crouching
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


    //De-spawns when its time limit runs out and disappearance effects
    //Also the healing logic for when it returns to its owner while full
    @Override
    public void tick() {
        super.tick();

        if (this.isFull && this.getOwner() != null) {

            double distanceToOwner = this.distanceToSqr(this.getOwner());

            if (distanceToOwner < 1.0) {
                LivingEntity owner = (LivingEntity) this.getOwner();
                if (owner != null) {
                    owner.heal(3.0F);
                }

                this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.BEEHIVE_ENTER, this.getSoundSource(), 0.5F, 1F);
                this.discard();
            }
        }

        if (this.getAge() == 0 && this.isTame) {

            if (this.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 3; i++) {

                    double offsetX = (this.level().random.nextDouble() - 0.5) * 2;
                    double offsetY = this.level().random.nextDouble();
                    double offsetZ = (this.level().random.nextDouble() - 0.5) * 2;

                    serverLevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR,
                            this.getX() + offsetX,
                            this.getY() + 0.5 + offsetY,
                            this.getZ() + offsetZ,
                            1,
                            0, 0, 0, 0);
                }
            }

            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.BEEHIVE_ENTER, this.getSoundSource(), 0.5F, 1F);

            this.discard();
        }
    }


    //When it hurts a target, its 'full' field will be set to true, causing it to return to the player.
    public boolean isFull = false;

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this.isTame) {
            this.entityData.set(IS_FULL, true);
            this.isFull = true;
            this.setTarget(null);
        }
        return super.doHurtTarget(entity);
    }

    //No XP farming for you
    @Override
    public boolean shouldDropExperience() {

        if (this.isTame) {
            return false;
        }

        return super.shouldDropExperience();
    }


    //GeckoLib

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    private PlayState predicate(AnimationState<LeechEntity> state) {

        if(this.isInWater()) {
            state.getController().setAnimation(RawAnimation.begin().then("LEECH_SWIM", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("LEECH_CRAWL", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}

