package net.trashelemental.blood_moon_rising.entity.ai.limb;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.LimbEntity;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class CollectItemsGoal extends Goal {
    private final LimbEntity limb;
    private ItemEntity targetItem;
    private BlockPos targetContainerPos;

    private int ticksTryingToReachItem = 0;
    private static final int MAX_TICKS_TO_REACH = 100;

    public CollectItemsGoal(LimbEntity limb) {
        this.limb = limb;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!limb.canCollect()) return false;
        targetItem = findNearbyItem();
        return targetItem != null;
    }

    @Override
    public boolean canContinueToUse() {
        return (targetItem != null && !targetItem.isRemoved()) || limb.hasCarriedItem();
    }

    @Override
    public void tick() {
        if (targetItem != null && !targetItem.isRemoved()) {
            Vec3 itemPos = targetItem.position().add(0, 0.1, 0);
            limb.getNavigation().moveTo(itemPos.x, itemPos.y, itemPos.z, 1.0);

            ticksTryingToReachItem++;

            double distanceSq = limb.distanceToSqr(targetItem);
            if (distanceSq < 3) {
                limb.setCarriedItem(targetItem.getItem());
                limb.playSound(SoundEvents.ITEM_PICKUP);
                targetItem.discard();
                targetItem = null;
                ticksTryingToReachItem = 0;
            } else if (ticksTryingToReachItem > MAX_TICKS_TO_REACH) {
                targetItem = findNearbyItem();
                ticksTryingToReachItem = 0;
            }
        } else if (limb.hasCarriedItem()) {
            if (targetContainerPos == null) {
                targetContainerPos = findNearbyContainer();
                if (targetContainerPos == null) return;
            }

            Vec3 targetVec = Vec3.atCenterOf(targetContainerPos);
            limb.getNavigation().moveTo(targetVec.x, targetVec.y, targetVec.z, 1.0);

            if (limb.blockPosition().closerThan(targetContainerPos, 1.5)) {
                BlockEntity be = limb.level().getBlockEntity(targetContainerPos);
                if (be instanceof Container container) {
                    if (addItemToContainer(container, limb.getCarriedItem())) {
                        limb.setCarriedItem(ItemStack.EMPTY);
                        targetContainerPos = null;
                        limb.playSound(SoundEvents.CHEST_OPEN);
                    }
                }
            }
        }
    }

    private ItemEntity findNearbyItem() {
        List<ItemEntity> items = limb.level().getEntitiesOfClass(ItemEntity.class,
                limb.getBoundingBox().inflate(8),
                item -> !item.getItem().isEmpty());

        if (items.isEmpty()) return null;

        items.sort(Comparator.comparingDouble(limb::distanceToSqr));
        return items.get(0);
    }

    private BlockPos findNearbyContainer() {
        for (BlockPos pos : BlockPos.betweenClosed(
                limb.blockPosition().offset(-5, -1, -5),
                limb.blockPosition().offset(5, 1, 5))) {

            BlockEntity be = limb.level().getBlockEntity(pos);
            if (be instanceof Container) {
                return pos;
            }
        }
        return null;
    }

    public static boolean addItemToContainer(Container container, ItemStack stack) {
        if (stack.isEmpty()) return false;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack slotStack = container.getItem(i);
            if (!slotStack.isEmpty() && ItemStack.isSameItemSameComponents(slotStack, stack)) {

                int space = slotStack.getMaxStackSize() - slotStack.getCount();
                if (space > 0) {
                    int toAdd = Math.min(space, stack.getCount());
                    slotStack.grow(toAdd);
                    stack.shrink(toAdd);
                    container.setChanged();
                    if (stack.isEmpty()) return true;
                }
            }
        }

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack slotStack = container.getItem(i);
            if (slotStack.isEmpty()) {
                container.setItem(i, stack.copy());
                stack.setCount(0);
                container.setChanged();
                return true;
            }
        }

        return stack.isEmpty();
    }
}