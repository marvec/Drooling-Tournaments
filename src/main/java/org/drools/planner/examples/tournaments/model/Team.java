package org.drools.planner.examples.tournaments.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Team {

    private final String name;
    private final Group group;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Team)) {
            return false;
        }
        if (!name.equals(((Team)obj).name)) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Team ");
        builder.append(name);
        return builder.toString();
    }

    private static final Set<Team> teams = new HashSet<Team>();

    public Team(String name, Group group) {
        if (name == null) throw new IllegalArgumentException("Name must not be null!");
        this.name = name;
        this.group = group;
        teams.add(this);
    }

    public static Set<Team> getAll() {
        return Collections.unmodifiableSet(teams);
    }

}
