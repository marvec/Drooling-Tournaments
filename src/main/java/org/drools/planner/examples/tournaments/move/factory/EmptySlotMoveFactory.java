package org.drools.planner.examples.tournaments.move.factory;

import java.util.LinkedList;
import java.util.List;

import org.drools.planner.core.move.Move;
import org.drools.planner.core.move.factory.CachedMoveFactory;
import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.tournaments.TournamentsSolution;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.planner.examples.tournaments.move.FillSlotMove;


/*
 * Finds duplicate match assignments and create moves that can clear this assignment
 * @author mvecera
 */
public class EmptySlotMoveFactory extends CachedMoveFactory {

    @Override
    public List<Move> createCachedMoveList(@SuppressWarnings("rawtypes") Solution arg0) {
        TournamentsSolution sol = (TournamentsSolution) arg0;
        List<Move> moves = new LinkedList<>();
        List<Match> foundMatches = new LinkedList<>();
        for (Slot s: sol.getSlotList()) { 
        	if (foundMatches.contains(s.getMatch())) {
                moves.add(new FillSlotMove(null, s));            		
        	} else {
        		foundMatches.add(s.getMatch());
        	}
        }
        return moves;
    }

}
