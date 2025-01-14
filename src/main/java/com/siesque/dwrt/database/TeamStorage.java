package com.siesque.dwrt.database;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import com.siesque.dwrt.Team;

public class TeamStorage {
    private static final Storage storage = new Storage("teams.json");

    public static void saveData(List<Team> teams) {
        storage.saveData(teams);
    }

    public static List<Team> loadData() {
        return storage.loadData(new TypeToken<List<Team>>() {}.getType());
    }

    public static void createTeam(String name, String initialMember) {
        Team team = new Team(name, initialMember);

        List<Team> teams = loadData();
        teams.add(team);
        saveData(teams);
    }

    public static Team getTeamFromMember(String member) {
        List<Team> teams = loadData();

        for (Team team : teams) {
            if (team.members.contains(member)) {
                return team;
            }
        }

        throw new RuntimeException("Player isn't in a team");
    }

    public static Team getTeam(String name) {
        List<Team> teams = loadData();

        for (Team team : teams) {
            if (team.name.equals(name)) {
                return team;
            }
        }

        throw new RuntimeException("Team not found");
    }

    public static List<Team> getTeams() {
        return loadData();
    }
}
