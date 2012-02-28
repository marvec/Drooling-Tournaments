package org.drools.planner.examples.tournaments.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias(value = "match")
public class TeamsMatch implements Match {

    @XStreamImplicit(itemFieldName = "team")
    private final List<Team> teamsInMatch;

    public TeamsMatch() {
        this.teamsInMatch = Collections.unmodifiableList(new LinkedList<Team>());
    }

    public TeamsMatch(List<Team> teamsInMatch) {
        Collections.sort(teamsInMatch);
        this.teamsInMatch = Collections.unmodifiableList(teamsInMatch);
    }

    public Collection<Team> getTeams() {
        return teamsInMatch;
    }

    /*
     * public Match clone() { Match m = new Match();
     * m.teamsInMatch.addAll(teamsInMatch); return m; }
     */

    /*
     * @PlanningVariable
     * 
     * @ValueRangeFromSolutionProperty(propertyName = "slotList") public Slot
     * getSlot() { return slot; }
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Match [teamsInMatch=");
        builder.append(teamsInMatch);
        builder.append("]");
        return builder.toString();
    }

    public boolean areTeamsShared(Match m) {
        if (m instanceof TeamsMatch) {
            for (Team t1 : this.teamsInMatch) {
                for (Team t2 : ((TeamsMatch) m).teamsInMatch) {
                    if (t1 == t2)
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        if (teamsInMatch == null) {
            return prime * result + 0;
        }

        for (Team t : teamsInMatch) {
            result = prime * result + t.hashCode();
        }

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
        if (!(obj instanceof TeamsMatch)) {
            return false;
        }
        TeamsMatch other = (TeamsMatch) obj;
        if (teamsInMatch == null) {
            if (other.teamsInMatch != null) {
                return false;
            }
        } else if (other.teamsInMatch == null) {
            return false;
        } else if (teamsInMatch.size() != other.teamsInMatch.size()) {
            return false;
        }
        
        Team[] t1 = teamsInMatch.toArray(new Team[teamsInMatch.size()]);
        Team[] t2 = other.teamsInMatch.toArray(new Team[t1.length]);
        for (int i = 0; i < t1.length; i++) {
            if (t1[i] != t2[i]) return false;
        }
        
        return true;
        // the lists are sorted - equals compares corresponding pairs
        //return teamsInMatch.equals(other.teamsInMatch);*/
    }

}
