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

    private final Set<Slot> slots = new HashSet<>(2);

    public SwitchSlotMove(Slot s1, Slot s2) {
        slots.add(s1);
        slots.add(s2);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SwitchSlotMove [");
        builder.append(slots);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((slots == null) ? 0 : slots.hashCode());
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
        if (slots == null) {
            if (other.slots != null) {
                return false;
            }
        } else if (!slots.equals(other.slots)) {
            return false;
        }
        return true;
    }

    @Override
    public Move createUndoMove(WorkingMemory arg0) {
   		return this;
    }

    private Slot[] getSlots() {
        return slots.toArray(new Slot[slots.size()]);
    }

    private void updateSlot(WorkingMemory wm, Slot s) {
        wm.update(wm.getFactHandle(s), s);
    }

    @Override
    public void doMove(WorkingMemory arg0) {
        Slot[] s = getSlots();
        Match m = s[0].getMatch();
        s[0].setMatch(s[1].getMatch());
        s[1].setMatch(m);
        updateSlot(arg0, s[0]);
        updateSlot(arg0, s[1]);
    }

    @Override
    public boolean isMoveDoable(WorkingMemory arg0) {
        Slot[] s = getSlots();
        Slot s1 = s[0];
        Slot s2 = s[1];
        if (s1.equals(s2)) {
            return false;
        }
        if (s1.getNumber() == s2.getNumber()) {
            // switching matches in the same time slot doesn't help us
            return false;
        }
        return true;
    }

}
