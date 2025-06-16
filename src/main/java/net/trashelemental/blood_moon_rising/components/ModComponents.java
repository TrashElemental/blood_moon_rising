package net.trashelemental.blood_moon_rising.components;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

import java.util.function.UnaryOperator;

public class ModComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(BloodMoonRising.MOD_ID);



    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> POINTS = register(
            "tool_points", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));



    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> IS_CHARGING_NIHIL = register(
            "is_charging_nihil", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));



    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register
            (String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}