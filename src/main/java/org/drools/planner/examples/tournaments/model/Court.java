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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        if (!(obj instanceof Court)) {
            return false;
        }
        Court other = (Court) obj;
        if (this.name == other.name) {
            return true;
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

}
