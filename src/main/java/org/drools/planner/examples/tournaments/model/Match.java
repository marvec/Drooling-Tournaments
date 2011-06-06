package org.drools.planner.examples.tournaments.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Match {

    public static final Map<Team, Map<Team, Match>> matches = new HashMap<Team, Map<Team, Match>>();

    private final List<Team> teamsInMatch = new LinkedList<Team>();
    private Slot slot = null;

    private Match(Team a, Team b) {
        teamsInMatch.add(a);
        teamsInMatch.add(b);
    }

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

    /**
     * Whether or not a match can be placed in this slot. (No conflicting 
     * matches of one of the teams.)
     * @param s
     * @return
     */
    public boolean isPossible(Slot s) {
        return isPossible(s, Match.getAll());
    }

    public boolean isSwitchPossible(Match m2) {
        Set<Match> matches = new HashSet<Match>(Match.getAll());
        matches.remove(this);
        matches.remove(m2);
        return this.isPossible(m2.getSlot(), matches) && m2.isPossible(getSlot(), matches);
    }

    private boolean isPossible(Slot s, Collection<Match> matches) {
        for (Match m : matches) {
            if (m.equals(this)) {
                continue; // the same match
            }
            // the slot is already taken
            if (m.getSlot().equals(s)) return false;
            // if none of the teams play in this match, ignore the match
            boolean cntd = true;
            for (Team t: teamsInMatch) {
                if (m.teamsInMatch.contains(t)) cntd = false;
            }
            if (cntd) continue;
            // one of the teams play; make sure slot number doesn't conflict 
            if (s.getNumber() == m.getSlot().getNumber()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPushPossible(Slot s) {
        Set<Match> matches = new HashSet<Match>();
        matches.addAll(Match.getAll());
        matches.remove(this);
        for (Match m: matches) {
            // slot already filled
            if (m.getSlot().equals(s)) return false;
            // some of the other courts may have teams conflicting
            if (m.getSlot().getNumber() == s.getNumber()) {
                for (Team t: this.teamsInMatch) {
                    if (m.teamsInMatch.contains(t)) return false;
                }
            }
        }
        return true;
    }

    private static final List<Match> allMatches = new LinkedList<Match>();

    public static Collection<Match> getAll() {
        return Collections.unmodifiableList(allMatches);
    }
    
    public static int getUnderruns(Team t) {
        // get all matches by team
        Set<Integer> slots = new TreeSet<Integer>();
        for (Match m: Match.getAll()) {
            if (!m.teamsInMatch.contains(t)) continue;
            slots.add(m.getSlot().getNumber());
        }
        // find the differences
        int last = -1;
        int count = 0;
        for (int slot: slots) {
            if (last == -1) {
                last = slot;
                continue;
            }
            if ((slot - last) == 1) count++;
            last = slot;
        }
        return count;
    }

    public static int getSpanOverhead(Team t) {
        int highest = -1;
        int lowest = Integer.MAX_VALUE;
        int matchNum = 0;
        for (Match m: Match.getAll()) {
            if (!m.teamsInMatch.contains(t)) continue;
            matchNum++;
            int num = m.getSlot().getNumber(); 
            if (num > highest) highest = num;
            if (num < lowest) lowest = num;
        }
        int optimalLength = (matchNum * 2) - 1;
        int actualLength = highest - lowest + 1; 
        return Math.max(0, actualLength - optimalLength);
    }

    public static Match get(Team a, Team b) {
        synchronized (matches) {
            if (!matches.containsKey(a)) matches.put(a, new HashMap<Team, Match>());
            if (!matches.containsKey(b)) matches.put(b, new HashMap<Team, Match>());
            if (matches.get(a).containsKey(b)) return matches.get(a).get(b);
            if (matches.get(b).containsKey(a)) return matches.get(b).get(a);
            Match m = new Match(a, b);
            matches.get(a).put(b, m);
            matches.get(b).put(a, m);
            allMatches.add(m);
        }
        Match m = matches.get(a).get(b);
        return m;
    }

}
