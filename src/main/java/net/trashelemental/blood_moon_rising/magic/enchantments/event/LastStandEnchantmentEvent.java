package net.trashelemental.blood_moon_rising.magic.enchantments.event;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.blood_moon_rising.magic.enchantments.ModEnchantments;

//When the wearer is at 8 health or less, reduces incoming damage.
//At 8 health or below, damage is reduced by 2 per level.
//Cannot reduce damage below 1, and the damage must come from an attack.
@Mod.EventBusSubscriber
public class LastStandEnchantmentEvent {

    @SubscribeEvent
    public static void onLivingAttack(LivingDamageEvent event) {

        if (event.getSource().getEntity() instanceof LivingEntity) {

            LivingEntity target = event.getEntity();
            if (!(target instanceof Player player)) return;

            ItemStack chestArmor = player.getItemBySlot(EquipmentSlot.CHEST);
            if (chestArmor.isEmpty() || (!(chestArmor.isEnchanted()))) return;

            int enchantmentLevel = chestArmor.getEnchantmentLevel(ModEnchantments.LAST_STAND.get());
            float healthThreshold = 8.0f;

            if (target.getHealth() <= healthThreshold && enchantmentLevel > 0) {
                float originalDamage = event.getAmount();
                float damageReduction = 2 * enchantmentLevel;
                float newDamage = Math.max(originalDamage - damageReduction, 1.0f);

                event.setAmount(newDamage);
            }

        }
    }
}