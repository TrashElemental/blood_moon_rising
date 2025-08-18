package net.trashelemental.blood_moon_rising.entity.custom.blood_moon;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.ai.KeepDistanceGoal;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.BileProjectileEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class BoilEntity extends WoundMob implements GeoEntity {
    public BoilEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level, 0, 0.4f, "boil");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10)
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
                10, true, false, this::isBoilAttackTarget));

        this.goalSelector.addGoal(2, new KeepDistanceGoal<>(this, 1.0, 5.0F, 12.0F));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new FloatGoal(this));
    }

    boolean isBoilAttackTarget(LivingEntity entity) {
        return !invalidTarget(entity) && (isWoundAttackTarget(entity) || entity instanceof Player);
    }

    //Sound Events
    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SPIDER_STEP, 1F, 0.6F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.TROPICAL_FISH_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.TROPICAL_FISH_DEATH;
    }


    //Projectile Attacks
    private int fireCooldown = 0;

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        fireCooldown = 60;
    }

    @Override
    public void tick() {

        LivingEntity target = this.getTarget();
        this.setAggressive(target != null && this.hasLineOfSight(target));

        if (--fireCooldown <= 0 && this.isAlive()) {
            if (this.isAggressive() && !(target == null)) {
                fireProjectileAtTarget(target);
            } else {
                fireProjectileRandom();
            }

        }

        super.tick();
    }

    //Fires 3-4 projectiles loosely around self
    public void fireProjectileRandom() {

        this.fireCooldown = 60 + this.random.nextInt(81);

        int amount = 3 + this.random.nextInt(2);

        for (int i = 0; i < amount; i++) {
            int delay = i * 5;
            BloodMoonRising.queueServerWork(delay, () -> {
                BileProjectileEntity proj = new BileProjectileEntity(this, this.level());
                proj.setPos(this.getX(), this.getEyeY(), this.getZ());

                double angle = this.random.nextDouble() * (2 * Math.PI);

                double distance = 0.2 + this.random.nextDouble() * 0.8;

                double vx = Math.cos(angle) * distance;
                double vz = Math.sin(angle) * distance;
                double vy = 1.0 + this.random.nextDouble() * 0.5;

                proj.shoot(vx, vy, vz, 0.7F, 0.2F);
                this.level().addFreshEntity(proj);

                this.playSound(SoundEvents.FOX_SPIT, 1, 0.4f);
            });
        }
    }

    //Fires a projectile loosely towards an entity the boil is targeting.
    public void fireProjectileAtTarget(LivingEntity target) {
        this.fireCooldown = 60 + this.random.nextInt(31);

        int amount = 3 + this.random.nextInt(2);

        for (int i = 0; i < amount; i++) {
            int delay = i * 5;
            BloodMoonRising.queueServerWork(delay, () -> {
                BileProjectileEntity proj = new BileProjectileEntity(this, this.level());
                proj.setPos(this.getX(), this.getEyeY(), this.getZ());

                double dx = target.getX() - this.getX();
                double dz = target.getZ() - this.getZ();
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist < 0.001) dist = 0.001;

                dx /= dist;
                dz /= dist;

                double clamped = Mth.clamp(dist, 1.0, 12.0);
                float normalized = (float)((clamped - 1.0) / 11.0);
                float force = 0.2f + (float)Math.sqrt(normalized) * 0.6f;

                double vy = 0.2 + force * 0.8 + (this.random.nextDouble() - 0.5) * 0.2;

                double vx = dx + (this.random.nextDouble() - 0.5) * 0.2;
                double vz = dz + (this.random.nextDouble() - 0.5) * 0.2;

                proj.shoot(vx, vy, vz, force, 0.3F);
                this.level().addFreshEntity(proj);

                this.playSound(SoundEvents.FOX_SPIT, 1, 0.6f);
            });
        }
    }

    //Gecko
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    private PlayState predicate(AnimationState<BoilEntity> state) {
        BoilEntity entity = state.getAnimatable();

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
