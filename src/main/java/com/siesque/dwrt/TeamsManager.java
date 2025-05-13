package com.siesque.dwrt;

import com.google.common.reflect.TypeToken;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.siesque.libdwr.database.JSONStorage;
import com.siesque.libdwr.database.Storage;
import com.siesque.libdwr.database.StorageProvider;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class TeamsManager {
    private static final FloodgateApi FAPI = FloodgateApi.getInstance();
    private static final Storage STORAGE;

    static {
        try {
            StorageProvider provider = new JSONStorage(Path.of("dwrt/teams.json"));
            STORAGE = new Storage(provider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final Logger LOGGER;
    private List<Team> teams;

    public TeamsManager(Logger logger) throws IOException {
        LOGGER = logger;
        teams = STORAGE.loadData(new TypeToken<List<Team>>() {
        }.getType());
        if (teams == null) {
            LOGGER.info("No teams found, initializing with empty list");
            teams = new ArrayList<>();
        } else {
            LOGGER.info("Loaded {} team(s)", teams.size());
        }
    }

    public int createTeam(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        UUID uuid = context.getSource().getPlayerOrThrow().getUuid();
        FloodgatePlayer player = FAPI.getPlayer(uuid);


        CustomForm form = CustomForm
                .builder()
                .title("Create team")
                .input("Name", "Rilis")
                .dropdown("Color", Consts.COLORS)
                .toggle("Public")
                .validResultHandler(response -> {
                    try {
                        String name = response.asInput();
                        int colorIndex = response.asDropdown();
                        boolean isPublic = response.asToggle();

                        LOGGER.info("Creating team: {}", name);
                        teams.add(new Team(name, isPublic, Consts.COLORS[colorIndex], uuid));
                        try {
                            STORAGE.saveData(teams);

                            Team newTeam = teams.getLast();
                            source.sendFeedback(() -> Text.literal(String.format(
                                    "Successfully created team %s%s§r (%s)",
                                    Consts.COLOR_MAP.get(newTeam.color),
                                    newTeam.name,
                                    newTeam.isPublic ? "Public" : "Private"
                            )), false);
                        } catch (IOException e) {
                            LOGGER.error("Failed to save team data", e);
                            source.sendError(Text.literal("Failed to create team: " + e.getMessage()));
                        }
                    } catch (Exception e) {
                        LOGGER.error("Unexpected error during team creation", e);
                        source.sendError(Text.literal("Failed to create team: " + e.getMessage()));
                    }
                })
                .build();

        player.sendForm(form);

        return 1;
    }

    private String formatTeam(Team team) {
        int members = team.members.size();
        return String.format(
                "%s%s§r (%s): %s %s",
                Consts.COLOR_MAP.get(team.color),
                team.name,
                team.isPublic ? "Public" : "Private",
                members,
                members > 1 ? "members" : "member"
        );
    }

    public int listTeams(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        source.sendFeedback(() -> Text.literal(teams
                .stream()
                .map(team -> formatTeam(team) + "\n")
                .reduce((x, y) -> x + y)
                .orElse("No teams found")
        ), false);

        return 1;
    }

    public int leaveTeam(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        UUID uuid = context.getSource().getPlayerOrThrow().getUuid();
        String teamName = StringArgumentType.getString(context, "teamName");

        int i = 0;
        for (Team team : teams) {
            if (team.name.equals(teamName) && team.members.contains(uuid)) {
                team.members.remove(uuid);
                if (team.members.isEmpty()) {
                    teams.remove(i);
                }
            }
            ++i;
        }

        return 1;
    }
}
