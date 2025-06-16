package net.trashelemental.blood_moon_rising.armor.custom;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.armor.client.renderer.VisceralArmorRenderer;
import net.trashelemental.blood_moon_rising.item.ModItems;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

public class VisceralArmorItem extends ArmorItem implements GeoAnimatable, GeoItem {

    public VisceralArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    private static final Map<ArmorItem.Type, ResourceLocation> ARMOR_HEALTH_IDS = Map.of(
            ArmorItem.Type.HELMET, BloodMoonRising.prefix("max_health_bonus_helmet"),
            ArmorItem.Type.CHESTPLATE, BloodMoonRising.prefix("max_health_bonus_chestplate"),
            ArmorItem.Type.LEGGINGS, BloodMoonRising.prefix("max_health_bonus_leggings"),
            ArmorItem.Type.BOOTS, BloodMoonRising.prefix("max_health_bonus_boots")
    );

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        ResourceLocation id = ARMOR_HEALTH_IDS.get(this.type);

        if (id == null) {
            return super.getDefaultAttributeModifiers();
        }

        AttributeModifier healthModifier = new AttributeModifier(id, 2.0D, AttributeModifier.Operation.ADD_VALUE);
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.MAX_HEALTH,
                        healthModifier,
                        EquipmentSlotGroup.bySlot(this.type.getSlot())
                )
                .build();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide) return;


    }

    public static boolean isWearingFullSet(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.VISCERAL_HELMET.get() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.VISCERAL_CHESTPLATE.get() &&
                player.getItemBySlot(EquipmentSlot.LEGS).getItem() == ModItems.VISCERAL_LEGGINGS.get() &&
                player.getItemBySlot(EquipmentSlot.FEET).getItem() == ModItems.VISCERAL_BOOTS.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if(this.renderer == null)
                    this.renderer = new VisceralArmorRenderer();

                return this.renderer;
            }
        });
    }


}












