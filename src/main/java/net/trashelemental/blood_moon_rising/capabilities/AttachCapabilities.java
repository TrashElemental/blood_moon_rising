package net.trashelemental.blood_moon_rising.capabilities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.HeartDataProvider;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.HeartUtil;

@Mod.EventBusSubscriber
public class AttachCapabilities {

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(HeartDataProvider.IDENTIFIER, new HeartDataProvider());
        }
    }

    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) {
        event.getOriginal().getCapability(ModCapabilities.HEART_DATA).ifPresent(oldData -> {
            event.getEntity().getCapability(ModCapabilities.HEART_DATA).ifPresent(newData -> {
                newData.copyFrom(oldData);
                HeartUtil.reapplyHeartEffects(event.getEntity(), newData);
            });
        });
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity().getCapability(ModCapabilities.HEART_DATA).ifPresent(data -> {
            HeartUtil.reapplyHeartEffects(event.getEntity(), data);
        });
    }

}
