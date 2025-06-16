package net.trashelemental.blood_moon_rising.item.custom.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects.AstralHeartEffect;
import net.trashelemental.blood_moon_rising.components.ModComponents;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.SacredSpearProjectileEntity;
import net.trashelemental.blood_moon_rising.magic.effects.events.HemorrhageLogic;
import net.trashelemental.blood_moon_rising.util.event.NihilAttack;
import net.trashelemental.blood_moon_rising.util.item.PointsToolInteractions;

import java.util.List;

@SuppressWarnings("Deprecated")
public class SacredSpearItem extends Item {
    private static final int NIHIL_CHARGE_TIME = 32;
    private static final int PROJECTILE_CHARGE_TIME = 10;
    private static final float PROJECTILE_SPEED = 2.5F;
    private static final int maxPoints = 10;

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.sacred_spear").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private boolean isChargingNihil(ItemStack stack) {
        return stack.getOrDefault(ModComponents.IS_CHARGING_NIHIL, false);
    }

    //While the weapon has points, attacks do not consume durability.
    //Mainly this is to make it so that Nihil can't be spammed.
    private int getCurrentPoints(ItemStack stack) {
        if (stack.has(ModComponents.POINTS)) {
            Integer points = stack.get(ModComponents.POINTS);
            return points != null ? points : 0;
        }
        return 0;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    //Melee Weapon Properties
    public SacredSpearItem(Item.Properties properties) {
        super(properties);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 8.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.9, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
    }

    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    //If the Sacred Spear is Empowered, it has a 50% chance to inflict Hemorrhage with a melee attack.
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        int currentPoints = getCurrentPoints(stack);

        if (currentPoints > 0) {

            stack.set(ModComponents.POINTS, currentPoints - 1);

            if (attacker.level().random.nextInt(2) == 0) {
                HemorrhageLogic.applyHemorrhage(target, attacker, 160);
            }
        } else {
            stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        }

        return true;
    }


    //Projectile Weapon Properties
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);
        int currentPoints = getCurrentPoints(itemstack);

        if (!level.isClientSide) {
            PointsToolInteractions pointsToolInteractions = new PointsToolInteractions();
            if (pointsToolInteractions.canAddPoints(player) && !(currentPoints >= maxPoints)) {
                pointsToolInteractions.addPointsFromIchorOrChrismBottle(player, itemstack, currentPoints, maxPoints);
                return InteractionResultHolder.success(itemstack);
            }
        }

        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        }

        if (!level.isClientSide && player.isCrouching()) {
            if (currentPoints == 0) {
                itemstack.set(ModComponents.IS_CHARGING_NIHIL.get(), true);
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.PLAYERS, 1.0F, 1.0F);

                NihilAttack nihilAttack = new NihilAttack();
                nihilAttack.performNihilOpening(player);
            }
        }

        player.startUsingItem(hand);

        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int chargeTime) {
        if (level.isClientSide) return;

        if (!(entity instanceof Player player)) {
            return;
        }

        int charge = this.getUseDuration(stack, entity) - chargeTime;

        if (isChargingNihil(stack)) {
            if (charge >= NIHIL_CHARGE_TIME) {
                performNihilAttack(player, level, stack);
            }
            stack.set(ModComponents.IS_CHARGING_NIHIL.get(), false);
        }

        else if (charge >= PROJECTILE_CHARGE_TIME) {
            performProjectileAttack(player, level, stack);
        }

    }

    //Activates 'Nihil', a powerful AOE that instantly hemorrhages non-tamed entities and heals the caster.
    private void performNihilAttack(Player player, Level level, ItemStack stack) {
        if (level.isClientSide) return;
        boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);
        int damage = astralHeart ? 113 : 226;

        stack.hurtAndBreak(damage, player, EquipmentSlot.MAINHAND);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.POLAR_BEAR_DEATH, SoundSource.PLAYERS, 1.0F, 0.4F);

        NihilAttack nihilAttack = new NihilAttack();
        nihilAttack.performNihilAttack(player);

        stack.set(ModComponents.POINTS, maxPoints);

        if (astralHeart) {
            if (!player.hasEffect(MobEffects.ABSORPTION)) {
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 300, 0, false, false));
            }
            if (!player.hasEffect(MobEffects.DAMAGE_BOOST)) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 0, false, false));
            }
        }

    }

    //Projectiles have a 50% chance to inflict Hemorrhage
    //If the Sacred Spear is Empowered, it has a 75% chance to inflict Hemorrhage
    private void performProjectileAttack(Player player, Level level, ItemStack stack) {
        if (level.isClientSide) return;

        int currentPoints = getCurrentPoints(stack);

        SacredSpearProjectileEntity projectile = new SacredSpearProjectileEntity(player, level);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, PROJECTILE_SPEED, 1.0F);

        float hemorrhageChance = (currentPoints > 0) ? 0.75f : 0.5f;
        projectile.setHemorrhageChance(hemorrhageChance);

        level.addFreshEntity(projectile);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (currentPoints == 0) {
            stack.hurtAndBreak(2, player, EquipmentSlot.MAINHAND);
        } else {
            stack.set(ModComponents.POINTS, currentPoints - 1);
        }

    }


    //I originally wanted it to use the bow animation for Nihil and the regular trident animation for
    //the normal projectile, but it was kind of a hassle. Might come back to it.

//    private boolean shouldUseBowAnimation = false;

//    @Override
//    public UseAnim getUseAnimation(ItemStack stack) {
//        return shouldUseBowAnimation ? UseAnim.BOW : UseAnim.SPEAR;
//    }

}

