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
