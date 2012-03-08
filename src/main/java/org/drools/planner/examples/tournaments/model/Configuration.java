package org.drools.planner.examples.tournaments.model;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "configuration")
public class Configuration implements Serializable {

	private static final long serialVersionUID = 1231317902784807427L;

	@XStreamAsAttribute
	private final int slotsPerMatch;
    @XStreamAsAttribute
	private final double pauseSlotsPercent;
	
	public Configuration() {
		slotsPerMatch = 2;
		pauseSlotsPercent = 0.04;
	}

	public Configuration(int slotsPerMatch, double pauseSlotsPercent) {
		this.slotsPerMatch = slotsPerMatch;
		this.pauseSlotsPercent = pauseSlotsPercent;
	}

	@Override
    public String toString() {
		StringBuilder sb = new StringBuilder("Configuration [");
		sb.append(slotsPerMatch);
		sb.append(", ");
		sb.append(pauseSlotsPercent);
		sb.append("]");
        return sb.toString();
    }

	public int getSlotsPerMatch() {
		return slotsPerMatch;
	}

	public double getPauseSlotsPercent() {
		return pauseSlotsPercent;
	}

	public boolean isMinimalDistanceBroken(Slot s1, Slot s2) {
        int diff = s1.getNumber() - s2.getNumber();
        return diff < slotsPerMatch && diff > -slotsPerMatch;
    }

}
