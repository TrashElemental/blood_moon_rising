package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.IHeartEffect;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;
import net.trashelemental.blood_moon_rising.util.event.AdjustHealthEvent;

import java.util.UUID;

public abstract class AbstractHeartEffect implements IHeartEffect {

    private final UUID uuid;
    private final String name;
    private final double amount;

    public AbstractHeartEffect(UUID uuid, String name, double amount) {
        this.uuid = uuid;
        this.name = name;
        this.amount = amount;
    }

    protected AttributeModifier getHealthModifier() {
        return new AttributeModifier(uuid, name + "Health Reduction", amount, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void onAdded(Player player) {
        UtilMethods.applyModifier(player, Attributes.MAX_HEALTH, getHealthModifier());

        if (player instanceof ServerPlayer serverPlayer) {
            BloodMoonRising.queueServerWork(4, () -> AdjustHealthEvent.adjustHealth(serverPlayer));
        }
    }

    @Override
    public void onRemoved(Player player) {
        UtilMethods.removeModifier(player, Attributes.MAX_HEALTH, uuid);

        if (player instanceof ServerPlayer serverPlayer) {
            BloodMoonRising.queueServerWork(6, () -> {
                float max = (float) serverPlayer.getAttribute(Attributes.MAX_HEALTH).getValue();
                if (serverPlayer.getHealth() > max) {
                    serverPlayer.setHealth(max);
                }
                AdjustHealthEvent.adjustHealth(serverPlayer);
            });
        }
    }
}
