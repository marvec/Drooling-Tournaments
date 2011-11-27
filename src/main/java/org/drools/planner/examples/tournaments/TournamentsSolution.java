package org.drools.planner.examples.tournaments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.drools.planner.api.domain.solution.PlanningEntityCollectionProperty;
import org.drools.planner.core.score.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Group;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.planner.examples.tournaments.model.Team;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("tournament")
public class TournamentsSolution implements Solution<HardAndSoftScore> {
    
    @XStreamImplicit(itemFieldName = "group")
    List<Group> groups = new LinkedList<Group>();
    @XStreamImplicit(itemFieldName = "court")
    List<Court> courts = new LinkedList<Court>();
    @XStreamImplicit(itemFieldName = "team")
    List<Team> teams = new LinkedList<Team>();
    @XStreamImplicit(itemFieldName = "match")
    private List<Match> matchList = new LinkedList<Match>();
    
    @XStreamOmitField
    private List<Slot> slotList = null;
    @XStreamOmitField
    private HardAndSoftScore score;

    private static final double MAX_SLOT_OVERFLOW_RATE = 0.04;

    public synchronized List<Slot> getSlotList() {
        if (slotList == null) {
            slotList = new LinkedList<Slot>();
            // initialize list of slots
            double idealMatchesPerCourt = Math.ceil(this.getMatchList().size() / this.getCourts().size());
            double allowedMatchesPerCourt = idealMatchesPerCourt * (1 + MAX_SLOT_OVERFLOW_RATE);
            Integer upperBound = (int)Math.round(allowedMatchesPerCourt);
            Integer lowerBound = 0;
            for (Court c: this.getCourts()) {
                for (int i = lowerBound; i < upperBound; i++) {
                    slotList.add(new Slot(c, i));
                }
            }
        }
        return this.slotList;
    }
    
    public Solution<HardAndSoftScore> cloneSolution() {
        TournamentsSolution s = new TournamentsSolution();
        s.groups = groups;
        s.teams = teams;
        s.courts = courts;
//        s.matchList = matchList;
        for (Match m : matchList) {
            s.matchList.add(m.clone());
        }
        s.setScore(getScore());
        return s;
    }

    public Collection<? extends Object> getProblemFacts() {
        List<Object> l = new ArrayList<Object>();
        l.addAll(getTeams());

        return l;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TournamentsSolution [groups=");
        builder.append(groups);
        builder.append(", courts=");
        builder.append(courts);
        builder.append(", teams=");
        builder.append(teams);
        builder.append(", score=");
        builder.append(score);
        builder.append("]");
        return builder.toString();
    }

    public HardAndSoftScore getScore() {
        return score;
    }

    public void setScore(HardAndSoftScore arg0) {
        score = arg0;
    }
    
    public Collection<Team> getTeams() {
        return Collections.unmodifiableCollection(this.teams);
    }
    
    public Collection<Court> getCourts() {
        return Collections.unmodifiableCollection(this.courts);
    }
    
    public Collection<Group> getGroups() {
        return Collections.unmodifiableCollection(this.groups);
    }
    
    // Suppose any order of Teams and generate matches like following:
    // X vs. Y for all X, Y from Teams where X.Group == Y.Group and X != Y and X < Y
    private List<Match> generateMatches() {
        List<Match> matches = new LinkedList<>();
        Team[] teams = this.teams.toArray(new Team[this.teams.size()]);

        for (int i = 0; i < teams.length; i++) {
            for (int j = i + 1; j < teams.length; j++) {
                Team teamA = teams[i];
                Team teamB = teams[j];
                if (teamA.getGroup() == teamB.getGroup()) {
                    List<Team> teamsInMatch = new LinkedList<>();
                    teamsInMatch.add(teamA);
                    teamsInMatch.add(teamB);
                    Match m = new Match(teamsInMatch);
                    matches.add(m);
                }
            }
        }

        return Collections.unmodifiableList(matches);
    }

    
    @PlanningEntityCollectionProperty
    public Collection<Match> getMatchList() {
        if (matchList == null || matchList.size() == 0) {
            matchList= generateMatches();
        }
        return this.matchList;
    }
    
    public void setMatchList(List<Match> matches) {
    	this.matchList = matches;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((matchList == null) ? 0 : matchList.hashCode());
        result = prime * result + ((score == null) ? 0 : score.hashCode());
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
        if (!(obj instanceof TournamentsSolution)) {
            return false;
        }
        TournamentsSolution other = (TournamentsSolution) obj;
        if (score == null) {
            if (other.score != null) {
                return false;
            }
        } else if (!score.equals(other.score)) {
            return false;
        } 
        if (matchList == null) {
            if (other.matchList != null) {
                return false;
            }
        } else if (!matchList.equals(other.matchList)) {
            return false;
        } 
        return true;
    }
    
}
