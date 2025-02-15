package net.trashelemental.blood_moon_rising.util.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.trashelemental.blood_moon_rising.item.ModItems;

public class PointsToolInteractions {

    public boolean canAddPoints(Player player) {
        InteractionHand offhand = InteractionHand.OFF_HAND;
        ItemStack offhandStack = player.getItemInHand(offhand);

        return offhandStack.is(ModItems.BOTTLE_OF_ICHOR.get()) || offhandStack.is(ModItems.BOTTLE_OF_CHRISM.get());
    }

    public void addPointsFromIchorOrChrismBottle(Player player, ItemStack stack, int maxPoints) {
        InteractionHand offhand = InteractionHand.OFF_HAND;
        ItemStack offhandStack = player.getItemInHand(offhand);
        int currentPoints = getPointsFromNBT(stack);

        //If the offhand item is an Ichor Bottle, charge the item halfway.
        if (offhandStack.is(ModItems.BOTTLE_OF_ICHOR.get())) {
            int pointsToAdd = maxPoints / 2;

            setPointsToNBT(stack, Math.min(currentPoints + pointsToAdd, maxPoints));

            if (!player.isCreative()) {
                offhandStack.shrink(1);
                ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);

                if (offhandStack.isEmpty()) {
                    player.setItemInHand(offhand, glassBottle);
                } else {
                    if (!player.getInventory().add(glassBottle)) {
                        player.drop(glassBottle, false);
                    }
                }
            }
        }

        //If the offhand item is a Chrism Bottle, charge the item completely.
        if (offhandStack.is(ModItems.BOTTLE_OF_CHRISM.get())) {

            setPointsToNBT(stack, maxPoints);

            if (!player.isCreative()) {
                player.setItemInHand(offhand, new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 1.0F, 0.8F);
    }

    private int getPointsFromNBT(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("Points")) {
            return stack.getTag().getInt("Points");
        }
        return 0;
    }

    private void setPointsToNBT(ItemStack stack, int points) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        stack.getTag().putInt("Points", points);
    }
}
