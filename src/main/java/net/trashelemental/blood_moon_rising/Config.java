package net.trashelemental.blood_moon_rising;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = BloodMoonRising.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    //Demo related
    public static final ModConfigSpec.BooleanValue DEMO_MESSAGE = BUILDER
            .comment("Controls whether a message providing information about the demo is sent to each player joining a world. Defaults to true.")
            .define("Send the information message:", true);
    public static final ModConfigSpec.BooleanValue SHOW_ALL = BUILDER
            .comment("Controls whether items that currently have no functionality will appear in the Creative Mode tab. Defaults to false.")
            .define("Show unimplemented items:", false);

    //Non demo related
    public static final ModConfigSpec.BooleanValue DO_BLOOD_MOON = BUILDER
            .comment("Controls whether blood moons happen. Defaults to true.")
            .define("Blood moons happen:", true);

    public static final ModConfigSpec.IntValue BLOOD_MOON_INTERVAL = BUILDER
            .comment("Controls the number of days between each blood moon. Defaults to 10.")
            .defineInRange("Number of days between each blood moon:", 10, 0, 100);

    public static final ModConfigSpec.BooleanValue DO_BLOOD_MOON_ACTIVATION_NOISE = BUILDER
            .comment("Controls whether an ominous sound should play when a blood moon starts or ends. Defaults to true.")
            .define("Play a sound when the blood moon starts or ends:", true);
    public static final ModConfigSpec.BooleanValue DO_BLOOD_MOON_ACTIVATION_MESSAGE = BUILDER
            .comment("Controls whether a message should be set notifying players when a blood moon starts or ends. Defaults to false.")
            .define("Send a message when the blood moon starts or ends:", false);
    public static final ModConfigSpec.BooleanValue DO_BLOOD_MOON_VISUAL_EFFECTS = BUILDER
            .comment("Controls whether the world fog, moon, and light should be tinted red during a blood moon. Defaults to true.")
            .define("Tint world fog red during a blood moon:", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {



    }
}
