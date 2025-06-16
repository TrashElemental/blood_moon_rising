package net.trashelemental.blood_moon_rising.magic.effects.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;

@Mod.EventBusSubscriber
public class CorrosionEvents {

    private static final double bonusDamagePercent = 0.15;
    private static final int armorDamage = 2;

    @SubscribeEvent
    public static void CorrosionActivate(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        float damage = event.getAmount();

        if (entity.level().isClientSide) return;

        if (entity.hasEffect(ModMobEffects.CORROSION.get())) {
            MobEffectInstance corrosion = entity.getEffect(ModMobEffects.CORROSION.get());
            if (corrosion == null) return;

            int amplifier = corrosion.getAmplifier();

            corrosionBonusDamage(event, damage, amplifier);

            if (entity instanceof Player player) {
                damageArmor(player, amplifier);
            }

        }
    }

    public static void corrosionBonusDamage(LivingDamageEvent event, float damage, int amplifier) {
        double bonusDamage = bonusDamagePercent * (amplifier + 1);
        float newDamage = (float) (damage + (damage * bonusDamage));

        event.setAmount(newDamage);
    }

    public static void damageArmor(Player player, int amplifier) {
        int damage = armorDamage * (amplifier + 1);

        tryDamageArmor(player.getItemBySlot(EquipmentSlot.HEAD), player, EquipmentSlot.HEAD, damage);
        tryDamageArmor(player.getItemBySlot(EquipmentSlot.CHEST), player, EquipmentSlot.CHEST, damage);
        tryDamageArmor(player.getItemBySlot(EquipmentSlot.LEGS), player, EquipmentSlot.LEGS, damage);
        tryDamageArmor(player.getItemBySlot(EquipmentSlot.FEET), player, EquipmentSlot.FEET, damage);
    }

    private static void tryDamageArmor(ItemStack armorPiece, Player player, EquipmentSlot slot, int damage) {
        if (!armorPiece.isEmpty()) {
            armorPiece.hurtAndBreak(damage, player, (p) -> p.broadcastBreakEvent(slot));
        }
    }
}
