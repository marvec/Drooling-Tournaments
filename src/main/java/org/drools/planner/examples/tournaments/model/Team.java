package org.drools.planner.examples.tournaments.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "team")
public class Team implements Comparable<Team> {

    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private Group group;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
        /*if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Team)) {
            return false;
        }
        Team other = (Team) obj;
        if (group == null) {
            if (other.group != null) {
                return false;
            }
        } else if (this.name == other.name) {
            return this.group.equals(other.group);
        } else if (!group.equals(other.group)) {
            return false;
        } 
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        } 
        return true;*/
    }

    public int compareTo(Team t) {
        return (name + "/" + group.getName()).compareTo(t.name + "/" + t.group.getName());
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

    
    public void setName(String name) {
        this.name = name;
    }

    
    public void setGroup(Group group) {
        this.group = group;
    }

}
