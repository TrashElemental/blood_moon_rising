package net.trashelemental.blood_moon_rising.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class MorselEntity extends TamableAnimal implements RangedAttackMob, GeoEntity {



    public MorselEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.isTame = false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false) {
            @Override
            public boolean canUse() {
                return super.canUse() && !isTame;
            }
        });
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1, (float) 10, (float) 2, false));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new FloatGoal(this));
    }

    public static boolean isTame(MorselEntity entity) {
        return entity.isTame;
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
    public void setTame(boolean tame) {
        super.setTame(tame);
        this.isTame = tame;
    }

    //Custom Behaviors


    //On right click behavior
    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {

        //Eat your Morsels to regain health
        //You monster
        if (this.isOwnedBy(pPlayer)) {

            this.level().playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                    SoundEvents.GENERIC_EAT, this.getSoundSource(), 1.0F, 1.0F);

            for (int i = 0; i < 5; i++) {
                double offsetX = (this.random.nextDouble() - 0.5) * 0.5;
                double offsetY = (this.random.nextDouble() - 0.5) * 0.5;
                double offsetZ = (this.random.nextDouble() - 0.5) * 0.5;
                this.level().addParticle(
                        ParticleTypes.DAMAGE_INDICATOR, this.getX() + offsetX, this.getY() + 0.5 + offsetY, this.getZ() + offsetZ,
                        0.0D, 0.0D, 0.0D);
            }

            pPlayer.heal(1);
            pPlayer.getFoodData().eat(1, 0.1f);
            this.discard();

        }


        return InteractionResult.PASS;
    }


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
    @Override
    public void tick() {
        super.tick();

        if (this.getAge() == 0) {

            if (this.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 5; i++) {

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
                    SoundEvents.WART_BLOCK_BREAK, this.getSoundSource(), 0.5F, 1.0F);

            this.discard();
        }
    }


    //No XP farming for you
    @Override
    public boolean shouldDropExperience() {

        if (this.isTame) {
            return false;
        }

        return super.shouldDropExperience();
    }


    //Ranged Attack
    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {

    }


    //GeckoLib

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
