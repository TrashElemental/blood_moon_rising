package net.trashelemental.blood_moon_rising.capabilities.hearts.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.trashelemental.blood_moon_rising.capabilities.AttachmentsRegistry;
import net.trashelemental.blood_moon_rising.capabilities.hearts.HeartData;

/**
 * Handles refunding hearts when the player dies.
 */
@EventBusSubscriber
public class RefundHearts {

    @SubscribeEvent
    public static void refundHeartsOnDeath(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);

        if (level.isClientSide) return;

        for (Item heartItem : data.getActiveHearts()) {
            ItemStack stack = new ItemStack(heartItem);
            boolean added = player.getInventory().add(stack);

            if (!added) {
                player.drop(stack, false);
            }
        }

        data.clearHearts(player);
    }

}
