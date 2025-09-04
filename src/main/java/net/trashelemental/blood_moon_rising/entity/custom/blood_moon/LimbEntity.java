package net.trashelemental.blood_moon_rising.entity.custom.blood_moon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.trashelemental.blood_moon_rising.entity.ai.limb.CleanseAllyGoal;
import net.trashelemental.blood_moon_rising.entity.ai.limb.CollectItemsGoal;
import net.trashelemental.blood_moon_rising.entity.ai.limb.PickUpOrganelleGoal;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.ParasiteEntity;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.junkyard_lib.entity.TamableEntity;
import net.trashelemental.blood_moon_rising.junkyard_lib.visual.particle.ParticleMethods;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.ArrayList;
import java.util.List;

public class LimbEntity extends TamableWoundMob implements GeoEntity {
    public LimbEntity(EntityType<? extends TamableEntity> type, Level level) {
        super(type, level, ModItems.PARASITE_EGGS.get(), null, 0, 0, "limb");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.ATTACK_KNOCKBACK, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class,
                10, true, false, this::isLimbAttackTarget) {
            @Override
            public boolean canUse() {
                if (!(behavior.equals(Behavior.WANDER))) return false;
                return super.canUse();
            }
        });
        this.targetSelector.addGoal(3, new OwnerHurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                if (!(behavior.equals(Behavior.FOLLOW))) return false;
                return super.canUse();
            }
        });
        this.targetSelector.addGoal(4, new OwnerHurtTargetGoal(this) {
            @Override
            public boolean canUse() {
                if (!(behavior.equals(Behavior.FOLLOW))) return false;
                return super.canUse();
            }
        });

        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Player.class, 12.0F, 1D, 1.1D,
                player -> this.shouldAvoid()));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1, 10f, 2f, false) {
            @Override
            public boolean canUse() {
                if (!(behavior.equals(Behavior.FOLLOW))) return false;
                return super.canUse();
            }
        });
        this.goalSelector.addGoal(7, new TemptGoal(this, 1, Ingredient.of(ModItems.PARASITE_EGGS.get()),  false) {
            @Override
            public boolean canUse() {
                if (!(behavior.equals(Behavior.WANDER))) return false;
                return super.canUse();
            }
        });

        this.goalSelector.addGoal(8, new CleanseAllyGoal(this, 1) {
            @Override
            public boolean canUse() {
                if (behavior.equals(Behavior.STAY)) return false;
                return super.canUse();
            }
        });
        this.goalSelector.addGoal(9, new MeleeAttackGoal(this, 1, false));
        this.goalSelector.addGoal(10, new PickUpOrganelleGoal(this, 1) {
            @Override
            public boolean canUse() {
                if (behavior.equals(Behavior.STAY) || hasOrganelle()) return false;
                return super.canUse();
            }
        });
        this.goalSelector.addGoal(11, new CollectItemsGoal(this));

        this.goalSelector.addGoal(12, new RandomStrollGoal(this, 1) {
            @Override
            public boolean canUse() {
                if (behavior.equals(Behavior.STAY)) return false;
                return super.canUse();
            }
        });
        this.goalSelector.addGoal(13, new LookAtPlayerGoal(this, Player.class, (float) 6));
        this.goalSelector.addGoal(14, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(15, new FloatGoal(this));
    }

    boolean isLimbAttackTarget(LivingEntity entity) {
        return (!invalidTarget(entity) && (entity.hasEffect(ModMobEffects.SCORN.get()) || entity instanceof ParasiteEntity) && behavior.equals(Behavior.WANDER));
    }

    boolean shouldAvoid() {
        return (!this.isTame() && this.hasCarriedItem());
    }

    public boolean canCollect() {
        return this.isTame() && this.isWandering() && (this.getTarget() == null) && !this.hasCarriedItem();
    }

    //Sound Events
    @Override
    public void playAmbientSound() {
        this.playSound(SoundEvents.STRIDER_AMBIENT, 0.5F, 1.1F);
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.FROG_STEP, 1F, 0.6F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.STRIDER_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.STRIDER_DEATH;
    }

    //Increases max health when tamed.
    @Override
    protected void onTamed(Player owner) {
        super.onTamed(owner);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0);
        this.setHealth(40.0f);
    }


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ItemStack held = this.getCarriedItem();

        //Can only be tamed while the interacting player has Kinship.
        if (!this.isTame() && itemstack.is(ModItems.PARASITE_EGGS.get())) {
            if (!player.hasEffect(ModMobEffects.KINSHIP.get())) return InteractionResult.PASS;
        }

        //Don't pick up items or anything if the player is crouching.
        //Have this trigger some happy heart particles and the wave animation later.
        if (player.isCrouching()) {
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        //If holding an item, it can be interacted with by its owner to take it.
        if (!this.level().isClientSide && this.isTame() && this.isOwnedBy(player) && !held.isEmpty()) {
            ItemStack copy = held.copy();
            boolean added = player.addItem(copy);
            if (!added) {
                ItemEntity it = new ItemEntity(this.level(), player.getX(), player.getY(), player.getZ(), copy);
                this.level().addFreshEntity(it);
            }
            this.setCarriedItem(ItemStack.EMPTY);
            this.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }


    @Override
    public boolean doHurtTarget(Entity target) {
        boolean flag = super.doHurtTarget(target);
        if (!flag) return false;

        //On a hit, it has a 75% chance to steal an item.
        if (!this.level().isClientSide && !this.hasCarriedItem() && canSteal && this.random.nextFloat() < 0.75F) {

            //For players, can steal a random item from their inventory on a hit.
            //This doesn't include anything in the main or off-hand, or armor slots.
            //Clears its target after stealing an item from the player, so it can start running away.
            if (target instanceof Player player) {
                NonNullList<ItemStack> inventory = player.getInventory().items;
                List<Integer> filledSlots = new ArrayList<>();
                int selected = player.getInventory().selected;

                for (int i = 0; i < inventory.size(); i++) {
                    if (!inventory.get(i).isEmpty() && i != selected) {
                        filledSlots.add(i);
                    }
                }

                if (!filledSlots.isEmpty()) {
                    int randomSlot = filledSlots.get(this.random.nextInt(filledSlots.size()));
                    ItemStack stolen = inventory.get(randomSlot).copy();
                    this.setCarriedItem(stolen);
                    inventory.set(randomSlot, ItemStack.EMPTY);
                }

                this.setTarget(null);
            }

            //For non-players, it can steal a random item from the target's loot pool.
            else if (target instanceof LivingEntity living) {
                ServerLevel serverLevel = (ServerLevel) this.level();
                ResourceLocation lootTableKey = living.getLootTable();
                LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(lootTableKey);

                LootParams lootParams = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.THIS_ENTITY, living)
                        .withParameter(LootContextParams.ORIGIN, living.position())
                        .withOptionalParameter(LootContextParams.DAMAGE_SOURCE, living.damageSources().magic())
                        .create(LootContextParamSets.ENTITY);

                List<ItemStack> generatedLoot = lootTable.getRandomItems(lootParams);

                if (!generatedLoot.isEmpty()) {
                    ItemStack stolen = generatedLoot.get(this.random.nextInt(generatedLoot.size())).copy();
                    this.setCarriedItem(stolen);
                }
            }

            this.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
            this.canSteal = false;
            this.stealCooldown = 140;

        }
        return true;
    }


    @Override
    protected void actuallyHurt(DamageSource pDamageSource, float pDamageAmount) {

        //Drops any item it is holding when damaged.
        if (!this.level().isClientSide && this.hasCarriedItem()) {
            ItemStack carried = this.getCarriedItem();
            this.setCarriedItem(ItemStack.EMPTY);

            ItemEntity drop = new ItemEntity(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), carried);
            drop.setDefaultPickUpDelay();
            this.level().addFreshEntity(drop);
        }

        super.actuallyHurt(pDamageSource, pDamageAmount);
    }

    private int stealCooldown = 0;
    private boolean canSteal = true;

    @Override
    public void tick() {
        super.tick();

        if (stealCooldown > 0) {
            stealCooldown--;
            if (stealCooldown <= 0) {
                canSteal = true;
            }
        }

        //If it needs healing and is carrying an Organelle, it will use it.
        if (this.getHealth() <= (this.getMaxHealth() * 0.5) && !this.level().isClientSide) {
            if (this.entityData.get(HAS_ORGANELLE)) {
                this.heal(this.getMaxHealth() / 2f);
                this.setHasOrganelle(false);
                this.level().playSound(null, this.blockPosition(), SoundEvents.FROGSPAWN_HATCH,
                        SoundSource.NEUTRAL, 2.0f, 1.5f);
                ParticleMethods.ParticlesAroundServerSide(this.level(), ParticleTypes.HEART,
                        this.getX(), this.getY() + 1, this.getZ(), 5, 2);
            }
        }

        //Chance to play a funny lil' wave animation while it's staying.
        if (isStaying()) {
            tickWave();
        }

    }

    private int waveCooldown = 0;
    private static final int WAVE_INTERVAL = 60;
    private static final float WAVE_CHANCE = 0.01f;

    public void tickWave() {
        if (waveCooldown > 0) {
            waveCooldown--;
            return;
        }

        waveCooldown = WAVE_INTERVAL;

        if (this.random.nextFloat() <= WAVE_CHANCE) {
            LivingEntity owner = this.getOwner();
            if (owner != null && this.hasLineOfSight(owner) && owner.hasLineOfSight(this)) {
                triggerAnim("waveController", "wave");
            }
        }
    }


    private static final EntityDataAccessor<Boolean> HAS_ORGANELLE =
            SynchedEntityData.defineId(LimbEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> CARRIED_ITEM =
            SynchedEntityData.defineId(LimbEntity.class, EntityDataSerializers.ITEM_STACK);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HAS_ORGANELLE, false);
        this.entityData.define(CARRIED_ITEM, ItemStack.EMPTY);
    }

    public ItemStack getCarriedItem() {
        ItemStack stack = this.entityData.get(CARRIED_ITEM);
        return stack == null ? ItemStack.EMPTY : stack;
    }

    public void setCarriedItem(ItemStack stack) {
        if (stack == null) stack = ItemStack.EMPTY;
        this.entityData.set(CARRIED_ITEM, stack.copy());
    }

    public boolean hasCarriedItem() {
        return !this.getCarriedItem().isEmpty();
    }

    public boolean hasOrganelle() {
        return this.entityData.get(HAS_ORGANELLE);
    }

    public void setHasOrganelle(boolean value) {
        this.entityData.set(HAS_ORGANELLE, value);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("HasOrganelle", hasOrganelle());
        ItemStack carried = this.getCarriedItem();
        if (!carried.isEmpty()) {
            CompoundTag itemTag = new CompoundTag();
            carried.save(itemTag);
            tag.put("CarriedItem", itemTag);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setHasOrganelle(tag.getBoolean("HasOrganelle"));
        if (tag.contains("CarriedItem")) {
            CompoundTag itemTag = tag.getCompound("CarriedItem");
            this.setCarriedItem(ItemStack.of(itemTag));
        } else {
            this.setCarriedItem(ItemStack.EMPTY);
        }
    }

    @Override
    public ResourceLocation getTexture() {
        if (this.entityData.get(HAS_ORGANELLE)) {
            return new ResourceLocation("blood_moon_rising","textures/entity/limb_with_organelle.png");
        }
        return super.getTexture();
    }

    //Gecko
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
        controllers.add(new AnimationController<>(this, "waveController", 0, state -> PlayState.STOP)
                .triggerableAnim("wave", RawAnimation.begin().then("WAVE", Animation.LoopType.PLAY_ONCE)));
    }

    private PlayState predicate(AnimationState<LimbEntity> state) {
        LimbEntity entity = state.getAnimatable();

        boolean actuallyMoving = state.getLimbSwingAmount() > 0.01f || !entity.getNavigation().isDone();

        if (actuallyMoving) {
            if (this.isAggressive()) {
                state.getController().setAnimation(RawAnimation.begin().then("RUN", Animation.LoopType.LOOP));
            } else {
                state.getController().setAnimation(RawAnimation.begin().then("WALK", Animation.LoopType.LOOP));
            }
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
