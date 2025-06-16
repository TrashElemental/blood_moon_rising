package net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.armor.custom.VisceralArmorItem;
import net.trashelemental.blood_moon_rising.capabilities.AttachmentsRegistry;
import net.trashelemental.blood_moon_rising.capabilities.hearts.HeartData;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;

/**
 * Acts as an amplifier for certain mod gear and prevents 'fail' outcomes of rituals.
 */
public class AstralHeartEffect extends AbstractHeartEffect {

    public AstralHeartEffect() {
        super("astral", -2);
    }

    private static final ResourceLocation getHealthModName() {
        return BloodMoonRising.prefix("broken_damage_increase");
    }

    private AttributeModifier getHealthMod() {
        return new AttributeModifier(getHealthModName(), 4.0, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void onTick(Player player) {
        boolean isWearingSet = VisceralArmorItem.isWearingFullSet(player);
        AttributeInstance healthAttr = player.getAttribute(Attributes.MAX_HEALTH);

        if (healthAttr == null) return;

        ResourceLocation modName = getHealthModName();
        AttributeModifier mod = getHealthMod();
        boolean hasModifier = healthAttr.hasModifier(modName);

        if (isWearingSet) {
            if (!hasModifier) {
                healthAttr.addPermanentModifier(mod);
            }
        } else if (hasModifier) {
            healthAttr.removeModifier(modName);
            if (player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        }

        super.onTick(player);
    }

    public static boolean hasAstralHeart(Player player) {
        HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);
        return data.hasHeart(ModItems.ASTRAL_HEART.get());
    }

}
