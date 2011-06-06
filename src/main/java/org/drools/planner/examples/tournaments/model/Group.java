package org.drools.planner.examples.tournaments.model;

import java.util.HashMap;
import java.util.Map;

public class Group {

    @Override
    public String toString() {
        return "Group [" + (name != null ? "name=" + name : "") + "]";
    }

    private static final Map<String, Group> groups = new HashMap<String, Group>();

    private final String name;

    private Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static final Group get(String name) {
        synchronized (groups) {
            if (!groups.containsKey(name)) {
                groups.put(name, new Group(name));
            }
        }
        return groups.get(name);
    }

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
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Group)) {
            return false;
        }
        Group other = (Group) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
