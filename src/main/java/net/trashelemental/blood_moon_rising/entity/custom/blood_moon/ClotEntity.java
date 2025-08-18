package net.trashelemental.blood_moon_rising.entity.custom.blood_moon;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.magic.effects.events.HemorrhageLogic;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class ClotEntity extends WoundMob implements GeoEntity {
    public ClotEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level, 0, 0.4f, "clot");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 12)
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.ATTACK_KNOCKBACK, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class,
                10, true, false, this::isClotAttackTarget));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1, false));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new FloatGoal(this));
    }

    boolean isClotAttackTarget(LivingEntity entity) {
        return !invalidTarget(entity) && (
                isWoundAttackTarget(entity) ||
                        entity instanceof Player ||
                        entity instanceof Villager ||
                        entity instanceof Zombie ||
                        entity instanceof Skeleton
        );
    }

    //Sound Events
    @Override
    public void playAmbientSound() {
        this.playSound(SoundEvents.DROWNED_AMBIENT, 1.0F, 0.6F);
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.15F, 0.6F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.SLIME_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH;
    }


    //Attacks have a 50% chance to inflict hemorrhage.
    @Override
    public boolean doHurtTarget(Entity entity) {
        if (!this.level().isClientSide && entity instanceof LivingEntity target) {
            if (this.random.nextFloat() < 0.5f) {
                HemorrhageLogic.applyHemorrhage(target, this, 240);
            }
        }
        return super.doHurtTarget(entity);
    }


    //Gecko
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    private PlayState predicate(AnimationState<ClotEntity> state) {
        ClotEntity entity = state.getAnimatable();

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
