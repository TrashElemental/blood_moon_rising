package net.trashelemental.blood_moon_rising.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.item.ModItems;


public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BloodMoonRising.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleItem(ModItems.FLEAM);
        simpleItem(ModItems.AUGUR);
        handheldItem(ModItems.WARHAM);

        simpleItem(ModItems.HEART);
        simpleItem(ModItems.CHYME);
        simpleItem(ModItems.EXALTED_BONE);
        simpleItem(ModItems.ICHOR_CLOT);
        simpleItem(ModItems.LIGAMENT);
        simpleItem(ModItems.EMBRYO);

        simpleItem(ModItems.COOKED_HEART);
        simpleItem(ModItems.CURED_HEART);
        simpleItem(ModItems.HEART_OF_GOLD);
        simpleItem(ModItems.CONSECRATED_FLESH);
        simpleItem(ModItems.COOKED_FLESH);
        simpleItem(ModItems.CURED_FLESH);
        simpleItem(ModItems.EXALTED_FLESH);
        simpleItem(ModItems.JERKY);
        simpleItem(ModItems.AMNION);

        simpleItem(ModItems.BOLUS);
        simpleItem(ModItems.BOTTLE_OF_CHRISM);
        simpleItem(ModItems.BOTTLE_OF_ICHOR);
        simpleItem(ModItems.PARASITE_EGGS);
        simpleItem(ModItems.MULTIPLYING_MORSEL);

        simpleItem(ModItems.VISCERAL_HELMET);
        simpleItem(ModItems.VISCERAL_CHESTPLATE);
        simpleItem(ModItems.VISCERAL_LEGGINGS);
        simpleItem(ModItems.VISCERAL_BOOTS);

        SpawnEggItem("leech");
        SpawnEggItem("mosquito");

        simpleItem(ModItems.ASTRAL_HEART);
        simpleItem(ModItems.BROKEN_HEART);
        simpleItem(ModItems.DIVIDING_HEART);
        simpleItem(ModItems.ECHOING_HEART);
        simpleItem(ModItems.ELUSIVE_HEART);
        simpleItem(ModItems.FERAL_HEART);
        simpleItem(ModItems.FRANTIC_HEART);
        simpleItem(ModItems.FROZEN_HEART);
        simpleItem(ModItems.HEAVY_HEART);
        simpleItem(ModItems.HUNGRY_HEART);
        simpleItem(ModItems.SCORCHED_HEART);
        simpleItem(ModItems.SELFLESS_HEART);
        simpleItem(ModItems.SPITEFUL_HEART);
        simpleItem(ModItems.TAINTED_HEART);
        simpleItem(ModItems.WRATHFUL_HEART);

    }


    //Helper Methods

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BloodMoonRising.MOD_ID, "item/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(BloodMoonRising.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseblock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", new ResourceLocation(BloodMoonRising.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseblock.get()).getPath()));
    }

    private void SpawnEggItem(String entityName) {
        withExistingParent(entityName + "_spawn_egg", "item/template_spawn_egg");
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(BloodMoonRising.MOD_ID, "item/" + item.getId().getPath()));
    }
}
