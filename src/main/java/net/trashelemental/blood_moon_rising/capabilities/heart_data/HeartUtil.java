package net.trashelemental.blood_moon_rising.capabilities.heart_data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.trashelemental.blood_moon_rising.capabilities.ModCapabilities;

public class HeartUtil {
    public static HeartData getHeartData(Player player) {
        return player.getCapability(ModCapabilities.HEART_DATA)
                .orElseThrow(() -> new IllegalStateException("HeartData not present"));
    }

    public static void reapplyHeartEffects(Player player, HeartData data) {
        for (ResourceLocation id : data.getHearts()) {
            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item != null) {
                IHeartEffect effect = HeartEffects.getEffect(item);
                if (effect != null) {
                    effect.onAdded(player);
                }
            }
        }
    }
}
