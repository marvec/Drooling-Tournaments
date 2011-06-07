package org.drools.planner.examples.tournaments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.drools.planner.config.XmlSolverConfigurer;
import org.drools.planner.core.Solver;
import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Team;


public final class Tournaments {
    
    private static class Execution implements Runnable {
        
        private Double time;
        private Solver solver;

        @Override
        public void run() {
            final XmlSolverConfigurer configurer = new XmlSolverConfigurer();
            configurer.configure(Tournaments.class.getResourceAsStream("/solverConfig.xml"));
            final TournamentsSolution startingSolution = new TournamentsSolution();
            solver = configurer.buildSolver();
            solver.setStartingSolution(startingSolution);
            time = Double.valueOf(System.nanoTime());
            solver.solve();
        }
        
        public void stop() {
            solver.terminateEarly();
            time = System.nanoTime() - time;

            // report results
            System.out.printf("         Done in: %d second(s).%n",
                    Math.max(1, Math.round(time / 1000 / 1000 / 1000)));
            outputSchedules();
            try {
                outputCSV();
                outputStats();
            } catch (Exception ex) {
                // just ignore
            }
        }
        
        private void outputSchedules() {
            System.out.println();
            for (Team t : Team.getAll()) {
                System.out.println("Schedule for " + t + ":");
                for (Match m : Match.getAll()) {
                    if (m.getTeams().contains(t)) {
                        System.out.println("  " + m.getTeams() + " @ " + m.getSlot());
                    }
                }
            }
        }

        private void outputSpecificCSV(Set<Entry<Court, Match[]>> s, File f) throws Exception {
            // print all the matches
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            for (Entry<Court, Match[]> entry : s) {
                for (int i = 0; i < entry.getValue().length; i++) {
                    Match m = entry.getValue()[i];
                    if (m != null) {
                        out.write(m.getTeams().toArray()[0] + " v. " + m.getTeams().toArray()[1]);
                    }
                    out.write(",");
                }
                out.newLine();
            }
            out.close();
        }
        
        private void outputStats() throws Exception {
            DescriptiveStatistics stat = new DescriptiveStatistics();
            for (Team t: Team.getAll()) {
                // gather all matches for a given team
                Set<Match> matches = new HashSet<Match>();
                for (Match m: Match.getAll()) {
                    if (!m.getTeams().contains(t)) continue;
                    matches.add(m);
                }
                // sort all the matches by their slot
                List<Match> sortedMatches = new LinkedList<Match>();
                for (int i = 0; i < matches.size(); i++) {
                    int minSlot = Integer.MAX_VALUE;
                    Match minSlotMatch = null;
                    for (Match m: matches) {
                        if (sortedMatches.contains(m)) continue;
                        if (m.getSlot().getNumber() > minSlot) continue;
                        minSlot = m.getSlot().getNumber();
                        minSlotMatch = m;
                    }
                    sortedMatches.add(minSlotMatch);
                }
                // add waiting periods
                for (int i = 1; i < sortedMatches.size(); i++) {
                    int origSlot = sortedMatches.get(i - 1).getSlot().getNumber(); 
                    int newSlot = sortedMatches.get(i).getSlot().getNumber();
                    int difference = newSlot - origSlot;
                    stat.addValue(difference);
                }
            }
            System.out.println();
            System.out.println("Team spans range from " + stat.getMin() + " to " + stat.getMax() + ".");
            System.out.println("Average is " + stat.getMean() + ".");
            int[] vals = new int[] {50, 96, 99};
            for (int val: vals) {
                System.out.println(val + " % values are between " + stat.getMin() + " and " + stat.getPercentile(val) + ".");
            }
        }

        private void outputCSV() throws Exception {
            // sort all the matches
            Map<Court, Match[]> matches = new HashMap<Court, Match[]>();
            for (Match m : Match.getAll()) {
                Court c = m.getSlot().getCourt();
                int position = m.getSlot().getNumber();
                if (!matches.containsKey(c)) {
                    matches.put(c, new Match[Match.getAll().size()]);
                }
                matches.get(c)[position] = m;
            }

            // output distances
            // output all
            outputSpecificCSV(matches.entrySet(), new File(System.getProperty("user.dir"), "ALL.csv"));
            for (Team t : Team.getAll()) {
                Map<Court, Match[]> teamMatches = new HashMap<Court, Match[]>();
                for (Match m : Match.getAll()) {
                    if (!m.getTeams().contains(t)) continue;
                    Court c = m.getSlot().getCourt();
                    int position = m.getSlot().getNumber();
                    if (!teamMatches.containsKey(c)) {
                        teamMatches.put(c, new Match[Match.getAll().size()]);
                    }
                    teamMatches.get(c)[position] = m;
                }
                outputSpecificCSV(teamMatches.entrySet(), new File(System.getProperty("user.dir"), t.getName() + ".csv"));
            }
        }
    }

    /**
     * Starts the application. As usual. :-)
     * 
     * @param args
     */
    public static void main(final String[] args) throws Exception {
        System.setProperty("drools.lrUnlinkingEnabled", "true");
        System.out.println("Press any key to start...");
        System.in.read();
        Execution e = new Execution();
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(e);
        char c = 0;
        while (c != 'Y') {
            System.out.println("Press Y and Enter to stop...");
            c = (char) System.in.read();
        }
        e.stop();
        es.shutdownNow();
    }
    

}