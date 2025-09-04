package net.trashelemental.blood_moon_rising.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.*;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.LeechEntity;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.MosquitoEntity;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.BileProjectileEntity;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.BloodProjectileEntity;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.SacredSpearProjectileEntity;


public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, BloodMoonRising.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<MorselEntity>> MORSEL =
            ENTITY_TYPES.register("morsel", () -> EntityType.Builder.of(MorselEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 2f).build("morsel"));

    public static final DeferredHolder<EntityType<?>, EntityType<LeechEntity>> LEECH =
            ENTITY_TYPES.register("leech", () -> EntityType.Builder.of(LeechEntity::new, MobCategory.CREATURE)
                    .sized(0.4f, 0.3f).build("leech"));
    public static final DeferredHolder<EntityType<?>, EntityType<MosquitoEntity>> MOSQUITO =
            ENTITY_TYPES.register("mosquito", () -> EntityType.Builder.of(MosquitoEntity::new, MobCategory.CREATURE)
                    .sized(0.4f, 0.3f).build("mosquito"));

    public static final DeferredHolder<EntityType<?>, EntityType<ClotEntity>> CLOT =
            ENTITY_TYPES.register("clot", () -> EntityType.Builder.of(ClotEntity::new, MobCategory.MONSTER)
                    .sized(1f, 1f).build("clot"));
    public static final DeferredHolder<EntityType<?>, EntityType<ArteryEntity>> ARTERY =
            ENTITY_TYPES.register("artery", () -> EntityType.Builder.of(ArteryEntity::new, MobCategory.MONSTER)
                    .sized(1f, 2.5f).build("artery"));
    public static final DeferredHolder<EntityType<?>, EntityType<BoilEntity>> BOIL =
            ENTITY_TYPES.register("boil", () -> EntityType.Builder.of(BoilEntity::new, MobCategory.MONSTER)
                    .sized(1f, 1f).build("boil"));

    public static final DeferredHolder<EntityType<?>, EntityType<OrganelleEntity>> ORGANELLE =
            ENTITY_TYPES.register("organelle", () -> EntityType.Builder.of(OrganelleEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 0.5f).build("organelle"));
    public static final DeferredHolder<EntityType<?>, EntityType<LesionEntity>> LESION =
            ENTITY_TYPES.register("lesion", () -> EntityType.Builder.of(LesionEntity::new, MobCategory.MONSTER)
                    .sized(1f, 2.5f).build("lesion"));
    public static final DeferredHolder<EntityType<?>, EntityType<MolarEntity>> MOLAR =
            ENTITY_TYPES.register("molar", () -> EntityType.Builder.of(MolarEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 0.5f).build("molar"));
    public static final DeferredHolder<EntityType<?>, EntityType<MouthEntity>> MOUTH =
            ENTITY_TYPES.register("mouth", () -> EntityType.Builder.of(MouthEntity::new, MobCategory.MONSTER)
                    .sized(1.5f, 1f).build("mouth"));

    public static final DeferredHolder<EntityType<?>, EntityType<LimbEntity>> LIMB =
            ENTITY_TYPES.register("limb", () -> EntityType.Builder.of(LimbEntity::new, MobCategory.CREATURE)
                    .sized(1f, 1.2f).build("limb"));



    //Projectiles
    public static final DeferredHolder<EntityType<?>, EntityType<SacredSpearProjectileEntity>> SACRED_SPEAR_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("sacred_spear_projectile_entity",
                    () -> EntityType.Builder.<SacredSpearProjectileEntity>of(SacredSpearProjectileEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.3f).build("sacred_spear_projectile_entity"));
    public static final DeferredHolder<EntityType<?>, EntityType<BileProjectileEntity>> BILE_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("bile_projectile_entity",
                    () -> EntityType.Builder.<BileProjectileEntity>of(BileProjectileEntity::new, MobCategory.MISC)
                            .sized(0.3f, 0.3f).build("bile_projectile_entity"));
    public static final DeferredHolder<EntityType<?>, EntityType<BloodProjectileEntity>> BLOOD_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("blood_projectile_entity",
                    () -> EntityType.Builder.<BloodProjectileEntity>of(BloodProjectileEntity::new, MobCategory.MISC)
                            .sized(0.3f, 0.3f).build("blood_projectile_entity"));



    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
