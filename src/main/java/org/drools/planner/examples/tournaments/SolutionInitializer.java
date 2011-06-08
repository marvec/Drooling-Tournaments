package org.drools.planner.examples.tournaments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.drools.planner.core.solution.initializer.StartingSolutionInitializer;
import org.drools.planner.core.solver.AbstractSolverScope;
import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.planner.examples.tournaments.model.Team;


public class SolutionInitializer implements StartingSolutionInitializer {

    private AtomicBoolean initialized = new AtomicBoolean(false);
    
    private TournamentsSolution getSolution(AbstractSolverScope arg0) {
        return (TournamentsSolution)arg0.getWorkingSolution();
    }
    
    private boolean isPossible(Match m, Slot s, Collection<Match> matches) {
        List<Match> ms = new ArrayList<Match>(matches);
        ms.remove(m);
        for (Match m2: ms) {
            // slot already filled
            if (m2.getSlot().equals(s)) return false;
            // some of the other courts may have teams conflicting
            if (m2.getSlot().getNumber() == s.getNumber()) {
                for (Team t: m.getTeams()) {
                    if (m2.getTeams().contains(t)) return false;
                }
            }
        }
        return true;
    }

    public void initializeSolution(AbstractSolverScope arg0) {
        // generate matches between the teams
        for (Team a : getSolution(arg0).getTeams()) {
            int startingSlotNum = 0;
            for (Team b : getSolution(arg0).getTeams()) {
                // teams only play other teams in the same group
                if (a.equals(b)) {
                    continue;
                }
                if (!a.getGroup().equals(b.getGroup())) {
                    continue;
                }
                // prepare the match
                Match m = getSolution(arg0).addMatch(a, b);
                for (int slotNum = startingSlotNum;; slotNum++) {
                    for (Court c : getSolution(arg0).getCourts()) {
                        Slot s = new Slot(c, slotNum);
                        if (isPossible(m, s, getSolution(arg0).getAllMatches())) {
                            m.setSlot(s);
                            break;
                        }
                    }
                    if (m.getSlot() != null) {
                        startingSlotNum = slotNum + 2;
                        break;
                    } else {
                        startingSlotNum = slotNum + 1;
                    }
                }
                arg0.getWorkingMemory().insert(m);
            }
            arg0.getWorkingMemory().insert(a);
        }
        getSolution(arg0).setInitialized();
        initialized.set(true);
    }

    public boolean isSolutionInitialized(AbstractSolverScope arg0) {
        return initialized.get();
    }

}
