package net.trashelemental.blood_moon_rising.util.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.blood_moon_rising.Config;

@Mod.EventBusSubscriber
public class DemoEvents {

    @SubscribeEvent
    public static void sendDemoMessage(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (Config.DEMO_MESSAGE.get()) {
            player.displayClientMessage(Component.translatable("message.blood_moon_rising.demo_welcome"), false);
            player.displayClientMessage(Component.translatable("message.blood_moon_rising.demo_current"), false);
        }
    }


}
