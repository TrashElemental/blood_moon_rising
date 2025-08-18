package net.trashelemental.blood_moon_rising.entity.custom.blood_moon;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.trashelemental.blood_moon_rising.entity.event.ArteryEvents;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;
import net.trashelemental.blood_moon_rising.magic.effects.events.HemorrhageLogic;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class ArteryEntity extends WoundMob implements GeoEntity {
    public ArteryEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level, 0.8f, 0.2f, "artery");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 22)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 4.5)
                .add(Attributes.ARMOR, 2)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.ATTACK_KNOCKBACK, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this) {
            @Override public boolean canUse() {return !isMimicking() && super.canUse();}
            @Override public boolean canContinueToUse() {return !isMimicking() && super.canContinueToUse();}});
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::isArteryAttackTarget) {
            @Override public boolean canUse() {return !isMimicking() && super.canUse();}
            @Override public boolean canContinueToUse() {return !isMimicking() && super.canContinueToUse();}});
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, false) {
            @Override public boolean canUse() {return !isMimicking() && super.canUse();}
            @Override public boolean canContinueToUse() {return !isMimicking() && super.canContinueToUse();}});
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0) {
            @Override public boolean canUse() {return !isMimicking() && super.canUse();}
            @Override public boolean canContinueToUse() {return !isMimicking() && super.canContinueToUse();}});
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this) {
            @Override public boolean canUse() {return !isMimicking() && super.canUse();}});
        this.goalSelector.addGoal(7, new FloatGoal(this) {
            @Override public boolean canUse() {return !isMimicking() && super.canUse();}});
    }

    boolean isArteryAttackTarget(LivingEntity entity) {
        return !invalidTarget(entity) && (isWoundAttackTarget(entity) || entity instanceof Player);
    }

    //Sound Events
    @Override
    public void playAmbientSound() {
        this.playSound(SoundEvents.DROWNED_AMBIENT, 1.0F, 0.6F);
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.PIGLIN_BRUTE_STEP, 1F, 0.6F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.SLIME_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH;
    }


    //Attacks inflict hemorrhage.
    @Override
    public boolean doHurtTarget(Entity entity) {
        if (!this.level().isClientSide && entity instanceof LivingEntity target) {
            HemorrhageLogic.applyHemorrhage(target, this, 240);
        }
        return super.doHurtTarget(entity);
    }


    //Mimicking - When the Artery first spawns, it will be mimicking a clot. When a player lingers near it for too long
    //or attacks it, it will transform. Any nearby Arteries will also be signalled to transform. It will transform immediately
    //if a nearby entity has Scorn.
    private static final EntityDataAccessor<Boolean> IS_MIMICKING = SynchedEntityData.defineId(ArteryEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> STRESS = SynchedEntityData.defineId(ArteryEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_MIMICKING, true);
        builder.define(STRESS, 120);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsMimicking", this.isMimicking());
        compound.putInt("Stress", this.getStress());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("IsMimicking")) {
            this.setMimicking(compound.getBoolean("IsMimicking"));
        }

        if (compound.contains("Stress")) {
            this.setStress(compound.getInt("Stress"));
        }
    }

    public boolean isMimicking() {
        return this.entityData.get(IS_MIMICKING) || this.isTransforming;
    }
    public void setMimicking(boolean mimicking) {
        this.entityData.set(IS_MIMICKING, mimicking);
    }
    public int getStress() {
        return this.entityData.get(STRESS);
    }
    public void setStress(int value) {
        this.entityData.set(STRESS, value);
    }
    private boolean isTransforming = false;
    private int transformingTicks = 0;

    public boolean isTransforming() {
        return this.isTransforming;
    }

    @Override
    public void tick() {
        super.tick();

        //Tick down transformation timer if transforming
        if (!this.level().isClientSide && isTransforming) {
            if (--transformingTicks <= 0) {
                isTransforming = false;
            }
        }

        if (!this.level().isClientSide && this.isMimicking()) {
            int currentStress = this.getStress();

            boolean nearScorn = false;
            boolean nearPlayer = false;

            //Check nearby entities for Players or entities with Scorn
            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class,
                    this.getBoundingBox().inflate(5), LivingEntity::isAlive)) {
                if (entity.hasEffect(ModMobEffects.SCORN) && !(entity instanceof Player player && player.isCreative())) {
                    nearScorn = true;
                    break;
                }
                if (entity instanceof Player player && !player.isCreative()) {
                    nearPlayer = true;
                }
            }

            //Transform immediately if nearby an entity with Scorn.
            if (nearScorn) {
                transform();
            }

            //Tick down Stress if a player is nearby and transform when at 0.
            else if (nearPlayer) {
                if (currentStress > 0) {
                    setStress(currentStress - 1);
                } else {
                    transform();
                }
            }

            //If no players or Scorn entities are nearby, recover Stress.
            else {
                if (currentStress < 120) {
                    setStress(currentStress + 1);
                }
            }
        }
    }

    //The artery will only take 1 damage while transforming to prevent cheesy kills
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isMimicking() || isTransforming()) {
            amount = 1f;
        }
        return super.hurt(source, amount);
    }

    //Transform if damaged by a player or to below 75% health.
    @Override
    protected void actuallyHurt(DamageSource source, float amount) {
        super.actuallyHurt(source, amount);

        float postDamageHealth = this.getHealth() - amount;
        postDamageHealth = Math.max(postDamageHealth, 0.0f);

        if ((source.getEntity() instanceof Player || postDamageHealth <= this.getMaxHealth() * 0.75f) && isMimicking()) {
            transform();
        }
    }

    private void transform() {
        if (!this.isMimicking() || this.isTransforming) return;

        this.setMimicking(false);
        this.setStress(0);
        this.isTransforming = true;
        this.transformingTicks = 40;

        if (!this.level().isClientSide) {
            this.triggerAnim("transformController", "transform");
        }

        ArteryEvents.ArteryTransformVFX(this);

        //Signal nearby arteries to also transform if they are mimicking.
        this.level().getEntitiesOfClass(ArteryEntity.class, this.getBoundingBox().inflate(10),
                        other -> other != this && other.isMimicking() && !other.isTransforming())
                .forEach(ArteryEntity::transform);
    }

    //Gecko
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "mimicController", 0, this::mimicPredicate));
        controllers.add(new AnimationController<>(this, "movementController", 4, this::movementPredicate));
        controllers.add(new AnimationController<>(this, "transformController", 4, state -> PlayState.STOP)
                .triggerableAnim("transform", RawAnimation.begin().then("TRANSFORM", Animation.LoopType.PLAY_ONCE)));
    }

    private PlayState mimicPredicate(AnimationState<ArteryEntity> state) {
        if (isMimicking() && !isTransforming()) {
            state.getController().setAnimation(RawAnimation.begin().then("MIMIC", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private PlayState movementPredicate(AnimationState<ArteryEntity> state) {
        if (isMimicking() || isTransforming()) return PlayState.STOP;

        ArteryEntity entity = state.getAnimatable();
        boolean actuallyMoving = state.getLimbSwingAmount() > 0.01f || !entity.getNavigation().isDone();

        if (actuallyMoving) {
            state.getController().setAnimation(RawAnimation.begin().then("WALK", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("IDLE", Animation.LoopType.LOOP));
        }

        return PlayState.CONTINUE;
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
