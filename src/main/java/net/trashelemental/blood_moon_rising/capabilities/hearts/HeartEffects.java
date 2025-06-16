package net.trashelemental.blood_moon_rising.capabilities.hearts;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HeartEffects {
    private static final Map<ResourceLocation, Entry> BY_ID = new HashMap<>();
    private static final Map<Item, ResourceLocation> BY_ITEM = new HashMap<>();

    public record Entry(Item item, IHeartEffect effect) {}

    public static void register(ResourceLocation id, Item item, IHeartEffect effect) {
        BY_ID.put(id, new Entry(item, effect));
        BY_ITEM.put(item, id);
    }

    public static IHeartEffect get(ResourceLocation id) {
        Entry entry = BY_ID.get(id);
        return entry != null ? entry.effect() : null;
    }

    public static ResourceLocation getId(Item item) {
        return BY_ITEM.get(item);
    }

    public static Item getItem(ResourceLocation id) {
        Entry entry = BY_ID.get(id);
        return entry != null ? entry.item() : null;
    }

    public static Set<ResourceLocation> getAllEffectIds() {
        return BY_ID.keySet();
    }

    public static IHeartEffect getEffect(Item item) {
        ResourceLocation id = BY_ITEM.get(item);
        if (id == null) return null;
        Entry entry = BY_ID.get(id);
        return entry != null ? entry.effect() : null;
    }

}
