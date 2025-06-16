package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.armor.custom.VisceralArmorItem;
import net.trashelemental.blood_moon_rising.capabilities.ModCapabilities;
import net.trashelemental.blood_moon_rising.item.ModItems;

import java.util.UUID;

/**
 * Acts as an amplifier for certain mod items and gear. Prevents 'fail' outcomes of rituals.
 */
public class AstralHeartEffect extends AbstractHeartEffect {

    private static final UUID UUID = java.util.UUID.fromString("9a4c2b1c-bf84-4f20-bf18-e063b14a23ed");

    public AstralHeartEffect() {
        super(UUID, "Astral", -2.0);
    }

    private static final UUID ASTRAL_HEALTH_BONUS = UUID.fromString("fcaf3014-3186-4a91-8790-b4a85bdcdf64");
    private static final AttributeModifier ASTRAL_HEALTH_MOD = new AttributeModifier(ASTRAL_HEALTH_BONUS, "astral-visceral-health", 4.0, AttributeModifier.Operation.ADDITION);

    @Override
    public void onTick(Player player) {
        boolean isWearingSet = VisceralArmorItem.isWearingFullSet(player);
        AttributeInstance healthAttr = player.getAttribute(Attributes.MAX_HEALTH);

        if (healthAttr == null) return;

        boolean hasModifier = healthAttr.getModifier(ASTRAL_HEALTH_BONUS) != null;

        if (isWearingSet) {
            if (!hasModifier) {
                healthAttr.addPermanentModifier(ASTRAL_HEALTH_MOD);
            }
        } else if (hasModifier) {
            healthAttr.removeModifier(ASTRAL_HEALTH_BONUS);
            if (player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        }

        super.onTick(player);
    }

    public static boolean hasAstralHeart(Player player) {
        return player.getCapability(ModCapabilities.HEART_DATA)
                .map(data -> data.hasHeart(ModItems.ASTRAL_HEART.get()))
                .orElse(false);
    }
}