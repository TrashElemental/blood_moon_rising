package net.trashelemental.blood_moon_rising.capabilities.heart_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HeartData implements INBTSerializable<CompoundTag> {
    private final Set<ResourceLocation> activeHearts = new HashSet<>();

    public void addHeart(Item item, Player player) {
        if (item == null) return;
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        if (id == null) return;

        if (activeHearts.add(id)) {
            IHeartEffect effect = HeartEffects.getEffect(item);
            if (effect != null) {
                effect.onAdded(player);
            }
        }
    }

    public void removeHeart(ResourceLocation heartId, Player player) {
        if (activeHearts.remove(heartId)) {
            Item item = ForgeRegistries.ITEMS.getValue(heartId);
            if (item != null) {
                IHeartEffect effect = HeartEffects.getEffect(item);
                if (effect != null) {
                    effect.onRemoved(player);
                }
            }
        }
    }

    public boolean hasHeart(Item item) {
        if (item == null) return false;
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        return id != null && activeHearts.contains(id);
    }

    public Set<ResourceLocation> getHearts() {
        return Collections.unmodifiableSet(activeHearts);
    }

    public void copyFrom(HeartData other) {
        this.activeHearts.clear();
        this.activeHearts.addAll(other.activeHearts);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag heartList = new ListTag();
        for (ResourceLocation id : activeHearts) {
            heartList.add(StringTag.valueOf(id.toString()));
        }
        tag.put("Hearts", heartList);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        activeHearts.clear();
        ListTag heartList = tag.getList("Hearts", Tag.TAG_STRING);
        for (Tag t : heartList) {
            activeHearts.add(new ResourceLocation(t.getAsString()));
        }
    }

    /**
     * Elusive Heart dodge chance
     */
    private float elusiveDodgeChance = 0.15f;

    public float getElusiveDodgeChance() {
        return elusiveDodgeChance;
    }

    public void increaseElusiveDodgeChance() {
        this.elusiveDodgeChance = Math.min(elusiveDodgeChance + 0.05f, 1.0f);
    }

    public void resetElusiveDodgeChance() {
        this.elusiveDodgeChance = 0.15f;
    }
}
