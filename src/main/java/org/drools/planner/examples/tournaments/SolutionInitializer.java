package org.drools.planner.examples.tournaments;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.drools.planner.core.solution.initializer.StartingSolutionInitializer;
import org.drools.planner.core.solver.AbstractSolverScope;
import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Group;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.planner.examples.tournaments.model.Team;


public class SolutionInitializer implements StartingSolutionInitializer {

    private AtomicBoolean initialized = new AtomicBoolean(false);

    public void initializeSolution(AbstractSolverScope arg0) {
        Court[] courts = new Court[]{Court.get("A"), Court.get("B")};
        Group[] groups = new Group[]{Group.get("X"), Group.get("Y"), Group.get("Z"), Group.get("U")};
        Integer[] teamCounts = new Integer[]{8, 8, 8, 8};
        Set<Team> teams = new HashSet<Team>();
        // generate some random teams
        for (int i = 0; i < groups.length; i++) {
            for (int j = 0; j < teamCounts[i]; j++) {
                teams.add(new Team(groups[i].getName() + j, groups[i]));
            }
        }
        // generate matches between the teams
        for (Team a : teams) {
            int startingSlotNum = 0;
            for (Team b : teams) {
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
                    for (Court c : courts) {
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
        initialized.set(true);
    }

    public boolean isSolutionInitialized(AbstractSolverScope arg0) {
        return initialized.get();
    }

}
