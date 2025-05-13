package com.siesque.dwrt;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class DWRTeams implements ModInitializer {
    private static final String MOD_ID = "dwrt";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private TeamsManager teamsManager;

    @Override
    public void onInitialize() {
        try {
            teamsManager = new TeamsManager(LOGGER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAccess,
                environment
        ) -> {
            dispatcher.register(
                    CommandManager.literal("dwrt")
                            .then(CommandManager.literal("create")
                                    .executes(teamsManager::createTeam))
                            .then(CommandManager.literal("list")
                                    .executes(teamsManager::listTeams))
                            .then(CommandManager.literal("leave")
                                    .then(CommandManager.argument("teamName", StringArgumentType.string())
                                            .executes(teamsManager::leaveTeam)))
            );
        });
    }
}