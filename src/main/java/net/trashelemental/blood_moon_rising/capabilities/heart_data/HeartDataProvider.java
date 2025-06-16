package net.trashelemental.blood_moon_rising.capabilities.heart_data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.capabilities.ModCapabilities;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class HeartDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final ResourceLocation IDENTIFIER = new ResourceLocation(BloodMoonRising.MOD_ID, "heart_data");

    private final HeartData backend = new HeartData();
    private final LazyOptional<HeartData> optional = LazyOptional.of(() -> backend);

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ModCapabilities.HEART_DATA ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return backend.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        backend.deserializeNBT(nbt);
    }

    public void invalidate() {
        optional.invalidate();
    }
}
