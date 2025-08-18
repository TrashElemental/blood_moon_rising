package net.trashelemental.blood_moon_rising.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.ModEntities;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagGenerator extends EntityTypeTagsProvider {
    public ModEntityTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, BloodMoonRising.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(EntityTypeTags.ARTHROPOD)
                .add(ModEntities.MOSQUITO.get())
                .add(ModEntities.LEECH.get());

        this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE)
                .add(ModEntities.MOSQUITO.get());

        this.tag(EntityTypeTags.FROG_FOOD)
                .add(ModEntities.MOSQUITO.get())
                .add(ModEntities.LEECH.get())
                .add(ModEntities.CLOT.get());

        this.tag(EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES)
                .add(ModEntities.LEECH.get());

        this.tag(EntityTypeTags.AQUATIC)
                .add(ModEntities.LEECH.get());

        this.tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                .add(ModEntities.LEECH.get());

    }



}
