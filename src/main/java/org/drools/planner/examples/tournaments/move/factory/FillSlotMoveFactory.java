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

public class FillSlotMoveFactory extends CachedMoveFactory {

	@Override
	public List<Move> createCachedMoveList(
			@SuppressWarnings("rawtypes") Solution arg0) {
		TournamentsSolution sol = (TournamentsSolution) arg0;
		List<Move> moves = new LinkedList<>();
		List<Match> used = new LinkedList<>();
		List<Slot> unusedSlots = new LinkedList<>();
		for (Slot s : sol.getSlotList()) {
			if (s.getMatch() != null) {
				used.add(s.getMatch());
			} else {
				unusedSlots.add(s);
			}
		}
		for (Slot s : unusedSlots) {
			if (s.getMatch() == null) {
				for (Match m : sol.getMatchList()) {
					if (!used.contains(m)) {
						moves.add(new FillSlotMove(m, s));
						used.add(m);
					}
				}
			}
		}
		return moves;
	}

}
