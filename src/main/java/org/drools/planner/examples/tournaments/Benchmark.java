package org.drools.planner.examples.tournaments;

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
        benchmarker.configure("/benchmarkConfig.xml");
        benchmarker.benchmark();
    }
    

}