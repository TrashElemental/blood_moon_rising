package net.trashelemental.blood_moon_rising.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.HeartData;

public class ModCapabilities {
    public static Capability<HeartData> HEART_DATA = CapabilityManager.get(new CapabilityToken<>() {});
}
