package net.trashelemental.blood_moon_rising.util.event;

import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.armor.custom.VisceralArmorItem;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;

/**
 * This is for nudging the client to update the health gui when the player
 * gets the health down effect or remove a piece of the Visceral set. This isn't
 * necessary in NF 1.21 because that bug got fixed in later versions evidently.
 */

@Mod.EventBusSubscriber
public class AdjustHealthEvent {

    @SubscribeEvent
    public static void onHealthDownAdded(MobEffectEvent.Added event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (event.getEffectInstance().getEffect().equals(ModMobEffects.HEALTH_DOWN.get())) {
                adjustHealth(player);
            }
        }
    }

    @SubscribeEvent
    public static void onVisceralSetRemoved(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();
        EquipmentSlot slot = event.getSlot();

        if (!(entity instanceof ServerPlayer player)) return;
        if (slot.getType() != EquipmentSlot.Type.ARMOR) return;

        if (isVisceralArmor(from) && !(isVisceralArmor(to))) {
            BloodMoonRising.queueServerWork(4, () -> adjustHealth(player));
        }

    }

    private static boolean isVisceralArmor(ItemStack stack) {
        return stack.getItem() instanceof VisceralArmorItem;
    }

    public static void adjustHealth(ServerPlayer serverPlayer) {
        float maxHealth = (float) serverPlayer.getAttribute(Attributes.MAX_HEALTH).getValue();
        float currentHealth = serverPlayer.getHealth();

        if (currentHealth <= 0.0f) {
            currentHealth = maxHealth;
            serverPlayer.setHealth(currentHealth);
        }

        float clampedHealth = Math.min(currentHealth, maxHealth);
        serverPlayer.connection.send(new ClientboundSetHealthPacket(
                clampedHealth,
                serverPlayer.getFoodData().getFoodLevel(),
                serverPlayer.getFoodData().getSaturationLevel()
        ));
    }
}
