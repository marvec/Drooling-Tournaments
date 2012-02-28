package org.drools.planner.examples.tournaments.model;

import java.util.Collection;

import org.drools.planner.api.domain.entity.PlanningEntity;
import org.drools.planner.api.domain.variable.PlanningVariable;
import org.drools.planner.api.domain.variable.ValueRangeFromSolutionProperty;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@PlanningEntity
@XStreamAlias(value = "slot")
public class Slot {

    @XStreamAsAttribute
    private Court court;
    @XStreamAsAttribute
    private int number;
    @XStreamAsAttribute
    private Match match = null;
    
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
    
    public Slot clone() {
      Slot s = new Slot(court, number);
      s.setMatch(match);
      
      return s;
    }

    public boolean isMinimalDistanceBroken(Slot s) {
        int diff = number - s.number;
        return diff < 2 && diff > -2;
    }
 
    @Override
    public String toString() {
        return "Slot [" + court + ", " + number + ", match=" + match + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((court == null) ? 0 : court.hashCode());
        result = prime * result + ((match == null) ? 0 : match.hashCode());
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
        if (court == null && other.court != null) {
            return false;
        }
        if (match == null && other.match != null) {
        	return false;
        }
        if (!court.equals(other.court) || number != other.number || !match.equals(other.match)) {
            return false;
        }
       
        return true;
    }
    
    public boolean solutionEquals(Object obj) {
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
        if (court == null && other.court != null) {
            return false;
        }
        if (number != other.number || !court.equals(other.court)) {
            return false;
        }

        return true;
    }

    public Court getCourt() {
        return court;
    }

    @PlanningVariable
    @ValueRangeFromSolutionProperty(propertyName = "matchList")
    public Match getMatch() {
        return match;
    }
    
    public void setMatch(Match m) {
        match = m;
    }
    
    public int getNumber() {
        return number;
    }
    
    public Collection<Team> getTeams() {
    	if (match instanceof TeamsMatch) {
    		return ((TeamsMatch) match).getTeams();
    	} else {
    		return null;
    	}
    }

    public boolean plays(Team t) {
        Collection<Team> teams = getTeams();
        if (teams == null) return false;
            
        Team[] tm = teams.toArray(new Team[teams.size()]);
        for (int i = 0; i < tm.length; i++) {
            if (t == tm[i]) return true;
        }
        
        return false;
    }
    
}
