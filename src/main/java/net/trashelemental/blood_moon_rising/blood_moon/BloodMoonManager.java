package net.trashelemental.blood_moon_rising.blood_moon;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.blood_moon.network.BloodMoonNetworking;
import net.trashelemental.blood_moon_rising.blood_moon.network.BloodMoonStatusPacket;
import net.trashelemental.blood_moon_rising.controls.ModGameRules;

@Mod.EventBusSubscriber
public class BloodMoonManager {

    private static final int DAWN = 0;
    private static final int NIGHTFALL = 12500;


    /**
     * At roughly nightfall, starts a blood moon if needed, or progresses towards starting one.
     */
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {

        if (!Config.DO_BLOOD_MOON.get()) return;

        if (event.phase == TickEvent.Phase.END) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerLevel overworld = server.getLevel(Level.OVERWORLD);

            if (overworld == null) return;
            boolean daylightCycleEnabled = overworld.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
            boolean bloodMoonCycleEnabled = overworld.getGameRules().getBoolean(ModGameRules.DO_BLOOD_MOON_CYCLE);
            if (!daylightCycleEnabled || !bloodMoonCycleEnabled) return;


            long time = overworld.getDayTime() % 24000L;
            int interval = Config.BLOOD_MOON_INTERVAL.get();

            if (time == NIGHTFALL) {
                if (getCurrentCountdown(overworld) > 0) {
                    incrementCountdown(overworld, 1);
                }

                if (BloodMoonSavedData.get(overworld).getCountdown() >= interval) {
                    startBloodMoon(overworld);
                }
            }

            if (time >= DAWN && time < NIGHTFALL && isBloodMoon(overworld)) {
                endBloodMoon(overworld);
            }
        }
    }

    public static int getCurrentCountdown(ServerLevel level) {
        BloodMoonSavedData data = BloodMoonSavedData.get(level);
        return Config.BLOOD_MOON_INTERVAL.get() - data.getCountdown();
    }

    public static void incrementCountdown(ServerLevel level, int amount) {
        BloodMoonSavedData data = BloodMoonSavedData.get(level);
        data.setCountdown(data.getCountdown() + amount);
        //System.out.println("Manager: Blood Moon Countdown Modified.");
        //System.out.println("Manager: Nights until next Blood Moon: " + getCurrentCountdown(level));
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
        for (ServerPlayer player : level.players()) {
            BloodMoonNetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new BloodMoonStatusPacket(true));
        }
        //System.out.println("Manager: Blood Moon has begun!");
    }

    public static void endBloodMoon(ServerLevel level) {
        BloodMoonSavedData data = BloodMoonSavedData.get(level);
        data.setActive(false);
        setCountdown(level, 0);
        for (ServerPlayer player : level.players()) {
            BloodMoonNetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new BloodMoonStatusPacket(false));
        }
        //System.out.println("Manager: Blood Moon has ended. Countdown reset to: " + getCurrentCountdown(level));
    }


    /**
     * Methods for edge cases like joining during a blood moon, switching dimensions, or dying.
     */
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ServerLevel level = player.serverLevel();
        if (level.dimension() != Level.OVERWORLD) return;

        boolean isActive = BloodMoonSavedData.get(level).isActive();
        BloodMoonNetworking.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new BloodMoonStatusPacket(isActive)
        );
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getTo() != Level.OVERWORLD) return;
        ServerLevel overworld = player.server.getLevel(Level.OVERWORLD);
        if (overworld == null) return;
        boolean isActive = BloodMoonSavedData.get(overworld).isActive();

        BloodMoonNetworking.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new BloodMoonStatusPacket(isActive)
        );
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ServerLevel level = player.serverLevel();
        if (!level.dimension().equals(Level.OVERWORLD)) return;
        boolean isActive = BloodMoonSavedData.get(level).isActive();

        BloodMoonNetworking.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new BloodMoonStatusPacket(isActive)
        );
    }

}
