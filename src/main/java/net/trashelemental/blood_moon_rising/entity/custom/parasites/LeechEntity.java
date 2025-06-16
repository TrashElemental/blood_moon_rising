package net.trashelemental.blood_moon_rising.entity.custom.parasites;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class LeechEntity extends ParasiteEntity implements GeoEntity {


    public LeechEntity(EntityType<? extends LeechEntity> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.moveControl = new LeechMoveControl(this);
    }

    static class LeechMoveControl extends SmoothSwimmingMoveControl {
        public LeechMoveControl(LeechEntity leech) {
            super(leech, 0, 5, 0.3F, 1F, false);
        }
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
    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return !this.isSwimming();
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new AmphibiousPathNavigation(this, pLevel);
    }

    public void travel(Vec3 pTravelVector) {
        if (this.isControlledByLocalInstance() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(pTravelVector);
        }
    }

    /**
     * Increases attack range vertically, so it can actually hit targets in the water.
     */
    @Override
    protected AABB getAttackBoundingBox() {
        AABB originalBoundingBox = super.getAttackBoundingBox();

        double extraVerticalRange = 1.5;
        return new AABB(
                originalBoundingBox.minX,
                originalBoundingBox.minY - extraVerticalRange,
                originalBoundingBox.minZ,
                originalBoundingBox.maxX,
                originalBoundingBox.maxY + extraVerticalRange,
                originalBoundingBox.maxZ
        );
    }


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
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.SILVERFISH_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
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
