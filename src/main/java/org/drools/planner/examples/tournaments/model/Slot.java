package org.drools.planner.examples.tournaments.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias(value = "slot")
public class Slot {

    @XStreamAsAttribute
    private Court court;
    @XStreamAsAttribute
    private int number;

    public Slot() { }

    public Slot(Court c, int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Slot number must be 0 or more, not " + number + ".");
        }
        if (c == null) {
            throw new IllegalArgumentException("Please provide a court for the slot!");
        }
        court = c;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Slot [" + court + ", " + number + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((court == null) ? 0 : court.hashCode());
        result = prime * result + number;
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
        if (!(obj instanceof Slot)) {
            return false;
        }
        Slot other = (Slot) obj;
        if (court == null) {
            if (other.court != null) {
                return false;
            }
        } else if (!court.equals(other.court)) {
            return false;
        }
        if (number != other.number) {
            return false;
        }
        return true;
    }

    public Court getCourt() {
        return court;
    }

    public int getNumber() {
        return number;
    }

}
