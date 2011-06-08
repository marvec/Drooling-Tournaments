package org.drools.planner.examples.tournaments.move;

import java.util.Collection;
import org.drools.WorkingMemory;
import org.drools.planner.core.move.Move;
import org.drools.planner.examples.tournaments.Util;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.planner.examples.tournaments.model.Team;
import org.drools.runtime.rule.FactHandle;


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
        builder.append("SwitchSlotMove [match1=");
        builder.append(match1);
        builder.append(", match2=");
        builder.append(match2);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((match1 == null) ? 0 : match1.hashCode());
        result = prime * result + ((match2 == null) ? 0 : match2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SwitchSlotMove)) {
            return false;
        }
        SwitchSlotMove other = (SwitchSlotMove) obj;
        if (match1 != other.match1) {
            return false;
        }
        if (match2 != other.match2) {
            return false;
        }
        return true;
    }

    private final Match match1, match2;

    public SwitchSlotMove(Match m1, Match m2) {
        match1 = m1;
        match2 = m2;
    }

    public Move createUndoMove(WorkingMemory arg0) {
        return new SwitchSlotMove(match2, match1);
    }

    public void doMove(WorkingMemory arg0) {
        Slot s = match1.getSlot();
        match1.setSlot(match2.getSlot());
        match2.setSlot(s);
        FactHandle fh = arg0.getFactHandle(match1);
        arg0.update(fh, match1);
        fh = arg0.getFactHandle(match2);
        arg0.update(fh, match2);
    }

    public boolean isMoveDoable(WorkingMemory arg0) {
        if (match1.equals(match2)) {
            return false;
        }
        if (match1.getSlot().getNumber() == match2.getSlot().getNumber()) {
            // switching matches in the same time slot doesn't help us
            return false;
        }
        Collection<Match> matches = Util.getMatches(arg0);
        matches.remove(match1);
        matches.remove(match2);
        return isPossible(match1, match2.getSlot(), matches) && isPossible(match2, match1.getSlot(), matches);
    }

    private boolean isPossible(Match m, Slot s, Collection<Match> matches) {
        for (Match m2 : matches) {
            if (m2.equals(m)) {
                continue; // the same match
            }
            // the slot is already taken
            if (m2.getSlot().equals(s)) return false;
            // if none of the teams play in this match, ignore the match
            boolean cntd = true;
            for (Team t: m.getTeams()) {
                if (m2.getTeams().contains(t)) cntd = false;
            }
            if (cntd) continue;
            // one of the teams play; make sure slot number doesn't conflict 
            if (s.getNumber() == m2.getSlot().getNumber()) {
                return false;
            }
        }
        return true;
    }

}
