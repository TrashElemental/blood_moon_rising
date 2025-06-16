package net.trashelemental.blood_moon_rising.blood_moon;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class BloodMoonSavedData extends SavedData {

    public static final String ID = "blood_moon_data";

    private int countdown = 0;
    private boolean active = false;

    public BloodMoonSavedData() {}

    public BloodMoonSavedData(CompoundTag tag, HolderLookup.Provider provider) {
        this.countdown = tag.getInt("Countdown");
        this.active = tag.getBoolean("Active");
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("Countdown", countdown);
        tag.putBoolean("Active", active);
        return tag;
    }

    public static final SavedData.Factory<BloodMoonSavedData> FACTORY =
            new SavedData.Factory<>(
                    BloodMoonSavedData::new,
                    BloodMoonSavedData::new
            );

    public static BloodMoonSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, ID);
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
        setDirty();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        setDirty();
    }
}