package net.trashelemental.blood_moon_rising.armor.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.armor.custom.VisceralArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class VisceralArmorModel extends GeoModel<VisceralArmorItem> {

	@Override
	public ResourceLocation getModelResource(VisceralArmorItem animatable) {
		return BloodMoonRising.prefix("geo/models/armor/visceral_armor.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(VisceralArmorItem animatable) {
		return BloodMoonRising.prefix("textures/armor/visceral_armor.png");
	}

	@Override
	public ResourceLocation getAnimationResource(VisceralArmorItem animatable) {
		return null;
	}
}