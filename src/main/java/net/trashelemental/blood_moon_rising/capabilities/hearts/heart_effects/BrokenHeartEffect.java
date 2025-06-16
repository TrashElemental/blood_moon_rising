package net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.capabilities.hearts.IHeartEffect;

public class BrokenHeartEffect extends AbstractHeartEffect {

    public BrokenHeartEffect() {
        super("broken", -6);
    }

    private static final ResourceLocation getStrengthModName() {
        return BloodMoonRising.prefix("broken_damage_increase");
    }

    private AttributeModifier getStrengthModifier() {
        return new AttributeModifier(getStrengthModName(), 6.0, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void onAdded(Player player) {
        if (!player.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(getStrengthModName())) {
            player.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(getStrengthModifier());
        }
        super.onAdded(player);
    }

    @Override
    public void onRemoved(Player player) {
        player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(getStrengthModifier());
        super.onRemoved(player);
    }

}
