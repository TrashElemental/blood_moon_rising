package net.trashelemental.blood_moon_rising.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.trashelemental.blood_moon_rising.junkyard_lib.entity.MinionEntity;
import net.trashelemental.blood_moon_rising.junkyard_lib.visual.particle.ParticleMethods;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class MorselEntity extends MinionEntity implements GeoEntity {

    private static final int GRACE_PERIOD_TICKS = 40;
    private int spawnTick;

    public MorselEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level,
                new DustParticleOptions(new Vector3f(0.6f, 0.02f, 0.02f), 1.1f),
                SoundEvents.WART_BLOCK_BREAK);
        this.spawnTick = this.tickCount;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.spawnTick == 0) {
            this.spawnTick = this.tickCount;
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false) {
            @Override
            public boolean canUse() {
                return super.canUse() && !isTame();
            }
        });
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1, (float) 10, (float) 2));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new FloatGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 4)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.ATTACK_KNOCKBACK, 0);
    }


    //On right click behavior
    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        Level level = this.level();

        //Prevents Morsels from being eaten immediately after they are spawned by accident
        if (this.tickCount - this.spawnTick < GRACE_PERIOD_TICKS) {
            return InteractionResult.PASS;
        }

        //Eat your Morsels to regain health and food
        //You monster
        if (this.isOwnedBy(pPlayer)) {

            level.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                    SoundEvents.GENERIC_EAT, this.getSoundSource(), 1.0F, 1.0F);

            ParticleMethods.ParticlesAround(level, ParticleTypes.DAMAGE_INDICATOR, this, 5, 0.5);

            pPlayer.heal(1);
            pPlayer.getFoodData().eat(1, 0.1f);
            this.discard();

        }
        return InteractionResult.SUCCESS;
    }


    //Sound Events
    @Override
    public SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.HUSK_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 1.5f;
    }

    //Gecko
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<MorselEntity> state) {

        if(state.isMoving()) {
            if (this.isAggressive()) {
                state.getController().setAnimation(RawAnimation.begin().then("WALK_ATTACKING", Animation.LoopType.LOOP));
            }
            else state.getController().setAnimation(RawAnimation.begin().then("WALK", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("IDLE", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
