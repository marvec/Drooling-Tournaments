package org.drools.planner.examples.tournaments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Group;
import org.drools.planner.examples.tournaments.model.TeamsMatch;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.planner.examples.tournaments.model.Team;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.NativeFieldKeySorter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.core.ReferenceByIdMarshallingStrategy;


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
        xs.processAnnotations(Slot.class);
        xs.processAnnotations(TeamsMatch.class);
        return xs;
    }
    
    public static TournamentsSolution fromXStream(InputStream s) {
        return (TournamentsSolution)getXStream().fromXML(s);
        
    }
    
}
