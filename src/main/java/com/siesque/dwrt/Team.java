package com.siesque.dwrt;

import java.util.ArrayList;
import java.util.List;

import com.siesque.dwrt.database.TeamStorage;

public class Team {
    public final String name;
    public List<String> members = new ArrayList<>();

    public Team(String name, String initialMember) {
        this.name = name;
        this.members.add(initialMember);
    }

    public void addMember(String member) {
        members.add(member);
        write();
    }

    public void removeMember(String member) {
        members.remove(member);
        write();
    }

    private void write() {
        List<Team> prevTeams = TeamStorage.loadData();
        List<Team> teams = new ArrayList<>();

        for (Team team : prevTeams) {
            if (team.name.equals(name)) {
                teams.add(this);
            } else {
                teams.add(team);
            }
        }

        TeamStorage.saveData(teams);
    }
}
