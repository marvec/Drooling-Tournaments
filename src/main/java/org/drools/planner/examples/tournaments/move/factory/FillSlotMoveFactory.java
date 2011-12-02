package org.drools.planner.examples.tournaments.move.factory;

import java.util.Iterator;
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
		List<Match> used = new LinkedList<>(); // matches assigned to slots
		List<Match> unusedMatches = new LinkedList<>(sol.getMatchList()); // matches that are not assigned to any slot
		List<Slot> duplicate = new LinkedList<>(); // slots that use already seen match
		List<Slot> unusedSlots = new LinkedList<>(); // slots with null match
		
		// First, find free slots, used and duplicate matches
		for (Slot s : sol.getSlotList()) {
			if (s.getMatch() != null) {
				if (used.contains(s.getMatch())) {
					duplicate.add(s);
				} else {
					used.add(s.getMatch());
				}
			} else {
				unusedSlots.add(s);
			}
		}
		for (Match m: used) {
			unusedMatches.remove(m);
		}
		Iterator<Match> it = unusedMatches.iterator();
		
		// Fill unused slots
		for (Slot s : unusedSlots) {
			if (s.getMatch() == null && it.hasNext()) {				
				moves.add(new FillSlotMove(it.next(), s));
			}
		}
		
		// Change slots with duplicate matches to use a match from the unusedMatches set
		if (duplicate.size() > 0 && it.hasNext()) { // we have duplicate matches in slots			
			for (Slot s: duplicate) {
				if (it.hasNext()) {
					moves.add(new FillSlotMove(it.next(), s));
				}
			}
		}
		
		return moves;
	}

}
