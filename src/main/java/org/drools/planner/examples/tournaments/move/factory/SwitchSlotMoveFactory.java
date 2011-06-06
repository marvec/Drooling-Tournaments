package org.drools.planner.examples.tournaments.move.factory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.drools.planner.core.move.Move;
import org.drools.planner.core.move.factory.CachedMoveFactory;
import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.move.SwitchSlotMove;


public class SwitchSlotMoveFactory extends CachedMoveFactory {

    public List<Move> createCachedMoveList(@SuppressWarnings("rawtypes") Solution arg0) {
        List<Move> moves = new LinkedList<Move>();
        Set<Match> unvisited = new HashSet<Match>(Match.getAll());
        for (Match m1 : Match.getAll()) {
            unvisited.remove(m1);
            for (Match m2 : unvisited) {
                moves.add(new SwitchSlotMove(m1, m2));
            }
        }
        return moves;
    }

}
