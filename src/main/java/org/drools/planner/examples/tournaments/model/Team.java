package org.drools.planner.examples.tournaments.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "team")
public class Team implements Comparable<Team> {

    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private Group group;

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
