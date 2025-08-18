package net.trashelemental.blood_moon_rising.entity.custom.blood_moon;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.ai.MolarEatItemsGoal;
import net.trashelemental.blood_moon_rising.entity.event.MolarEvents;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;
import net.trashelemental.blood_moon_rising.util.ModTags;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class MolarEntity extends WoundMob implements GeoEntity {
    public MolarEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level, 0, 0.2f, "molar");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 16)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
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
                10, true, false, entity -> entity.hasEffect(ModMobEffects.SCORN)));

        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1, false));
        this.goalSelector.addGoal(4, new MolarEatItemsGoal(this, 1, 40, ModTags.Items.MOLAR_ACCEPTS));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new FloatGoal(this));
    }

    //Sound Events
    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.TURTLE_AMBIENT_LAND;
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.TURTLE_SHAMBLE, 1F, 1F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.TURTLE_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.TURTLE_DEATH;
    }


    public boolean isTrading = false;
    private int tradingTicks = 0;
    public boolean canEat = true;
    public int eatCooldownTicks = 0;

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {

        ItemStack item = player.getItemInHand(hand);

        //While the player has Kinship, they can 'trade' with a Molar by feeding it certain items.
        if (player.hasEffect(ModMobEffects.KINSHIP) && !this.isAggressive() && !isTrading) {
            if (item.is(ModTags.Items.MOLAR_ACCEPTS) || item.is(ItemTags.MEAT)) {
                MolarEvents.molarTrade(this, item);
                this.isTrading = true;
                this.tradingTicks = 60;
                this.eatCooldownTicks = 200;
                this.canEat = false;
                triggerAnim("chewController", "trade");
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {

        if (--tradingTicks <= 0) {
            this.isTrading = false;
            this.tradingTicks = 0;
        }

        if (--eatCooldownTicks <= 0) {
            this.canEat = true;
            this.eatCooldownTicks = 0;
        }

        super.tick();
    }


    //Textures
    @Override
    public ResourceLocation getTexture() {
        return BloodMoonRising.prefix("textures/entity/" + (isTaintedSkin() ? "molar_tainted" : "molar") + ".png");
    }

    public String getEntityName() {
        if (this.hasCustomName()) {
            return this.getCustomName().getString();
        }
        return this.getType().getDescription().getString();
    }

    public boolean isTaintedSkin() {
        return this.getEntityName().equals("Tainted Love");
    }


    //Gecko
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
        controllers.add(new AnimationController<>(this, "chewController", 0, state -> PlayState.STOP)
                .triggerableAnim("trade", RawAnimation.begin().then("CHEW", Animation.LoopType.PLAY_ONCE)
                        .then("SPIT_UP", Animation.LoopType.PLAY_ONCE)));
        controllers.add(new AnimationController<>(this, "nonTradeChewController", 0, state -> PlayState.STOP)
                .triggerableAnim("eat", RawAnimation.begin().then("CHEW", Animation.LoopType.PLAY_ONCE)));
    }

    private PlayState predicate(AnimationState<MolarEntity> state) {
        MolarEntity entity = state.getAnimatable();

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
