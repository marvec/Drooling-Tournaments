package org.drools.planner.examples.tournaments;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.drools.ObjectFilter;
import org.drools.WorkingMemory;
import org.drools.planner.examples.tournaments.model.Match;


public class Util {
    private static class MatchFilter implements ObjectFilter {

        @Override
        public boolean accept(Object arg0) {
            return arg0 instanceof Match;
        }
        
    }
    
    public static Collection<Match> getMatches(WorkingMemory arg0) {
        Set<Match> matches = new HashSet<Match>();
        Iterator<?> i = arg0.iterateObjects(new MatchFilter());
        while (i.hasNext()) {
            matches.add((Match)i.next());
        }
        return matches;
    }
    
}
