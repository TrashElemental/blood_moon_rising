package net.trashelemental.blood_moon_rising.entity.custom.blood_moon;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
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
import net.trashelemental.blood_moon_rising.entity.ai.DestroyBlocksGoal;
import net.trashelemental.blood_moon_rising.entity.ai.MolarEatItemsGoal;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.ParasiteEntity;
import net.trashelemental.blood_moon_rising.entity.event.MouthEvents;
import net.trashelemental.blood_moon_rising.util.ModTags;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class MouthEntity extends WoundMob implements GeoEntity {
    public MouthEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level, 0, 0.4f, "mouth");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.ATTACK_KNOCKBACK, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class,
                10, true, false, this::isMouthAttackTarget));

        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1, false));
        this.goalSelector.addGoal(4, new MolarEatItemsGoal(this, 1, 20, ModTags.Items.MOLAR_ACCEPTS) {
                @Override public void consumeItem() {
                    increaseStomach();
                    super.consumeItem();
                }});
        this.goalSelector.addGoal(5, new DestroyBlocksGoal(this, 1, 20, BlockTags.CROPS) {
            @Override public void doSideEffects() {
                increaseStomach();
                super.doSideEffects();
            }});
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new FloatGoal(this));
    }

    boolean isMouthAttackTarget(LivingEntity entity) {
        return !invalidTarget(entity) && (
                isWoundAttackTarget(entity) ||
                        entity instanceof Player ||
                        (entity instanceof Animal animal && (!(animal instanceof ParasiteEntity)))
        );
    }

    //Sound Events
    @Override
    public void playAmbientSound() {
        this.playSound(SoundEvents.HORSE_AMBIENT, 1.0F, 0.4F);
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.HOGLIN_STEP, 1F, 0.6F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.TURTLE_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.TURTLE_DEATH;
    }


    //Stomach mechanics
    private int stomach = 0;
    private int getStomach() {
        return stomach;
    }
    private void setStomach(int amount) {
        this.stomach = amount;
    }

    private int isChewingCountdown = 80;
    private boolean isChewing = false;

    //Spawns some reinforcements or another nuisance when the stomach is full.
    private void spawnReinforcements() {
        triggerAnim("chewController", "chew");
        MouthEvents.spawnReinforcements(this);
    }

    //Eating crops and items will fill the stomach by 1 or 2.
    private void increaseStomach() {
        setStomach(stomach + 1 + this.random.nextInt(2));
    }

    //Killing entities will fill it by an amount equal to the entity's max hp.
    private void increaseStomachByTargetMaxHealth(LivingEntity entity) {
        float increase = entity.getMaxHealth();
        setStomach(getStomach() + (int) increase);
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity entity) {
        increaseStomachByTargetMaxHealth(entity);
        return super.killedEntity(level, entity);
    }

    @Override
    public void tick() {
        super.tick();

        if (isChewing) {
            if (--isChewingCountdown <= 0) {
                isChewingCountdown = 80;
                isChewing = false;
            }
            this.getNavigation().stop();
        }

        if (getStomach() >= 10) {
            stomach = 0;
            isChewing = true;
            spawnReinforcements();
        }
    }

    //Gecko
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
        controllers.add(new AnimationController<>(this, "chewController", 0, state -> PlayState.STOP)
                .triggerableAnim("chew", RawAnimation.begin().then("CHEW", Animation.LoopType.PLAY_ONCE)
                        .then("SPIT_UP", Animation.LoopType.PLAY_ONCE)));
    }

    private PlayState predicate(AnimationState<MouthEntity> state) {
        MouthEntity entity = state.getAnimatable();

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
