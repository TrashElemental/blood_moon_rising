package net.trashelemental.blood_moon_rising.blood_moon;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.trashelemental.blood_moon_rising.Config;

@EventBusSubscriber
public class BloodMoonClientEvents {

    private static boolean isBloodMoonActive = false;
    private static final int DAWN = 0;
    private static final int NIGHTFALL = 13000;

    public static void setBloodMoonActive(boolean active) {
        if (active) {
            BloodMoonClientEvents.doBloodMoonStartingVFX();
        } else {
            BloodMoonClientEvents.doBloodMoonEndingVFX();
        }
        isBloodMoonActive = active;
    }

    public static boolean isBloodMoonActive() {
        return isBloodMoonActive;
    }

    /**
     * Sends a message and plays a sound if the time is around when it would start. Meant to avoid message spam if
     * a player leaves and rejoins during the night or switches dimensions.
     */
    public static void doBloodMoonStartingVFX() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        long time = mc.level.getDayTime() % 24000L;
        if (time < NIGHTFALL - 100 || time > NIGHTFALL + 100) return;

        System.out.println("Blood Moon has started. Activating VFX.");

        if (Config.DO_BLOOD_MOON_ACTIVATION_MESSAGE.get()) {
            mc.player.sendSystemMessage(Component.translatable("message.blood_moon_rising.blood_moon_start")
                    .withStyle(ChatFormatting.RED));
        }

        if (Config.DO_BLOOD_MOON_ACTIVATION_NOISE.get()) {
            mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.AMBIENT_CAVE, 1.0F));
        }
    }

    public static void doBloodMoonEndingVFX() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        long time = mc.level.getDayTime() % 24000L;
        if (time < DAWN - 100 || time > DAWN + 100) return;

        System.out.println("Blood Moon has ended. Activating VFX.");

        if (Config.DO_BLOOD_MOON_ACTIVATION_MESSAGE.get()) {
            mc.player.sendSystemMessage(Component.translatable("message.blood_moon_rising.blood_moon_end")
                    .withStyle(ChatFormatting.RED));
        }

        if (Config.DO_BLOOD_MOON_ACTIVATION_NOISE.get()) {
            mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.AMBIENT_CAVE, 1.0F));
        }
    }

    /**
     * Manages changing the fog color to an eerie red.
     */
    @SubscribeEvent
    public static void changeFogColor(ViewportEvent.ComputeFogColor event) {
        if (!isBloodMoonActive()) return;
        if (!(Config.DO_BLOOD_MOON_VISUAL_EFFECTS.get())) return;

        event.setRed(0.3f);
        event.setGreen(0.0f);
        event.setBlue(0.0f);
    }

}

// I didn't like how this looked, might come back to it eventually.
//    @SubscribeEvent
//    public static void changeFogDensity(ViewportEvent.RenderFog event) {
//        if (!isBloodMoonActive()) return;
//
//        event.setCanceled(true);
//        event.setNearPlaneDistance(0.5f);
//        event.setFarPlaneDistance(64.0f);
//    }