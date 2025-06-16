package net.trashelemental.blood_moon_rising.junkyard_lib.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * A basic tamed mob that can be tamed if wild, and can be interacted with to cycle its
 * behavior between follow, stay, and wander, sending an action bar message.
 *
 */

@SuppressWarnings("Deprecated")
public class TamableEntity extends TamableAnimal {

    /**
     * The item that can be used to tame the mob if it is not tamed.
     */
    private final Item tameItem;
    /**
     * The food/breed item for the mob. Pass in 'null' if you don't want it to
     * be able to breed, and override the breed offspring to be null as well just in case.
     */
    private final Item breedItem;

    public TamableEntity(EntityType<? extends TamableAnimal> entityType, Level level, Item tameItem, Item breedItem) {
        super(entityType, level);
        this.tameItem = tameItem;
        this.breedItem = breedItem;
    }

    public static boolean follow(TamableEntity entity) {
        if (entity == null)
            return false;
        return entity.isFollowing();
    }

    public static boolean wander(TamableEntity entity) {
        if (entity == null)
            return false;
        return entity.isWandering();
    }

    public static boolean stay(TamableEntity entity) {
        return !follow(entity) && !wander(entity);
    }

    public static boolean isTame(TamableEntity entity) {
        return entity.isTame();
    }


    /**
     * Item that can be used to breed it.
     */
    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(breedItem);
    }

    /**
     * Make this null and remove breed goals if you don't want your entity to breed.
     */
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return (AgeableMob) this.getType().create(serverLevel);
    }


    /**
     * Mob interact behavior.
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!this.isTame() && itemstack.is(tameItem)) {
            this.usePlayerItem(player, hand, itemstack);
            if (!this.level().isClientSide) {
                if (this.random.nextInt(5) == 0) {
                    this.tame(player);
                    this.BEHAVIOR = "FOLLOW";
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
                this.setPersistenceRequired();
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        if (this.isTame() && this.isOwnedBy(player) && !(itemstack.is(breedItem))) {
            cycleBehavior(player);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return super.mobInteract(player, hand);
    }


    /**
     * Behavior NBT and cycling method.
     */
    public String BEHAVIOR = "WANDER";

    private void setBehaviorInPersistentData(String behavior) {
        CompoundTag tag = this.getPersistentData();
        tag.putString("Behavior", behavior);
    }

    public boolean isFollowing() {
        return this.BEHAVIOR.equals("FOLLOW");
    }

    public boolean isWandering() {
        return this.BEHAVIOR.equals("WANDER");
    }

    private void cycleBehavior(Player pPlayer) {
        switch (this.BEHAVIOR) {
            case "FOLLOW":
                this.BEHAVIOR = "WANDER";
                pPlayer.displayClientMessage(Component.literal(getEntityName() + " will wander"), true);
                break;
            case "STAY":
                this.BEHAVIOR = "FOLLOW";
                pPlayer.displayClientMessage(Component.literal(getEntityName() + " will follow"), true);
                break;
            case "WANDER":
                this.BEHAVIOR = "STAY";
                pPlayer.displayClientMessage(Component.literal(getEntityName() + " will stay"), true);
                break;
        }
        this.setBehaviorInPersistentData(this.BEHAVIOR);
    }

    /**
     * Gets the entity's custom name if it has one, or defaults to its lang entry.
     * Useful for unique skins for custom names.
     */
    public String getEntityName() {
        if (this.hasCustomName()) {
            return this.getCustomName().getString();
        }
        return this.getType().getDescription().getString();
    }

    /**
     * NBT data.
     */
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Behavior", this.BEHAVIOR);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Behavior")) {
            this.BEHAVIOR = compound.getString("Behavior");
        }
    }


}
