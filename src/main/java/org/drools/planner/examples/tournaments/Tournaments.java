package org.drools.planner.examples.tournaments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.drools.planner.config.XmlSolverConfigurer;
import org.drools.planner.core.Solver;
import org.drools.planner.core.score.HardAndSoftScore;
import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.PauseMatch;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.planner.examples.tournaments.model.Team;
import org.drools.planner.examples.tournaments.model.TeamsMatch;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public final class Tournaments {

	private static class Execution implements Runnable {

		private volatile long time = System.nanoTime();
		private Solver solver;

		private TournamentsSolution getInitialSolution() {
			XStream xs = new XStream(new DomDriver());
			xs.processAnnotations(TournamentsSolution.class);
			TournamentsSolution sol = Util.fromXStream(Tournaments.class
					.getResourceAsStream("/input-my.xml"));
			return sol;

		}

		@Override
		public void run() {
			final XmlSolverConfigurer configurer = new XmlSolverConfigurer();
			configurer.configure(Tournaments.class
					.getResourceAsStream("/solverConfig.xml"));
			solver = configurer.buildSolver();
			solver.setPlanningProblem(getInitialSolution());
			System.out.printf("Boot time: %ds%n", Math.round(((double) (System
					.nanoTime() - time)) / 1000 / 1000 / 1000));
			time = System.nanoTime();
			solver.solve();
		}

		private void printScore() {
			if (solver != null) {
				TournamentsSolution bestSolution = (TournamentsSolution) solver
						.getBestSolution();
				if (bestSolution != null) {
					HardAndSoftScore score = (HardAndSoftScore) bestSolution
							.getScore();
					System.out.printf("Score: %d hard/%d soft%n",
							score.getHardScore(), score.getSoftScore());
				}
			}
		}

		public void stop() {
			solver.terminateEarly();
			time = System.nanoTime() - time;

			// report results
			System.out.printf(
					"Done in: %ds%n",
					Math.max(1,
							Math.round(((double) time) / 1000 / 1000 / 1000)));
			printScore();
			TournamentsSolution bestSolution = (TournamentsSolution) solver
					.getBestSolution();
			outputSchedules(bestSolution);
			outputPauses(bestSolution);
			try {
				outputCSV(bestSolution);
				outputStats(bestSolution);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public boolean isRunning() {
			return (solver == null) ? true : solver.isSolving();
		}

		private void outputSchedules(TournamentsSolution sol) {
			System.out.println();
			for (Team t : sol.getTeams()) {
				System.out.println("Schedule for " + t + ":");
				for (Slot s : sol.getSlotList()) {
					if (s.getMatch() instanceof TeamsMatch) {
						TeamsMatch tm = (TeamsMatch) s.getMatch();
						if (tm.getTeams().contains(t)) {
							System.out.println("  "	+ tm.getTeams() + " @ " + s);
						}
					}
				}
			}
		}
		
		private void outputPauses(TournamentsSolution sol) {
			System.out.println("Schedule for pauses:");
			for (Slot s : sol.getSlotList()) {
				if (s.getMatch() instanceof PauseMatch) {
					System.out.println("  PAUSE @ " + s);
				}
			}			
		}

		private void outputSpecificCSV(Set<Entry<Court, Match[]>> s, File f)
				throws Exception {
			// print all the matches
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			for (Entry<Court, Match[]> entry : s) {
				for (int i = 0; i < entry.getValue().length; i++) {
					Match m = entry.getValue()[i];
					if (m instanceof TeamsMatch) {
						TeamsMatch tm = (TeamsMatch) m;
						out.write(tm.getTeams().toArray()[0] + " v. "
								+ tm.getTeams().toArray()[1]);
					} else {
						out.write("PAUSE");
					}
					out.write(",");
				}
				out.newLine();
			}
			out.close();
		}

		private void outputStats(TournamentsSolution sol) throws Exception {
			DescriptiveStatistics stat = new DescriptiveStatistics();
			for (Team t : sol.getTeams()) {
				// gather all slots for the given team
				Set<Slot> slots = new HashSet<>();
				for (Slot s : sol.getSlotList()) {
					if (s.getMatch() instanceof TeamsMatch && ((TeamsMatch) s.getMatch()).getTeams().contains(t)) {
						slots.add(s);
					}
				}

				// compute differences
				int[] slotArray = new int[slots.size()];
				int i = 0;
				for (Slot s: slots) {
					slotArray[i++] = s.getNumber();
				}
				Arrays.sort(slotArray);
				System.out.println(t.toString() + " " + Arrays.toString(slotArray));
				for (i = 1; i < slotArray.length; i++) {
					stat.addValue(Math.abs(slotArray[i - 1] - slotArray[i]));
				}
			}
			System.out.println();
			System.out.println("Team spans range from " + stat.getMin()
					+ " to " + stat.getMax() + ".");
			System.out.println("Average is " + stat.getMean() + ".");
			int[] vals = new int[] { 25, 50, 75, 90, 95, 99 };
			for (int val : vals) {
				System.out.println(val + " % values are between "
						+ stat.getMin() + " and " + stat.getPercentile(val)
						+ ".");
			}
		}

		private void outputCSV(TournamentsSolution sol) throws Exception {
			// determine maximal slot number
			int maxSlotNo = 0;
			for (Slot s : sol.getSlotList()) {
				if (maxSlotNo < s.getNumber()) {
					maxSlotNo = s.getNumber();
				}
			}

			// sort all the matches
			Map<Court, Match[]> matches = new HashMap<>();
			for (Slot s : sol.getSlotList()) {
				Court c = s.getCourt();
				int pos = s.getNumber();
				if (!matches.containsKey(c)) {
					matches.put(c, new Match[maxSlotNo + 1]); // +1 because of zero based index
				}
				matches.get(c)[pos] = s.getMatch();
			}

			File rootDir = new File(System.getProperty("user.dir"), "data/");
			rootDir.mkdirs();
			outputSpecificCSV(matches.entrySet(), new File(rootDir, "ALL.csv"));

			for (Team t : sol.getTeams()) {
				Map<Court, Match[]> teamMatches = new HashMap<>();
				for (Slot s : sol.getSlotList()) {
					if (s.getMatch() instanceof TeamsMatch) {
						if (!((TeamsMatch) s.getMatch()).getTeams().contains(t)) continue;
						Court c = s.getCourt();
						int pos = s.getNumber();
						if (!teamMatches.containsKey(c)) {
							teamMatches.put(c, new TeamsMatch[maxSlotNo + 1]);
						}
						teamMatches.get(c)[pos] = s.getMatch();
					}
				}
				outputSpecificCSV(teamMatches.entrySet(),
						new File(rootDir, t.getName() + ".csv"));
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
		System.out.println("Computing, press Ctrl+C to stop...");
		final Execution e = new Execution();
		final ExecutorService es = Executors.newCachedThreadPool();
		es.execute(e);
		while (e.isRunning()) {
			Thread.sleep(1000);
			e.printScore();
		}
		e.stop();
		es.shutdownNow();
	}

}