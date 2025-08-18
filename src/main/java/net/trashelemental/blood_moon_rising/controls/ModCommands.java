package net.trashelemental.blood_moon_rising.controls;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.blood_moon.BloodMoonManager;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.HeartData;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.HeartUtil;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.event.HeartDeathHandler;

import java.util.Collection;
import java.util.Set;

@Mod.EventBusSubscriber
public class ModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        clearActiveHearts(dispatcher);
        setBloodMoonCountdown(dispatcher);
    }

    public static void clearActiveHearts(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("blood_moon_rising:clear_active_hearts")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("refund", BoolArgumentType.bool())
                                .executes(context -> {
                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
                                    boolean refund = BoolArgumentType.getBool(context, "refund");

                                    int totalRemoved = 0;

                                    for (ServerPlayer target : targets) {
                                        HeartData heartData = HeartUtil.getHeartData(target);
                                        Set<ResourceLocation> removed = HeartDeathHandler.clearHearts(heartData, target, refund);
                                        totalRemoved += removed.size();
                                        context.getSource().sendSuccess(() ->
                                                Component.literal("Cleared " + removed.size() + " hearts from " + target.getName().getString()), false);
                                    }

                                    return totalRemoved;
                                }))));
    }

    public static void setBloodMoonCountdown(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("blood_moon_rising:set_blood_moon_countdown")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("nights", IntegerArgumentType.integer(0))
                        .executes(context -> {
                            int nights = IntegerArgumentType.getInteger(context, "nights");
                            ServerLevel level = context.getSource().getLevel();

                            int interval = Config.BLOOD_MOON_INTERVAL.get();
                            int countdownValue = Math.max(1, interval - nights);

                            BloodMoonManager.setCountdown(level, countdownValue);

                            context.getSource().sendSuccess(() ->
                                    Component.literal("The next Blood Moon will happen in " + nights + " nights."), false);

                            return 1;
                        })));
    }
}
