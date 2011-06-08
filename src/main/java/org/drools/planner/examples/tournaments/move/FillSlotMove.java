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
 * The purpose of this move is to fill empty spaces in the roster.
 * @author lpetrovi
 *
 */
public class FillSlotMove implements Move {

    private final Match match1;
    private final Slot newSlot;

    public FillSlotMove(Match m1, Slot newSlot) {
        match1 = m1;
        this.newSlot = newSlot;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((match1 == null) ? 0 : match1.hashCode());
        result = prime * result + ((newSlot == null) ? 0 : newSlot.hashCode());
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
        if (!(obj instanceof FillSlotMove)) {
            return false;
        }
        FillSlotMove other = (FillSlotMove) obj;
        if (match1 == null) {
            if (other.match1 != null) {
                return false;
            }
        } else if (!match1.equals(other.match1)) {
            return false;
        }
        if (newSlot == null) {
            if (other.newSlot != null) {
                return false;
            }
        } else if (!newSlot.equals(other.newSlot)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PushSlotMove [match1=");
        builder.append(match1);
        builder.append(", ");
        builder.append(newSlot);
        builder.append("]");
        return builder.toString();
    }

    public Move createUndoMove(WorkingMemory arg0) {
        return new FillSlotMove(match1, match1.getSlot());
    }

    public void doMove(WorkingMemory arg0) {
        match1.setSlot(newSlot);
        FactHandle fh1 = arg0.getFactHandle(match1);
        arg0.update(fh1, match1);
    }
    
    public boolean isMoveDoable(WorkingMemory arg0) {
        if (match1.getSlot().equals(newSlot)) {
            return false;
        }
        Collection<Match> matches = Util.getMatches(arg0);
        matches.remove(match1);
        for (Match m: matches) {
            // slot already filled
            if (m.getSlot().equals(newSlot)) return false;
            // some of the other courts may have teams conflicting
            if (m.getSlot().getNumber() == newSlot.getNumber()) {
                for (Team t: match1.getTeams()) {
                    if (m.getTeams().contains(t)) return false;
                }
            }
        }
        return true;
    }

}
