package net.trashelemental.blood_moon_rising.util.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.trashelemental.blood_moon_rising.blood_moon.BloodMoonManager;

@EventBusSubscriber
public class PreventSleepDuringBloodMoonEvent {

    @SubscribeEvent
    public static void PreventSleepDuringBloodMoon(CanPlayerSleepEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide) return;

        if (level instanceof ServerLevel serverLevel && BloodMoonManager.isBloodMoon(serverLevel)) {
            event.setProblem(Player.BedSleepingProblem.OTHER_PROBLEM);
            player.sendSystemMessage(Component.translatable("message.blood_moon_rising.blood_moon_sleep_fail")
                    .withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC));
        }
    }
}
