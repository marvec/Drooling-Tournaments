package org.drools.planner.examples.tournaments.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias(value = "court")
public class Court {

    @Override
    public String toString() {
        return "Court " + name;
    }

    @XStreamAsAttribute
    private String name;
    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }

}
