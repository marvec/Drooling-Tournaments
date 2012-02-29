package org.drools.planner.examples.tournaments.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias(value = "group")
public class Group {

    @Override
    public String toString() {
        return "Group [" + (name != null ? "name=" + name : "") + "]";
    }

    @XStreamAsAttribute
    private String name;
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
