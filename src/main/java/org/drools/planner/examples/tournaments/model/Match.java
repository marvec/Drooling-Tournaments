package org.drools.planner.examples.tournaments.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias(value = "match")
public class Match {

    @XStreamImplicit(itemFieldName = "team")
    private final List<Team> teamsInMatch;
    
    public Match() {
        this.teamsInMatch = new LinkedList<Team>();
    }

    public Match(List<Team> teamsInMatch) {
        this.teamsInMatch = teamsInMatch;
    }

/*    public Match clone() {
        Match m = new Match();
        m.teamsInMatch.addAll(teamsInMatch);
        return m;
    }*/
    
/*    @PlanningVariable
    @ValueRangeFromSolutionProperty(propertyName = "slotList")
    public Slot getSlot() {
        return slot;
    }*/
    
    public Collection<Team> getTeams() {
        return Collections.unmodifiableList(teamsInMatch);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Match [teamsInMatch=");
        builder.append(teamsInMatch);
        builder.append("]");
        return builder.toString();
    }

    public boolean areTeamsShared(Match m) {
        for (Team t1: this.teamsInMatch) {
            for (Team t2: m.teamsInMatch) {
                if (t1 == t2) return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((teamsInMatch == null) ? 0 : teamsInMatch.hashCode());
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
        if (!(obj instanceof Match)) {
            return false;
        }
        Match other = (Match) obj;
        if (teamsInMatch == null) {
            if (other.teamsInMatch != null) {
                return false;
            }
        } else if (!teamsInMatch.equals(other.teamsInMatch)) {
            return false;
        }
        return true;
    }

}
