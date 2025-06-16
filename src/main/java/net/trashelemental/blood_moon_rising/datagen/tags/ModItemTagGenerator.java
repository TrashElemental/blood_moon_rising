package net.trashelemental.blood_moon_rising.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, BloodMoonRising.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        addForgeTags();
        addNeoforgeTags();

    }

    private void addForgeTags() {

        tag(ItemTags.create(new ResourceLocation("forge", "swords"))).add(ModItems.JAWBLADE.get());
        tag(ItemTags.create(new ResourceLocation("forge", "tools/swords"))).add(ModItems.JAWBLADE.get());
        tag(ItemTags.create(new ResourceLocation("forge", "swords"))).add(ModItems.SACRIFICIAL_DAGGER.get());
        tag(ItemTags.create(new ResourceLocation("forge", "tools/swords"))).add(ModItems.SACRIFICIAL_DAGGER.get());
        tag(ItemTags.create(new ResourceLocation("forge", "swords"))).add(ModItems.BUTCHERS_CLEAVER.get());
        tag(ItemTags.create(new ResourceLocation("forge", "tools/swords"))).add(ModItems.BUTCHERS_CLEAVER.get());
        tag(ItemTags.create(new ResourceLocation("forge", "swords"))).add(ModItems.BLOODSHOT.get());
        tag(ItemTags.create(new ResourceLocation("forge", "tools/swords"))).add(ModItems.BLOODSHOT.get());

        tag(ItemTags.create(new ResourceLocation("forge", "armors"))).add(ModItems.VISCERAL_HELMET.get());
        tag(ItemTags.create(new ResourceLocation("forge", "armors"))).add(ModItems.VISCERAL_CHESTPLATE.get());
        tag(ItemTags.create(new ResourceLocation("forge", "armors"))).add(ModItems.VISCERAL_LEGGINGS.get());
        tag(ItemTags.create(new ResourceLocation("forge", "armors"))).add(ModItems.VISCERAL_BOOTS.get());

        tag(ItemTags.create(new ResourceLocation("forge", "armors/helmets"))).add(ModItems.VISCERAL_HELMET.get());
        tag(ItemTags.create(new ResourceLocation("forge", "armors/chestplates"))).add(ModItems.VISCERAL_CHESTPLATE.get());
        tag(ItemTags.create(new ResourceLocation("forge", "armors/chest_armors"))).add(ModItems.VISCERAL_CHESTPLATE.get());
        tag(ItemTags.create(new ResourceLocation("forge", "armors/leggings"))).add(ModItems.VISCERAL_LEGGINGS.get());
        tag(ItemTags.create(new ResourceLocation("forge", "armors/boots"))).add(ModItems.VISCERAL_BOOTS.get());

    }

    private void addNeoforgeTags() {

        tag(ItemTags.create(new ResourceLocation("neoforge", "swords"))).add(ModItems.JAWBLADE.get());
        tag(ItemTags.create(new ResourceLocation("neoforge", "tools/swords"))).add(ModItems.JAWBLADE.get());
        tag(ItemTags.create(new ResourceLocation("neoforge", "swords"))).add(ModItems.SACRIFICIAL_DAGGER.get());
        tag(ItemTags.create(new ResourceLocation("neoforge", "tools/swords"))).add(ModItems.SACRIFICIAL_DAGGER.get());
        tag(ItemTags.create(new ResourceLocation("neoforge", "swords"))).add(ModItems.BUTCHERS_CLEAVER.get());
        tag(ItemTags.create(new ResourceLocation("neoforge", "tools/swords"))).add(ModItems.BUTCHERS_CLEAVER.get());
        tag(ItemTags.create(new ResourceLocation("neoforge", "swords"))).add(ModItems.BLOODSHOT.get());
        tag(ItemTags.create(new ResourceLocation("neoforge", "tools/swords"))).add(ModItems.BLOODSHOT.get());

        //Armor sets
        tag(ItemTags.create(new ResourceLocation("neoforge", "armors/helmets"))).add(
                ModItems.VISCERAL_HELMET.get());
        tag(ItemTags.create(new ResourceLocation("neoforge", "armors/chestplates"))).add(
                ModItems.VISCERAL_CHESTPLATE.get());
        tag(ItemTags.create(new ResourceLocation("neoforge", "armors/chest_armors"))).add(
                ModItems.VISCERAL_CHESTPLATE.get());
        tag(ItemTags.create(new ResourceLocation("neoforge", "armors/leggings"))).add(
                ModItems.VISCERAL_LEGGINGS.get());
        tag(ItemTags.create(new ResourceLocation("neoforge", "armors/boots"))).add(
                ModItems.VISCERAL_BOOTS.get());

    }
}
