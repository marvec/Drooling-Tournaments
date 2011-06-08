package org.drools.planner.examples.tournaments;

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
                Match m = Match.get(a, b);
                for (int slotNum = startingSlotNum;; slotNum++) {
                    for (Court c : getSolution(arg0).getCourts()) {
                        Slot s = new Slot(c, slotNum);
                        if (m.isPossible(s)) {
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
