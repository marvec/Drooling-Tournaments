package org.drools.planner.examples.tournaments.move;

import java.util.LinkedList;
import java.util.List;

import org.drools.WorkingMemory;
import org.drools.planner.core.move.Move;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Slot;

/**
 * TODO: finish implementation
 * @author mvecera
 *
 */
public class PillarSwitchSlotMove implements Move {

    private final List<Slot> slots;

    public PillarSwitchSlotMove(Slot... slots) {
    	this.slots = new LinkedList<>();
    	for (Slot s: slots) {
    		this.slots.add(s);
    	}
    }

    public PillarSwitchSlotMove(List<Slot> slots) {
    	this.slots = new LinkedList<>(slots); 
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PillarSwitchSlotMove [");
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
        if (!(obj instanceof PillarSwitchSlotMove)) {
            return false;
        }
        PillarSwitchSlotMove other = (PillarSwitchSlotMove) obj;
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
        Slot[] s = getSlots().clone();
        
        // circulate matches backwards twice for the later doMove() to recreate the original state
        for (int cnt = 0; cnt < 2; cnt++) {
	        Match m = s[0].getMatch(); // store first match
	        for (int i = 1; i < s.length; i++) {
	            s[i - 1].setMatch(s[i].getMatch());
	        }
	        s[s.length - 1].setMatch(m);
        }
        
        PillarSwitchSlotMove pssm = new PillarSwitchSlotMove(s);

        return pssm;
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
    
        // circulate matches
        Match m = s[s.length - 1].getMatch(); // store last match
        for (int i = 1; i < s.length; i++) {
            s[i].setMatch(s[i - 1].getMatch());
        }
        s[0].setMatch(m);
        
        for (int i = 0; i < s.length; i++) {
            updateSlot(arg0, s[i]);
        }
    }

    @Override
    public boolean isMoveDoable(WorkingMemory arg0) {
        Slot[] s = getSlots();
        
        for (int i = 0; i < s.length; i++) {
        	for (int j = i + 1; j < s.length; j++) {
        		if (s[i].equals(s[j])) {
        			return false;
        		} else if (s[i].getNumber() == s[j].getNumber()) {
        			return false;
        		}
        	}
        }

        return true;
    }

}
