package net.trashelemental.blood_moon_rising.armor.renderer;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.armor.custom.VisceralArmorItem;
import net.trashelemental.blood_moon_rising.armor.models.VisceralArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class VisceralArmorRenderer extends GeoArmorRenderer<VisceralArmorItem> {

    public VisceralArmorRenderer() {
        super(new VisceralArmorModel());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(VisceralArmorItem armorItem) {
        return new ResourceLocation("blood_moon_rising","textures/armor/visceral_armor.png");
    }
}