package org.drools.planner.examples.tournaments;

import java.security.acl.Group;

import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Team;

import org.drools.planner.benchmark.XmlSolverBenchmarker;

public final class Benchmark {
    
    /**
     * Starts the application. As usual. :-)
     * 
     * @param args
     */
    public static void main(final String[] args) throws Exception {
        XmlSolverBenchmarker benchmarker = new XmlSolverBenchmarker();
        benchmarker.addXstreamAnnotations(TournamentsSolution.class);
        benchmarker.addXstreamAnnotations(Court.class);
        benchmarker.addXstreamAnnotations(Group.class);
        benchmarker.addXstreamAnnotations(Team.class);
        benchmarker.configure("/benchmarkConfig.xml");
        benchmarker.benchmark();
    }
    

}