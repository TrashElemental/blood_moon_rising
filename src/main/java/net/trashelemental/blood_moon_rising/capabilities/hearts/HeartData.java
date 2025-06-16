package net.trashelemental.blood_moon_rising.capabilities.hearts;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Set;

public class HeartData implements IHeartEffect, INBTSerializable {

    private final Set<Item> activeHearts = new HashSet<>();

    public void addHeart(Item heartItem, Player player) {
        if (activeHearts.add(heartItem)) {
            IHeartEffect effect = HeartEffects.getEffect(heartItem);
            if (effect != null) {
                effect.onAdded(player);
            }
        }
    }


    public void removeHeart(Player player, Item heartItem) {
        if (activeHearts.remove(heartItem)) {
            IHeartEffect effect = HeartEffects.getEffect(heartItem);
            if (effect != null) {
                effect.onRemoved(player);
            }
        }
    }

    public boolean hasHeart(Item item) {
        return activeHearts.contains(item);
    }


    public Set<Item> getActiveHearts() {
        return Set.copyOf(activeHearts);
    }

    public void clearHearts(Player player) {
        for (Item heartItem : activeHearts) {
            IHeartEffect effect = HeartEffects.getEffect(heartItem);
            if (effect != null) {
                effect.onRemoved(player);
            }
        }
        activeHearts.clear();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        for (Item heartItem : activeHearts) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(heartItem);
            if (id != null) {
                CompoundTag entry = new CompoundTag();
                entry.putString("Heart", id.toString());
                list.add(entry);
            }
        }

        tag.put("Hearts", list);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
        if (!(tag instanceof CompoundTag compoundTag)) {
            return;
        }

        activeHearts.clear();
        ListTag list = compoundTag.getList("Hearts", Tag.TAG_COMPOUND);

        for (Tag t : list) {
            CompoundTag entry = (CompoundTag) t;
            String heartId = entry.getString("Heart");
            ResourceLocation id = ResourceLocation.tryParse(heartId);
            if (id != null) {
                Item item = BuiltInRegistries.ITEM.get(id);
                if (item != null) {
                    activeHearts.add(item);
                }
            }
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


    /**
     * Methods for the event handler
     */
    public void onTick(Player player) {
        for (Item heartItem : activeHearts) {
            IHeartEffect effect = HeartEffects.getEffect(heartItem);
            if (effect != null) {
                effect.onTick(player);
            }
        }
    }

    public void onHurt(Player player, LivingEntity target, DamageSource source, float amount) {
        for (Item heartItem : activeHearts) {
            IHeartEffect effect = HeartEffects.getEffect(heartItem);
            if (effect != null) {
                effect.onHurt(player, target, source, amount);
            }
        }
    }

    public void onDamage(Player player, LivingEntity target, float amount) {
        for (Item heartItem : activeHearts) {
            IHeartEffect effect = HeartEffects.getEffect(heartItem);
            if (effect != null) {
                effect.onDamage(player, target, amount);
            }
        }
    }

    public void onKilledEnemy(Player player, LivingEntity killed) {
        for (Item heartItem : activeHearts) {
            IHeartEffect effect = HeartEffects.getEffect(heartItem);
            if (effect != null) {
                effect.onKilledEnemy(player, killed);
            }
        }
    }

    public void onInteract(Player player, LivingEntity entity) {
        for (Item heartItem : activeHearts) {
            IHeartEffect effect = HeartEffects.getEffect(heartItem);
            if (effect != null) {
                effect.onInteract(player, entity);
            }
        }
    }

    public void onConsumeFood(Player player, ItemStack food, int nutrition, float saturation) {
        for (Item heartItem : activeHearts) {
            IHeartEffect effect = HeartEffects.getEffect(heartItem);
            if (effect != null) {
                effect.onConsumeFood(player, food, nutrition, saturation);
            }
        }
    }
}
