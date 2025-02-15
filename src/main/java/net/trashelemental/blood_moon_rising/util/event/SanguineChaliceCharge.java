package net.trashelemental.blood_moon_rising.util.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.item.custom.tools.SanguineChaliceItem;

@Mod.EventBusSubscriber(modid = BloodMoonRising.MOD_ID)
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
                        SanguineChaliceItem.setCurrentPoints(stack, currentPoints + 1);
                    }
                }
            }
        }
    }
}
