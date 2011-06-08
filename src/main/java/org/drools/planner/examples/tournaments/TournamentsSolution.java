package org.drools.planner.examples.tournaments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.drools.planner.core.score.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Group;
import org.drools.planner.examples.tournaments.model.Match;
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
    public Map<Team, Map<Team, Match>> matches = new HashMap<Team, Map<Team, Match>>();
    @XStreamOmitField
    private List<Match> allMatches = new LinkedList<Match>();
    @XStreamOmitField
    private HardAndSoftScore score;
    @XStreamOmitField
    private boolean initialized;

    public Solution<HardAndSoftScore> cloneSolution() {
        TournamentsSolution s = new TournamentsSolution();
        s.groups = groups;
        s.teams = teams;
        s.courts = courts;
        // clone the matches
        for (Match m: this.allMatches) {
            Team[] ms = m.getTeams().toArray(new Team[2]); 
            Match nm = s.addMatch(ms[0], ms[1]);
            nm.setSlot(m.getSlot());
        }
        s.initialized = true;
        s.setScore(getScore());
        return s;
    }

    protected synchronized Match addMatch(Team a, Team b) {
        if (matches == null) { // funky stuff is happening with XStream
            matches = new HashMap<Team, Map<Team, Match>>();
        }
        if (!matches.containsKey(a)) matches.put(a, new HashMap<Team, Match>());
        if (!matches.containsKey(b)) matches.put(b, new HashMap<Team, Match>());
        if (matches.get(a).containsKey(b)) return matches.get(a).get(b);
        if (matches.get(b).containsKey(a)) return matches.get(b).get(a);
        Match m = new Match(a, b);
        matches.get(a).put(b, m);
        matches.get(b).put(a, m);
        if (allMatches == null) { // funky stuff is happening with XStream
          allMatches = new LinkedList<Match>();
        }
        allMatches.add(m);
        return m;
    }

    public Collection<? extends Object> getFacts() {
        List<Object> l = new ArrayList<Object>();
        l.addAll(getTeams());
        l.addAll(getAllMatches());
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
    
    public Collection<Match> getAllMatches() {
        if (allMatches == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableCollection(allMatches);
        }
    }

}
