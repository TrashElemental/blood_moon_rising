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
            return BlockTags.create(new ResourceLocation(BloodMoonRising.MOD_ID, name));
        }
    }

    public static class Entities {

        public static final TagKey<EntityType<?>> HEMORRHAGE_IMMUNE = tag("hemorrhage_immune");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(BloodMoonRising.MOD_ID, name));
        }
    }


    public static class Items {

        public static final TagKey<Item> MOLAR_ACCEPTS = tag("molar_accepts");

        public static final TagKey<Item> MOLAR_MEAT_INPUT = tag("molar_input_meat");
        public static final TagKey<Item> MOLAR_MEAT_OUTPUT = tag("molar_output_meat");
        public static final TagKey<Item> MOLAR_BONE_INPUT = tag("molar_input_bone");
        public static final TagKey<Item> MOLAR_BONE_OUTPUT = tag("molar_output_bone");
        public static final TagKey<Item> MOLAR_PLANT_INPUT = tag("molar_input_plant");
        public static final TagKey<Item> MOLAR_PLANT_OUTPUT = tag("molar_output_plant");
        public static final TagKey<Item> MOLAR_SPECIAL_INPUT = tag("molar_input_special");
        public static final TagKey<Item> MOLAR_SPECIAL_OUTPUT = tag("molar_output_special");
        public static final TagKey<Item> MOLAR_SUPER_SPECIAL_INPUT = tag("molar_input_super_special");
        public static final TagKey<Item> MOLAR_SUPER_SPECIAL_OUTPUT = tag("molar_output_super_special");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(BloodMoonRising.MOD_ID, name));
        }
    }

}
