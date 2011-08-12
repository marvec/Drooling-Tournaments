package org.drools.planner.examples.tournaments.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.drools.planner.api.domain.entity.PlanningEntity;
import org.drools.planner.api.domain.variable.PlanningVariable;
import org.drools.planner.api.domain.variable.ValueRangeFromSolutionProperty;

@PlanningEntity
public class Match {

    private final List<Team> teamsInMatch = new LinkedList<Team>();
    private Slot slot = null;

    public Match(Team a, Team b) {
        teamsInMatch.add(a);
        teamsInMatch.add(b);
    }

    @PlanningVariable
    @ValueRangeFromSolutionProperty(propertyName = "slotList")
    public Slot getSlot() {
        return slot;
    }
    
    public Collection<Team> getTeams() {
        return Collections.unmodifiableList(teamsInMatch);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Match [teamsInMatch=");
        builder.append(teamsInMatch);
        builder.append(", slot=");
        builder.append(slot);
        builder.append("]");
        return builder.toString();
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

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

}
