package net.trashelemental.blood_moon_rising.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
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

        simpleItem(ModItems.COOKED_HEART);
        simpleItem(ModItems.CURED_HEART);
        simpleItem(ModItems.HEART_OF_GOLD);
        simpleItem(ModItems.CONSECRATED_FLESH);
        simpleItem(ModItems.COOKED_FLESH);
        simpleItem(ModItems.CURED_FLESH);
        simpleItem(ModItems.EXALTED_FLESH);
        simpleItem(ModItems.JERKY);

        simpleItem(ModItems.BOLUS);
        simpleItem(ModItems.BOTTLE_OF_CHRISM);
        simpleItem(ModItems.BOTTLE_OF_ICHOR);
        simpleItem(ModItems.PARASITE_EGGS);
        simpleItem(ModItems.MULTIPLYING_MORSEL);

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


    private ItemModelBuilder simpleItem(DeferredItem<Item> item) {
        return getBuilder(item.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(DeferredBlock<Block> block) {
        ResourceLocation blockId = block.getId();
        withExistingParent(blockId.getPath(), modLoc("block/" + blockId.getPath()));
    }

    public void wallItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
        ResourceLocation blockId = block.getId();
        ResourceLocation baseBlockId = baseBlock.getId();

        withExistingParent(blockId.getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", modLoc("block/" + baseBlockId.getPath()));
    }

    private void SpawnEggItem(String entityName) {
        withExistingParent(entityName + "_spawn_egg", "item/template_spawn_egg");
    }

    private ItemModelBuilder handheldItem(DeferredItem<?> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(BloodMoonRising.MOD_ID, "item/" + item.getId().getPath()));
    }

}
