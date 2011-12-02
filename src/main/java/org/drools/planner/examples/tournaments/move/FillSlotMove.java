package org.drools.planner.examples.tournaments.move;

import org.drools.WorkingMemory;
import org.drools.planner.core.move.Move;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.runtime.rule.FactHandle;

/**
 * The purpose of this move is to fill empty spaces in the roster.
 * @author lpetrovi,mvecera
 *
 */
public class FillSlotMove implements Move {

    private final Match newMatch;
    private final Slot slot;

    public FillSlotMove(Match newMatch, Slot slot) {
        this.newMatch = newMatch;
        this.slot = slot;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((newMatch == null) ? 0 : newMatch.hashCode());
        result = prime * result + ((slot == null) ? 0 : slot.hashCode());
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
        if (newMatch == null) {
            if (other.newMatch != null) {
                return false;
            }
        } else if (!newMatch.equals(other.newMatch)) {
            return false;
        }
        if (slot == null) {
            if (other.slot != null) {
                return false;
            }
        } else if (!slot.equals(other.slot)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FillSlotMove [match=");
        builder.append(newMatch);
        builder.append(", ");
        builder.append(slot);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public Move createUndoMove(WorkingMemory arg0) {
        return new FillSlotMove(slot.getMatch(), slot);
    }

    @Override
    public void doMove(WorkingMemory arg0) {
        slot.setMatch(newMatch);
        FactHandle fh1 = arg0.getFactHandle(slot);
        if (slot.getMatch() == null) {
        	System.out.println("NULL MATCH");
        }
        if (fh1 == null) {
    		System.out.println("UNKNOWN SLOT " + slot);
        	
        }
        arg0.update(fh1, slot);
    }

    @Override
    public boolean isMoveDoable(WorkingMemory arg0) {
        return slot.getMatch() == null || !slot.getMatch().equals(newMatch);
    }

}
