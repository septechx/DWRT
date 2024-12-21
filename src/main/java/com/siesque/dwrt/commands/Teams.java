package com.siesque.dwrt.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.siesque.dwrt.Team;
import com.siesque.dwrt.database.InviteStorage;
import com.siesque.dwrt.database.TeamStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.io.IOException;

public class Teams {
    private final Logger LOGGER;

    public Teams(Logger logger) {
        this.LOGGER = logger;
    }

    public int createTeam(CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        ServerCommandSource source = context.getSource();

        if (source.getEntity() == null) {
            source.sendFeedback(() -> Text.literal("This command must be run by and entity!"), false);
            return 0;
        }

        String player = source.getEntity().getName().getString();

        TeamStorage.createTeam(name, player);

        source.sendFeedback(() -> Text.literal("Created new team named " + name), false);

        LOGGER.info("Created new team: {}", name);

        return 1;
    }

    public int invite(CommandContext<ServerCommandSource> context) {
        String player = StringArgumentType.getString(context, "player");
        ServerCommandSource source = context.getSource();

        if (source.getEntity() == null) {
            source.sendFeedback(() -> Text.literal("This command must be run by and entity!"), false);
            return 0;
        }

        String inviter = source.getEntity().getName().getString();
        Team team = TeamStorage.getTeamFromMember(inviter);

        try {
            InviteStorage.write(player, team.name);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write invite to file", e);
        }

        source.sendFeedback(() -> Text.literal(String.format("Invited player %s to team %s", player, team.name)), false);

        return 1;
    }

    public int join(CommandContext<ServerCommandSource> context) {
        String team = StringArgumentType.getString(context, "name");
        ServerCommandSource source = context.getSource();

        if (source.getEntity() == null) {
            source.sendFeedback(() -> Text.literal("This command must be run by and entity!"), false);
            return 0;
        }

        String player = source.getEntity().getName().getString();

        try {
            if(InviteStorage.canJoin(player, team)) {
                TeamStorage.getTeam(team).addMember(player);
                LOGGER.info("Added player {} to team {}", player, team);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to join team", e);
        }

        source.sendFeedback(() -> Text.literal("You have joined team " + team), false);

        return 1;
    }
}
