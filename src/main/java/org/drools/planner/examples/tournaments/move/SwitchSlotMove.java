package org.drools.planner.examples.tournaments.move;

import java.util.HashSet;
import java.util.Set;

import org.drools.WorkingMemory;
import org.drools.planner.core.move.Move;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Slot;

/**
 * The purpose of this move is to find better places for matches in the roster
 * by switching two of them.
 * @author lpetrovi
 *
 */
public class SwitchSlotMove implements Move {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SwitchSlotMove [");
        builder.append(matches);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((matches == null) ? 0 : matches.hashCode());
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
        if (!(obj instanceof SwitchSlotMove)) {
            return false;
        }
        SwitchSlotMove other = (SwitchSlotMove) obj;
        if (matches == null) {
            if (other.matches != null) {
                return false;
            }
        } else if (!matches.equals(other.matches)) {
            return false;
        }
        return true;
    }

    private final Set<Match> matches = new HashSet<Match>();

    public SwitchSlotMove(Match m1, Match m2) {
        matches.add(m1);
        matches.add(m2);
    }

    @Override
    public Move createUndoMove(WorkingMemory arg0) {
        return this;
    }

    private Match[] getMatches() {
        return matches.toArray(new Match[2]);
    }

    private void updateMatch(WorkingMemory wm, Match m) {
        wm.update(wm.getFactHandle(m), m);
    }

    @Override
    public void doMove(WorkingMemory arg0) {
        Match[] m = getMatches();
        Match match1 = m[0];
        Match match2 = m[1];
        Slot s = match1.getSlot();
        match1.setSlot(match2.getSlot());
        match2.setSlot(s);
        updateMatch(arg0, match1);
        updateMatch(arg0, match2);
    }

    @Override
    public boolean isMoveDoable(WorkingMemory arg0) {
        Match[] m = getMatches();
        Match match1 = m[0];
        Match match2 = m[1];
        if (match1.equals(match2)) {
            return false;
        }
        if (match1.getSlot().getNumber() == match2.getSlot().getNumber()) {
            // switching matches in the same time slot doesn't help us
            return false;
        }
        return true;
    }

}
