package net.trashelemental.blood_moon_rising.armor.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.armor.client.models.VisceralArmorModel;
import net.trashelemental.blood_moon_rising.armor.custom.VisceralArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class VisceralArmorRenderer extends GeoArmorRenderer<VisceralArmorItem> {

    public VisceralArmorRenderer() {
        super(new VisceralArmorModel());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(VisceralArmorItem armorItem) {
        return BloodMoonRising.prefix("textures/armor/visceral_armor.png");
    }
}