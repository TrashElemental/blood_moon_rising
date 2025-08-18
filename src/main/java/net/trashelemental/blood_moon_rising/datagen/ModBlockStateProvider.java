package net.trashelemental.blood_moon_rising.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BloodMoonRising.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        simpleBlock(ModBlocks.BILE_PUDDLE.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/bile_puddle")));

    }

    private void blockItem(DeferredBlock<Block> blockRegistryObject) {
        ResourceLocation blockId = blockRegistryObject.getId();
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(modLoc("block/" + blockId.getPath())));
    }

    private void BlockWithItem(DeferredBlock<Block> blockRegistryObject) {
        ResourceLocation blockId = blockRegistryObject.getId();
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
