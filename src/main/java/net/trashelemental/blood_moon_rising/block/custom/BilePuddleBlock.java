package net.trashelemental.blood_moon_rising.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.BileProjectileEntity;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;
import org.joml.Vector3f;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("deprecation")
public class BilePuddleBlock extends Block {

    public BilePuddleBlock(Properties pProperties) {
        super(Properties.of()
                .sound(SoundType.SLIME_BLOCK)
                .strength(0.5f, 1f)
                .noCollission()
                .noOcclusion()
                .forceSolidOn()
                .pushReaction(PushReaction.DESTROY));
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.1;
        double z = pos.getZ() + 0.5;

        ParticlesUpwardServerSide(level, dustParticle(), x, y, z, 6, 0.9, 0.5);

        level.scheduleTick(pos, this, 10);
    }

    //Damages non-immune entities for 1 point of damage twice every second, with an 80% chance to inflict corrosion for
    //ten seconds as well.
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

        float corrosionChance = 0.8f;

        if (level.getGameTime() % 10 != 0) return;
        if (level.isClientSide) return;
        if (!(entity instanceof LivingEntity target)) return;

        if (BileProjectileEntity.shouldHurt(target)) {
            UtilMethods.damageEntity(target, DamageTypes.GENERIC, 1);

            if (level.random.nextFloat() < corrosionChance && (!target.hasEffect(ModMobEffects.CORROSION))) {
                target.addEffect(new MobEffectInstance(ModMobEffects.CORROSION, 200));
            }
        }
    }

    //Is destroyed ten seconds after being placed.
    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, level, pos, pOldState, pMovedByPiston);

        BloodMoonRising.queueServerWork(300, () -> {
            BlockState currentState = level.getBlockState(pos);
            if (currentState.getBlock() == this) {
                level.destroyBlock(pos, false);
            }
        });

        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 10);
        }
    }

    //Valid spawn conditions
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState belowState = world.getBlockState(pos.below());
        return belowState.getShape(world, pos.below()).equals(Shapes.block());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        return !state.canSurvive(world, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public static void ParticlesUpwardServerSide(Level level, ParticleOptions particleType, double x, double y, double z, int count, double horizontalSpread, double verticalSpeed) {
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < count; i++) {
                double vx = (ThreadLocalRandom.current().nextDouble() - 0.5) * horizontalSpread;
                double vy = ThreadLocalRandom.current().nextDouble() * verticalSpeed;
                double vz = (ThreadLocalRandom.current().nextDouble() - 0.5) * horizontalSpread;
                serverLevel.sendParticles(particleType, x, y, z, 1, vx, vy, vz, 0.0D);
            }
        }
    }

    protected ParticleOptions dustParticle() {
        return new DustParticleOptions(new Vector3f(0.702f, 0.823f, 0.471f), 1.1f);
    }

}
