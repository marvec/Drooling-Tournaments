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
        for (Match m: matchList) {
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
    
    @PlanningEntityCollectionProperty
    public Collection<Match> getMatchList() {
        return this.matchList;
    }
    
    public void setMatchList(List<Match> matches) {
    	this.matchList = matches;
    }
    
}
