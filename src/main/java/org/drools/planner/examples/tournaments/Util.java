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
    
}
