package org.drools.planner.examples.tournaments.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Court {

    @Override
    public String toString() {
        return "Court " + name;
    }

    private static final Map<String, Court> courts = new HashMap<String, Court>();

    private final String name;

    private Court(String name) {
        this.name = name;
    }

    public static final Court get(String name) {
        synchronized (courts) {
            if (!courts.containsKey(name)) {
                courts.put(name, new Court(name));
            }
        }
        return courts.get(name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public static Collection<Court> getAll() {
        return Collections.unmodifiableCollection(courts.values());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Court)) {
            return false;
        }
        Court other = (Court) obj;
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
