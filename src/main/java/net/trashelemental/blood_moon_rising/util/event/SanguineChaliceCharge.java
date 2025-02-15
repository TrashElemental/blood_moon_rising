package net.trashelemental.blood_moon_rising.util.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.trashelemental.blood_moon_rising.components.ModComponents;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.item.custom.tools.SanguineChaliceItem;

@EventBusSubscriber
public class SanguineChaliceCharge {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        Level level = event.getEntity().level();
        Entity sourceEntity = event.getSource().getEntity();

        if (event == null || event.getEntity() == null || sourceEntity == null) {
            return;
        }

        if (sourceEntity instanceof Player player && !level.isClientSide) {

            for (ItemStack stack : player.getInventory().items) {
                if (stack.is(ModItems.SANGUINE_CHALICE.get())) {

                    int currentPoints = SanguineChaliceItem.getCurrentPoints(stack);

                    if (currentPoints < 12) {
                        stack.set(ModComponents.POINTS, currentPoints + 1);
                    }
                }
            }
        }
    }
}
