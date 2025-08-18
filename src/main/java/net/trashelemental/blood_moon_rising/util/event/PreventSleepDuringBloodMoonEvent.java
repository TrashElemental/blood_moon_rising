package net.trashelemental.blood_moon_rising.util.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.blood_moon_rising.blood_moon.BloodMoonManager;

@Mod.EventBusSubscriber
public class PreventSleepDuringBloodMoonEvent {

    @SubscribeEvent
    public static void PreventSleepDuringBloodMoon(PlayerSleepInBedEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide) return;

        if (level instanceof ServerLevel serverLevel && BloodMoonManager.isBloodMoon(serverLevel)) {
            event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
            player.sendSystemMessage(Component.translatable("message.blood_moon_rising.blood_moon_sleep_fail")
                    .withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC));
        }
    }
}
