package net.trashelemental.blood_moon_rising.compat.BetterCombat;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.item.ModItems;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BetterCombatWeaponPresetProvider implements DataProvider {
    private final PackOutput packOutput;
    private final List<CompletableFuture<?>> futures = new ArrayList<>();
    private CachedOutput cache;
    private Path outputFolder;

    public BetterCombatWeaponPresetProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.cache = cache;
        this.outputFolder = packOutput.getOutputFolder();

        registerWeapon(ModItems.JAWBLADE, "sword");
        registerWeapon(ModItems.BUTCHERS_CLEAVER, "axe");
        registerWeapon(ModItems.WARHAM, "anchor");
        registerWeapon(ModItems.SACRIFICIAL_DAGGER, "dagger");
        registerWeapon(ModItems.SACRED_SPEAR, "trident");
        registerWeapon(ModItems.BLOODSHOT, "rapier");

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]));
    }

    private void registerWeapon(DeferredItem<? extends Item> item, String preset) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", "bettercombat:" + preset);

        Path path = outputFolder.resolve("data/" + BloodMoonRising.MOD_ID + "/weapon_attributes/" + item.getId().getPath() + ".json");

        futures.add(DataProvider.saveStable(cache, json, path));
    }

    @Override
    public String getName() {
        return "Better Combat Weapon Attributes";
    }
}
