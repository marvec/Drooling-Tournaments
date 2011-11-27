package org.drools.planner.examples.tournaments.move;

import org.drools.WorkingMemory;
import org.drools.planner.core.move.Move;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.runtime.rule.FactHandle;

/**
 * The purpose of this move is to fill empty spaces in the roster.
 * @author lpetrovi
 *
 */
public class FillSlotMove implements Move {

    private final Match match;
    private final Slot newSlot;

    public FillSlotMove(Match m1, Slot newSlot) {
        match = m1;
        this.newSlot = newSlot;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((match == null) ? 0 : match.hashCode());
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
        if (match == null) {
            if (other.match != null) {
                return false;
            }
        } else if (!match.equals(other.match)) {
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
        builder.append("FillSlotMove [match=");
        builder.append(match);
        builder.append(", ");
        builder.append(newSlot);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public Move createUndoMove(WorkingMemory arg0) {
        return new FillSlotMove(match, match.getSlot());
    }

    @Override
    public void doMove(WorkingMemory arg0) {
        match.setSlot(newSlot);
        FactHandle fh1 = arg0.getFactHandle(match);
        arg0.update(fh1, match);
    }

    @Override
    public boolean isMoveDoable(WorkingMemory arg0) {
        if (match.getSlot().equals(newSlot)) {
            return false;
        }
        return true;
    }

}
