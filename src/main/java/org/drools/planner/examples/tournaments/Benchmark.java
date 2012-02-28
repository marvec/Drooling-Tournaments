package org.drools.planner.examples.tournaments;

import java.security.acl.Group;

import org.drools.planner.benchmark.config.XmlPlannerBenchmarkFactory;
import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Team;

public final class Benchmark {
    
    /**
     * Starts the application. As usual. :-)
     * 
     * @param args
     */
    public static void main(final String[] args) throws Exception {
        XmlPlannerBenchmarkFactory benchmarker = new XmlPlannerBenchmarkFactory();
        benchmarker.addXstreamAnnotations(TournamentsSolution.class);
        benchmarker.addXstreamAnnotations(Court.class);
        benchmarker.addXstreamAnnotations(Group.class);
        benchmarker.addXstreamAnnotations(Team.class);
        benchmarker.configure("/benchmarkConfig.xml");
        benchmarker.buildPlannerBenchmark();
    }
    

}