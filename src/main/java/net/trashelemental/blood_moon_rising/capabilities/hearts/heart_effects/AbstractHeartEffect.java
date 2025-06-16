package net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.capabilities.hearts.IHeartEffect;

public abstract class AbstractHeartEffect implements IHeartEffect {

    private final ResourceLocation healthReductionName;
    private final AttributeModifier healthModifier;

    public AbstractHeartEffect(String nameKey, int healthReduction) {
        this.healthReductionName = BloodMoonRising.prefix(nameKey + "health_modifier");
        this.healthModifier = new AttributeModifier(this.healthReductionName, healthReduction, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void onAdded(Player player) {
        var attribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (attribute != null && !attribute.hasModifier(healthReductionName)) {
            attribute.addPermanentModifier(healthModifier);
        }
    }

    @Override
    public void onRemoved(Player player) {
        var attribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (attribute != null) {
            attribute.removeModifier(healthModifier);
        }
    }

    protected ResourceLocation getHealthReductionName() {
        return healthReductionName;
    }

    protected AttributeModifier getHealthModifier() {
        return healthModifier;
    }
}
