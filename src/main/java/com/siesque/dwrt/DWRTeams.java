package com.siesque.dwrt;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siesque.dwrt.commands.Teams;

public class DWRTeams implements DedicatedServerModInitializer {
    public static final String MOD_ID = "dwrt";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    Teams teams = new Teams(LOGGER);

    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register((dispatcher,registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("dwrt")
                    .then(CommandManager.literal("create")
                            .then(CommandManager.argument("name", StringArgumentType.string())
                                    .executes(teams::createTeam)))
                    .then(CommandManager.literal("invite")
                            .then(CommandManager.argument("player", StringArgumentType.string())
                                    .executes(teams::invite)))
                    .then(CommandManager.literal("join")
                            .then(CommandManager.argument("name", StringArgumentType.string())
                                    .executes(teams::join))));
        });
    }
}
