package com.siesque.dwrt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Team {
    public String name;
    public List<UUID> members;
    public String color;
    public boolean isPublic;

    public Team(String name, boolean isPublic, String color, UUID creator) {
        this.members = new ArrayList<>();
        this.members.add(creator);
        this.name = name;
        this.color = color;
        this.isPublic = isPublic;
    }
}
