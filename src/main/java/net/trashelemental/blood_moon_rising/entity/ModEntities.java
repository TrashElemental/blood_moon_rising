package net.trashelemental.blood_moon_rising.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.LeechEntity;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.MosquitoEntity;
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




    //Projectiles
    public static final DeferredHolder<EntityType<?>, EntityType<SacredSpearProjectileEntity>> SACRED_SPEAR_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("sacred_spear_projectile_entity",
                    () -> EntityType.Builder.<SacredSpearProjectileEntity>of(SacredSpearProjectileEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.3f).build("sacred_spear_projectile_entity"));



    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
