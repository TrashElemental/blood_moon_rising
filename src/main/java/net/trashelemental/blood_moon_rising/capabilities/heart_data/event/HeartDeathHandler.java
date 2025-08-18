package net.trashelemental.blood_moon_rising.capabilities.heart_data.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.trashelemental.blood_moon_rising.capabilities.ModCapabilities;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.HeartData;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.HeartUtil;

import java.util.*;

@Mod.EventBusSubscriber
public class HeartDeathHandler {

    private static final Map<UUID, Set<ResourceLocation>> heartsToRefund = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        player.getCapability(ModCapabilities.HEART_DATA).ifPresent(heartData -> {
            Set<ResourceLocation> removedHearts = clearHearts(heartData, player, false);
            heartsToRefund.put(player.getUUID(), removedHearts);
        });
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        Player newPlayer = event.getEntity();
        UUID uuid = newPlayer.getUUID();

        Set<ResourceLocation> refund = heartsToRefund.remove(uuid);
        if (refund != null && !refund.isEmpty()) {
            refundHeartsToInventory(newPlayer, refund);
        }

        event.getOriginal().getCapability(ModCapabilities.HEART_DATA).ifPresent(oldData ->
                newPlayer.getCapability(ModCapabilities.HEART_DATA).ifPresent(newData ->
                        newData.copyFrom(oldData)
                )
        );
    }

    public static Set<ResourceLocation> clearHearts(HeartData heartData, Player player, boolean refund) {
        Set<ResourceLocation> heartIds = new HashSet<>(heartData.getHearts());

        for (ResourceLocation heartId : heartIds) {
            heartData.removeHeart(heartId, player);
        }

        HeartUtil.reapplyHeartEffects(player, heartData);

        if (refund) {
            refundHeartsToInventory(player, heartIds);
        }

        return heartIds;
    }

    private static void refundHeartsToInventory(Player player, Set<ResourceLocation> heartIds) {
        for (ResourceLocation heartId : heartIds) {
            Item heartItem = getHeartItemFromResourceLocation(heartId);
            if (heartItem != null) {
                ItemStack returnedHeart = new ItemStack(heartItem);
                boolean added = player.getInventory().add(returnedHeart);
                if (!added) {
                    player.drop(returnedHeart, false);
                }
            }
        }
    }

    private static Item getHeartItemFromResourceLocation(ResourceLocation heartId) {
        return ForgeRegistries.ITEMS.getValue(heartId);
    }
}