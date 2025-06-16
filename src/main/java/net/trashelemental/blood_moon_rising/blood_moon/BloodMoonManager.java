package net.trashelemental.blood_moon_rising.blood_moon;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.blood_moon.network.BloodMoonNetworkHelper;


@EventBusSubscriber
public class BloodMoonManager {

    private static final int DAWN = 0;
    private static final int NIGHTFALL = 12500;
    private static long lastProcessedTime = -1;

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

        if (!Config.DO_BLOOD_MOON.get()) return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        if (server == null) return;

        ServerLevel overworld = server.getLevel(Level.OVERWORLD);

        if (overworld == null) return;
        boolean daylightCycleEnabled = overworld.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
        if (!daylightCycleEnabled) return;

        long time = overworld.getDayTime() % 24000L;
        int interval = Config.BLOOD_MOON_INTERVAL.get();

        if (time == NIGHTFALL && lastProcessedTime != time) {
            if (getCurrentCountdown(overworld) > 0) {
                incrementCountdown(overworld, 1);
            }

            if (BloodMoonSavedData.get(overworld).getCountdown() >= interval) {
                startBloodMoon(overworld);
            }

            lastProcessedTime = time;
        }

        if (time >= DAWN && time < NIGHTFALL) {

            if (isBloodMoon(overworld)) {
                endBloodMoon(overworld);
            }
            lastProcessedTime = -1;
        }
    }

    public static int getCurrentCountdown(ServerLevel level) {
        BloodMoonSavedData data = BloodMoonSavedData.get(level);
        return Config.BLOOD_MOON_INTERVAL.get() - data.getCountdown();
    }

    public static void incrementCountdown(ServerLevel level, int amount) {
        BloodMoonSavedData data = BloodMoonSavedData.get(level);
        data.setCountdown(data.getCountdown() + amount);
        System.out.println("Manager: Blood Moon Countdown Modified.");
        System.out.println("Manager: Nights until next Blood Moon: " + getCurrentCountdown(level));
    }

    public static void setCountdown(ServerLevel level, int countdown) {
        BloodMoonSavedData data = BloodMoonSavedData.get(level);
        data.setCountdown(countdown);
    }

    public static boolean isBloodMoon(ServerLevel level) {
        return BloodMoonSavedData.get(level).isActive();
    }

    public static void startBloodMoon(ServerLevel level) {
        BloodMoonSavedData data = BloodMoonSavedData.get(level);
        data.setActive(true);
        BloodMoonNetworkHelper.syncToAll(true);
        System.out.println("Manager: Blood Moon has begun!");
    }

    public static void endBloodMoon(ServerLevel level) {
        BloodMoonSavedData data = BloodMoonSavedData.get(level);
        data.setActive(false);
        setCountdown(level, 0);
        BloodMoonNetworkHelper.syncToAll(false);
        System.out.println("Manager: Blood Moon has ended. Countdown reset to: " + getCurrentCountdown(level));
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ServerLevel level = player.serverLevel();
        if (level.dimension() != Level.OVERWORLD) return;

        boolean isActive = BloodMoonSavedData.get(level).isActive();
        BloodMoonNetworkHelper.syncTo(player, isActive);
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getTo() != Level.OVERWORLD) return;
        ServerLevel overworld = player.server.getLevel(Level.OVERWORLD);
        if (overworld == null) return;

        boolean isActive = BloodMoonSavedData.get(overworld).isActive();
        BloodMoonNetworkHelper.syncTo(player, isActive);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ServerLevel level = player.serverLevel();
        if (!level.dimension().equals(Level.OVERWORLD)) return;

        boolean isActive = BloodMoonSavedData.get(level).isActive();
        BloodMoonNetworkHelper.syncTo(player, isActive);
    }

}
