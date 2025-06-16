package net.trashelemental.blood_moon_rising.capabilities.heart_data;

import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class HeartEffects {
    private static final Map<Item, IHeartEffect> EFFECTS = new HashMap<>();

    public static void register(Item item, IHeartEffect effect) {
        EFFECTS.put(item, effect);
    }

    public static IHeartEffect getEffect(Item item) {
        return EFFECTS.get(item);
    }
}
