package net.trashelemental.blood_moon_rising.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

public class ModTags {

    public static class Blocks {




        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(BloodMoonRising.MOD_ID, name));
        }
    }


    public static class Items {


        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(BloodMoonRising.MOD_ID, name));
        }
    }

    public static class EntityTags {

        public static final TagKey<EntityType<?>> HEMORRHAGE_IMMUNE = tag("hemorrhage_immune");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BloodMoonRising.MOD_ID, name));
        }

    }

}
