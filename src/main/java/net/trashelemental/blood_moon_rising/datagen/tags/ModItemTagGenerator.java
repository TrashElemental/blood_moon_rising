package net.trashelemental.blood_moon_rising.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.trashelemental.blood_moon_rising.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(ItemTags.WOLF_FOOD)
                .add(ModItems.EXALTED_FLESH.get())
                .add(ModItems.COOKED_FLESH.get())
                .add(ModItems.CURED_FLESH.get())
                .add(ModItems.COOKED_HEART.get())
                .add(ModItems.CURED_HEART.get())
                .add(ModItems.JERKY.get());

        this.tag(ItemTags.SWORDS)
                .add(ModItems.JAWBLADE.get())
                .add(ModItems.BUTCHERS_CLEAVER.get())
                .add(ModItems.SACRIFICIAL_DAGGER.get())
                .add(ModItems.BLOODSHOT.get());

        this.tag(ItemTags.HEAD_ARMOR)
                .add(ModItems.VISCERAL_HELMET.get());

        this.tag(ItemTags.CHEST_ARMOR)
                .add(ModItems.VISCERAL_CHESTPLATE.get());

        this.tag(ItemTags.LEG_ARMOR)
                .add(ModItems.VISCERAL_LEGGINGS.get());

        this.tag(ItemTags.FOOT_ARMOR)
                .add(ModItems.VISCERAL_BOOTS.get());

    }
}
