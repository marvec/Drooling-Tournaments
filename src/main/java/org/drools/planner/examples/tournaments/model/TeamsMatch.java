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

}
