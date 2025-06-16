package net.trashelemental.blood_moon_rising.armor.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.trashelemental.blood_moon_rising.armor.renderer.VisceralArmorRenderer;
import net.trashelemental.blood_moon_rising.item.ModItems;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class VisceralArmorItem extends ArmorItem implements GeoAnimatable, GeoItem {

    public VisceralArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    private static final Map<EquipmentSlot, UUID> ARMOR_HEALTH_UUIDS = Map.of(
            EquipmentSlot.HEAD, UUID.fromString("b02dcd0a-5b30-4f98-8c68-5f31363c84a0"),
            EquipmentSlot.CHEST, UUID.fromString("f547c7fd-04d2-4b7e-b8b5-8480059ac92b"),
            EquipmentSlot.LEGS, UUID.fromString("3499835e-2221-4f1e-b81e-6742b8e5b486"),
            EquipmentSlot.FEET, UUID.fromString("dbe508f6-37bb-4e90-8055-e78851c7ce98")
    );

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);


        if (slot == this.getEquipmentSlot()) {
            UUID uuid = ARMOR_HEALTH_UUIDS.get(slot);
            AttributeModifier healthBoost = new AttributeModifier(uuid,
                    "bmr-max-health-bonus",
                    2.0D,
                    AttributeModifier.Operation.ADDITION);

            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(Attributes.MAX_HEALTH, healthBoost);
            return builder.build();
        }
        return modifiers;
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);

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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack,
                                                                   EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if(renderer == null){
                    renderer = new VisceralArmorRenderer();
                }
                renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public double getTick(Object object) {
        return 0;
    }
}
