package net.trashelemental.blood_moon_rising.item.custom.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects.AstralHeartEffect;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.SacredSpearProjectileEntity;
import net.trashelemental.blood_moon_rising.magic.effects.event.HemorrhageLogic;
import net.trashelemental.blood_moon_rising.util.event.NihilAttack;
import net.trashelemental.blood_moon_rising.util.item.PointsToolInteractions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("Deprecated")
public class SacredSpearItem extends Item {
    private static final int NIHIL_CHARGE_TIME = 32;
    private static final int PROJECTILE_CHARGE_TIME = 10;
    private static final float PROJECTILE_SPEED = 2.5F;
    private static final int maxPoints = 10;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public SacredSpearItem(Item.Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if ((double)pState.getDestroySpeed(pLevel, pPos) != 0.0D) {
            pStack.hurtAndBreak(2, pEntityLiving, (p_43385_) -> {
                p_43385_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (Config.DISPLAY_POINTS.get()) {
            int currentPoints = getCurrentPoints(stack);
            tooltipComponents.add(Component.literal("Points: " + currentPoints + " / " + maxPoints));
        }

        if (Config.DISPLAY_TOOLTIPS.get()) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.sacred_spear").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.blood_moon_rising.hold_shift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }

    private boolean isChargingNihil(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("IsChargingNihil");
    }

    private void setChargingNihil(ItemStack stack, boolean charging) {
        stack.getOrCreateTag().putBoolean("IsChargingNihil", charging);
    }

    private int getCurrentPoints(ItemStack stack) {
        return stack.getOrCreateTag().getInt("Points");
    }

    private void setCurrentPoints(ItemStack stack, int points) {
        stack.getOrCreateTag().putInt("Points", points);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int currentPoints = getCurrentPoints(stack);

        if (currentPoints > 0) {
            setCurrentPoints(stack, currentPoints - 1);

            if (attacker.level().random.nextInt(2) == 0) {
                HemorrhageLogic.applyHemorrhage(target, attacker, 160);
            }
        } else {
            stack.hurtAndBreak(1, attacker, (entity) -> {entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);});
        }

        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        int currentPoints = getCurrentPoints(item);

        if (item.getDamageValue() >= item.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(item);
        }

        if (!level.isClientSide) {
            PointsToolInteractions checkForPointsAdd = new PointsToolInteractions();

            if (checkForPointsAdd.canAddPoints(player) && currentPoints < maxPoints) {
                checkForPointsAdd.addPointsFromIchorOrChrismBottle(player, item, maxPoints);
                return InteractionResultHolder.success(item);
            }
        }

        if (!level.isClientSide && player.isCrouching()) {
            if (currentPoints == 0) {
                setChargingNihil(item, true);
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.PLAYERS, 1.0F, 1.0F);

                NihilAttack nihilAttack = new NihilAttack();
                nihilAttack.performNihilOpening(player);
            }
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(item);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int chargeTime) {
        if (level.isClientSide) return;

        if (!(entity instanceof Player player)) {
            return;
        }

        int charge = this.getUseDuration(stack) - chargeTime;

        if (isChargingNihil(stack)) {
            if (charge >= NIHIL_CHARGE_TIME) {
                performNihilAttack(player, level, stack);
            }
            setChargingNihil(stack, false);
        } else if (charge >= PROJECTILE_CHARGE_TIME) {
            performProjectileAttack(player, level, stack);
        }
    }

    private void performNihilAttack(Player player, Level level, ItemStack stack) {
        if (level.isClientSide) return;
        boolean astralHeart = AstralHeartEffect.hasAstralHeart(player);
        int damage = astralHeart ? 113 : 226;

        stack.hurtAndBreak(damage, player, (entity) -> {entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);});

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.POLAR_BEAR_DEATH, SoundSource.PLAYERS, 1.0F, 0.4F);

        NihilAttack nihilAttack = new NihilAttack();
        nihilAttack.performNihilAttack(player);

        if (astralHeart) {
            if (!player.hasEffect(MobEffects.ABSORPTION)) {
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 300, 0, false, false));
            }
            if (!player.hasEffect(MobEffects.DAMAGE_BOOST)) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 0, false, false));
            }
        }

        setCurrentPoints(stack, maxPoints);
    }

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
            stack.hurtAndBreak(2, player, (entity) -> {entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);});
        } else {
            setCurrentPoints(stack, currentPoints - 1);
        }
    }
}
