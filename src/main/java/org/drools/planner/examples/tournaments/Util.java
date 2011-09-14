package org.drools.planner.examples.tournaments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.acl.Group;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.thoughtworks.xstream.core.ReferenceByIdMarshallingStrategy;

import org.drools.planner.examples.tournaments.model.Court;

import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

import com.thoughtworks.xstream.converters.reflection.NativeFieldKeySorter;

import com.thoughtworks.xstream.XStream;

import com.thoughtworks.xstream.converters.reflection.FieldDictionary;

import org.drools.ObjectFilter;
import org.drools.WorkingMemory;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Team;


public class Util {
    
    public static void toXStream(TournamentsSolution sol, File f) {
        XStream xs = getXStream(); 
        try {
            xs.toXML(sol, new FileWriter(f));
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
    private static XStream getXStream() {
        XStream xs = new XStream(new PureJavaReflectionProvider(new FieldDictionary(new NativeFieldKeySorter())));
        xs.setMarshallingStrategy(new ReferenceByIdMarshallingStrategy());
        xs.processAnnotations(TournamentsSolution.class);
        xs.processAnnotations(Court.class);
        xs.processAnnotations(Group.class);
        xs.processAnnotations(Team.class);
        return xs;
    }
    
    public static TournamentsSolution fromXStream(InputStream s) {
        return (TournamentsSolution)getXStream().fromXML(s);
        
    }
    
    private static class MatchFilter implements ObjectFilter {

        @Override
        public boolean accept(Object arg0) {
            return arg0 instanceof Match;
        }
        
    }

    public static boolean areTeamsShared(Collection<Team> teams1, Collection<Team> teams2) {
        Set<Team> s1 = new HashSet<Team>(teams1);
        s1.retainAll(teams2);
        return (s1.size() > 0);
    }

    public static boolean areSlotsShared(Match m1, Match m2) {
        return m1.getSlot().equals(m2.getSlot());
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
