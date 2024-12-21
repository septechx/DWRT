package com.siesque.dwrt;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public final String name;
    public List<String> members = new ArrayList<>();

    public Team(String name, String initialMember) {
        this.name = name;
        this.members.add(initialMember);
    }

    public void addMember(String member) {
        members.add(member);
    }

    public void removeMember(String member) {
        members.remove(member);
    }
}
