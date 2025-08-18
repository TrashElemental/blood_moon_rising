package net.trashelemental.blood_moon_rising.entity.custom.blood_moon;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.capabilities.AttachmentsRegistry;
import net.trashelemental.blood_moon_rising.capabilities.hearts.HeartData;
import net.trashelemental.blood_moon_rising.entity.event.LesionEvents;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.Optional;

public class LesionEntity extends WoundMob implements GeoEntity {
    public LesionEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level, 0, 0.4f, "lesion");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 35)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 14)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2)
                .add(Attributes.ATTACK_KNOCKBACK, 1);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this) {
            @Override public boolean canUse() {return !isShuddering() && !isHeartless() && super.canUse();}});
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class,
                10, true, false, this::isLesionAttackTarget) {
            @Override public boolean canUse() {return !isShuddering() && !isHeartless() && super.canUse();}});

        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1, false) {
            @Override public boolean canUse() {return !isShuddering() && !isHeartless() && super.canUse();}});
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8) {
            @Override public boolean canUse() {return !isHeartless() && super.canUse();}});
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1) {
            @Override public boolean canUse() {return !isShuddering() && !isHeartless() && super.canUse();}});
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this) {
            @Override public boolean canUse() {return !isShuddering() && !isHeartless() && super.canUse();}});
        this.goalSelector.addGoal(7, new FloatGoal(this));
    }

    boolean isLesionAttackTarget(LivingEntity entity) {
        return !invalidTarget(entity) && (isWoundAttackTarget(entity));
    }

    //Sound Events
    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_HORSE_AMBIENT;
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ZOGLIN_STEP, 1F, 0.6F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.ZOMBIE_HORSE_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_HORSE_DEATH;
    }


    //Attacks inflict Health Down if the entity does not already have it.
    @Override
    public boolean doHurtTarget(Entity entity) {
        BloodMoonRising.queueServerWork(10, () -> {
            super.doHurtTarget(entity);
            LesionEvents.LesionHurtTarget(entity);
        });
        return false;
    }

    //If the Lesion is shuddering when it is attacked, it will only take 1 damage.
    @Override
    protected void actuallyHurt(DamageSource pDamageSource, float pDamageAmount) {
        if (shudderingTicks > 0) {
            super.actuallyHurt(pDamageSource, 1);
            return;
        }
        super.actuallyHurt(pDamageSource, pDamageAmount);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {

            //If Heart has been stolen, decrement special death timer and kill at the end (to coincide with animation)
            if (isHeartless && --specialDeathTicks <= 0) {
                remove(RemovalReason.KILLED);
                return;
            }

            //If shuddering, look at target.
            if (isShuddering) {
                this.getNavigation().stop();
                LivingEntity target = this.getTarget();
                if (target != null) {
                    this.getLookControl().setLookAt(target, 30.0f, 30.0f);
                }

                if (--shudderingTicks <= 0) {
                    isShuddering = false;

                    if (target != null && !this.isAggressive()) {
                        this.setLastHurtByMob(target);
                        this.setAggressive(true);
                    }
                }
                return;
            }

            if (hasShudderedRecently) {
                if (--shudderCooldownTicks <= 0) {
                    hasShudderedRecently = false;
                }
            }

            //If not aggressive or shuddering, look for nearby entities with Scorn or players with Hearts equipped
            //If either are detected, become aggressive and start shuddering.
            if (!this.isAggressive() && !isShuddering && (this.tickCount % 20 == 0)) {
                for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(16), LivingEntity::isAlive)) {

                    if (entity.hasEffect(ModMobEffects.SCORN) && !(entity instanceof Player p && p.isCreative())) {
                        this.setTarget(entity);
                        shudder();
                        break;
                    }

                    if (entity instanceof Player player && !player.isCreative()) {
                        HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);
                        if (!data.getActiveHearts().isEmpty()) {
                            this.setTarget(player);
                            shudder();
                            break;
                        }
                    }
                }
            }
        }
    }


    //When not aggressive, the Lesion can be interacted with for a 1% chance to steal its heart and instantly kill it.
    //Scales with the player's luck stat.
    private boolean isHeartless = false;
    public boolean isHeartless() {
        return this.isHeartless;
    }
    public int specialDeathTicks = 0;

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {

        if (!this.isAggressive() && !this.isHeartless()) {

            AttributeInstance luckAttr = player.getAttribute(Attributes.LUCK);
            double luck = luckAttr != null ? luckAttr.getValue() : 0;
            float stealChance = (float)(0.01 + (0.05 * luck));

            if (this.random.nextFloat() <= stealChance) {
                this.isHeartless = true;
                this.specialDeathTicks = 30;
                LesionEvents.LesionStealHeart(this, player);

            } else {
                if (!player.isCreative()) {
                    this.setTarget(player);
                    this.setLastHurtByMob(player);
                    this.setAggressive(true);
                }
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }


    //Shuddering logic
    private boolean isShuddering = false;
    private int shudderingTicks = 0;
    private boolean hasShudderedRecently = false;
    private int shudderCooldownTicks = 0;

    public boolean isShuddering() {
        return this.isShuddering;
    }

    public void shudder() {
        if (hasShudderedRecently) return;

        this.isShuddering = true;
        this.shudderingTicks = 80;
        this.hasShudderedRecently = true;
        this.shudderCooldownTicks = 2400;

        if (!this.level().isClientSide) {
            this.triggerAnim("shudderController", "shudder");
            this.playSound(SoundEvents.ZOMBIE_HORSE_AMBIENT, 2, 0.6f);
        }
    }


    //Textures
    @Override
    public ResourceLocation getTexture() {
        return BloodMoonRising.prefix("textures/entity/" + (isHeartless()
                        ? (isMinosSkin() ? "lesion_minos_heartless" : "lesion_heartless")
                        : (isMinosSkin() ? "lesion_minos"          : "lesion")) + ".png");
    }

    public ResourceLocation getGlowmaskTexture() {
        String mood     = isAggressive() ? "hostile" : "peaceful";
        String variant  = isMinosSkin() ? "_minos"   : "";
        return BloodMoonRising.prefix("textures/entity/lesion_" + mood + variant + "_glowmask.png");
    }

    public String getEntityName() {
        if (this.hasCustomName()) {
            return this.getCustomName().getString();
        }
        return this.getType().getDescription().getString();
    }

    public boolean isMinosSkin() {
        return this.getEntityName().equals("Judge");
    }


    //Gecko
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movementController", 4, this::mainPredicate));
        controllers.add(new AnimationController<GeoAnimatable>(this, "attackController", 4, this::attackPredicate));
        controllers.add(new AnimationController<>(this, "shudderController", 0, state -> PlayState.STOP)
                .triggerableAnim("shudder", RawAnimation.begin().then("SHUDDER", Animation.LoopType.PLAY_ONCE)));
        controllers.add(new AnimationController<>(this, "specialDeathController", 0, state -> PlayState.STOP)
                .triggerableAnim("special_death", RawAnimation.begin().then("SPECIAL_DEATH", Animation.LoopType.PLAY_ONCE)));
    }

    private PlayState mainPredicate(AnimationState<LesionEntity> state) {
        LesionEntity entity = state.getAnimatable();
        if (entity.isShuddering()) return PlayState.STOP;

        boolean actuallyMoving = state.getLimbSwingAmount() > 0.01f || !entity.getNavigation().isDone();

        if (actuallyMoving) {
            state.getController().setAnimation(RawAnimation.begin().then("WALK", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("IDLE", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationState<GeoAnimatable> state) {
        if (this.swinging && state.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            state.getController().forceAnimationReset();
            String attackAnim = this.getRandom().nextBoolean() ? "SWIPE" : "SLAM";
            state.getController().setAnimation(RawAnimation.begin().then(attackAnim, Animation.LoopType.PLAY_ONCE));
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
