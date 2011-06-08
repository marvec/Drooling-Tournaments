package org.drools.planner.examples.tournaments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.drools.planner.core.score.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Group;
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
    
    @XStreamOmitField
    private HardAndSoftScore score;
    @XStreamOmitField
    private boolean initialized;

    public Solution<HardAndSoftScore> cloneSolution() {
        TournamentsSolution s = new TournamentsSolution();
        s.groups = groups;
        s.teams = teams;
        s.courts = courts;
        s.initialized = true;
        s.setScore(getScore());
        return s;
    }

    public Collection<? extends Object> getFacts() {
        List<Object> l = new ArrayList<Object>();
        l.addAll(teams);
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
    
    protected void addGroup(Group g) {
        if (initialized) throw new IllegalStateException("Solution is already initialized!");
        groups.add(g);
    }

    protected void addCourt(Court c) {
        if (initialized) throw new IllegalStateException("Solution is already initialized!");
        courts.add(c);
    }

    protected void addTeam(Team t) {
        if (initialized) throw new IllegalStateException("Solution is already initialized!");
        teams.add(t);
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
    
    protected void setInitialized() {
        this.initialized = true;
    }
}
