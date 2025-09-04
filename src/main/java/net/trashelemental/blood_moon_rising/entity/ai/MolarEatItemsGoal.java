package net.trashelemental.blood_moon_rising.entity.ai;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.MolarEntity;

import java.util.List;

public class MolarEatItemsGoal extends Goal {
    private final PathfinderMob mob;
    private final double speed;
    private final int cooldown;
    private final TagKey<Item> acceptedItems;
    private ItemEntity targetItem;
    private int eatCooldown;

    public MolarEatItemsGoal(PathfinderMob mob, double speed, int cooldownTicks, TagKey<Item> acceptedItems) {
        this.mob = mob;
        this.speed = speed;
        this.cooldown = cooldownTicks;
        this.acceptedItems = acceptedItems;
    }

    @Override
    public boolean canUse() {
        if (eatCooldown > 0) {
            eatCooldown--;
            return false;
        }
        if (mob instanceof MolarEntity molar && (molar.isTrading || !molar.canEat)) return false;

        List<ItemEntity> nearby = mob.level().getEntitiesOfClass(ItemEntity.class,
                mob.getBoundingBox().inflate(8), this::isValidItem);

        if (!nearby.isEmpty()) {
            targetItem = nearby.get(0);
            return true;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetItem == null || !targetItem.isAlive()) {
            return false;
        }

        double reach = mob.getBbWidth() + 1;
        double reachSq = reach * reach;

        return targetItem.distanceToSqr(mob) > reachSq &&
                (isValidItem(targetItem));
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(targetItem, speed);
    }

    @Override
    public void tick() {
        if (targetItem == null) return;

        mob.getLookControl().setLookAt(targetItem, 30.0F, 30.0F);

        double reach = mob.getBbWidth() + 1;
        double reachSq = reach * reach;

        if (mob.distanceToSqr(targetItem) <= reachSq) {
            targetItem.discard();
            mob.playSound(SoundEvents.FOX_EAT, 1.0F, 0.6F);
            eatCooldown = cooldown;
            targetItem = null;
            consumeItem();
        } else {
            mob.getNavigation().moveTo(targetItem, speed);
        }
    }

    public void consumeItem() {

    }

    @Override
    public void stop() {
        targetItem = null;
        mob.getNavigation().stop();
    }

    private boolean isValidItem(ItemEntity entity) {
        return entity.isAlive() &&
                entity.getItem().is(acceptedItems);
    }
}
