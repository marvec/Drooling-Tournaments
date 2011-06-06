package org.drools.planner.examples.tournaments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.planner.core.score.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.tournaments.model.Team;


public class TournamentsSolution implements Solution<HardAndSoftScore> {

    private HardAndSoftScore score;

    public Solution<HardAndSoftScore> cloneSolution() {
        TournamentsSolution s = new TournamentsSolution();
        s.setScore(getScore());
        return s;
    }

    public Collection<? extends Object> getFacts() {
        List<Object> l = new ArrayList<Object>();
        l.addAll(Team.getAll());
        return l;
    }

    public HardAndSoftScore getScore() {
        return score;
    }

    public void setScore(HardAndSoftScore arg0) {
        score = arg0;
    }

}
