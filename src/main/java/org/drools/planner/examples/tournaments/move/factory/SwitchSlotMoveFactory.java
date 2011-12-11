package org.drools.planner.examples.tournaments.move.factory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.drools.planner.core.move.Move;
import org.drools.planner.core.move.factory.CachedMoveFactory;
import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.tournaments.TournamentsSolution;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.planner.examples.tournaments.move.SwitchSlotMove;

public class SwitchSlotMoveFactory extends CachedMoveFactory {

    @Override
    public List<Move> createCachedMoveList(@SuppressWarnings("rawtypes") Solution arg0) {
        TournamentsSolution sol = (TournamentsSolution) arg0;
        List<Move> moves = new LinkedList<>();
        Set<Slot> unvisited = new HashSet<>(sol.getSlotList());
        
        for (Slot s1 : sol.getSlotList()) {
            unvisited.remove(s1);
            for (Slot s2 : unvisited) {
            	if (s1.getNumber() != s2.getNumber()) {
            		moves.add(new SwitchSlotMove(s1, s2));
            	}
            }
        }
        return moves;
    }
}
