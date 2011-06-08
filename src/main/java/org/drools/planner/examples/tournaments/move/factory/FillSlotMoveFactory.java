package org.drools.planner.examples.tournaments.move.factory;

import java.util.LinkedList;
import java.util.List;

import org.drools.planner.core.move.Move;
import org.drools.planner.core.move.factory.CachedMoveFactory;
import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.tournaments.TournamentsSolution;
import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.planner.examples.tournaments.move.FillSlotMove;


public class FillSlotMoveFactory extends CachedMoveFactory {

    private static final double MAX_SLOT_OVERFLOW_RATE = 0.04;
    
    public List<Move> createCachedMoveList(@SuppressWarnings("rawtypes") Solution arg0) {
        TournamentsSolution sol = (TournamentsSolution) arg0;
        // get some boundaries
        double idealMatchesPerCourt = Math.ceil(sol.getAllMatches().size() / sol.getCourts().size());
        double allowedMatchesPerCourt = idealMatchesPerCourt * (1 + MAX_SLOT_OVERFLOW_RATE);
        Integer upperBound = (int)Math.round(allowedMatchesPerCourt);
        Integer lowerBound = 0;
        // execute against those
        List<Move> moves = new LinkedList<Move>();
        for (Match m1 : sol.getAllMatches()) {
            for (Court c: sol.getCourts()) {
                for (int i = lowerBound; i < upperBound; i++) {
                    moves.add(new FillSlotMove(m1, new Slot(c, i)));
                }
            }
        }
        return moves;
    }

}
