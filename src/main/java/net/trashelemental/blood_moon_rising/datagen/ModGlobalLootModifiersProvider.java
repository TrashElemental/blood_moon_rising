package net.trashelemental.blood_moon_rising.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, BloodMoonRising.MOD_ID);
    }


    @Override
    protected void start() {


    }
}
