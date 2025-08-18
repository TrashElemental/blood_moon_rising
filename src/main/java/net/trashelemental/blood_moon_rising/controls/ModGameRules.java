package net.trashelemental.blood_moon_rising.controls;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModGameRules {

    public static final GameRules.Key<GameRules.BooleanValue> DO_BLOOD_MOON_CYCLE =
            GameRules.register("doBloodMoonCycle", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));



}
