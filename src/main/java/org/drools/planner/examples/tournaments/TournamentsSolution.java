package org.drools.planner.examples.tournaments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.drools.planner.api.domain.solution.PlanningEntityCollectionProperty;
import org.drools.planner.core.score.buildin.hardandsoft.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Group;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.PauseMatch;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.planner.examples.tournaments.model.Team;
import org.drools.planner.examples.tournaments.model.TeamsMatch;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("tournament")
public class TournamentsSolution implements Solution<HardAndSoftScore> {

	@XStreamImplicit(itemFieldName = "group")
	List<Group> groups = new LinkedList<>();
	@XStreamImplicit(itemFieldName = "court")
	List<Court> courts = new LinkedList<>();
	@XStreamImplicit(itemFieldName = "team")
	List<Team> teams = new LinkedList<>();
	@XStreamImplicit(itemFieldName = "match")
	private List<Match> matchList = new LinkedList<>();

	@XStreamImplicit(itemFieldName = "slot")
	// @XStreamOmitField
	private List<Slot> slotList = new LinkedList<>();
	@XStreamOmitField
	private HardAndSoftScore score;

	private static final double MAX_SLOT_OVERFLOW_RATE = 0;//0.04;

	@PlanningEntityCollectionProperty
	public List<Slot> getSlotList() {
		if (slotList == null || slotList.size() == 0) {
			slotList = generateSlots();
		}
		return slotList;
	}
	
	private static int getSlotsPerCourt(int matchesCount, int courtsCount) {
		double idealMatchesPerCourt = Math.ceil((double) matchesCount / courtsCount);
		double allowedMatchesPerCourt = idealMatchesPerCourt * (1 + MAX_SLOT_OVERFLOW_RATE);
		int result = (int) Math.round(allowedMatchesPerCourt);
		
		return result;
	}
	
	public List<Slot> generateSlots() {
		List<Slot> slotList = new LinkedList<>();
		// initialize list of slots
		int upperBound = getSlotsPerCourt(getRealMatchCount(), getCourts().size());
		int lowerBound = 0;
		for (Court c : this.getCourts()) {
			for (int i = lowerBound; i < upperBound; i++) {
				slotList.add(new Slot(c, i));
			}
		}

		return Collections.unmodifiableList(slotList);
	}

	public Solution<HardAndSoftScore> cloneSolution() {
		TournamentsSolution s = new TournamentsSolution();
		s.groups = groups;
		s.teams = teams;
		s.courts = courts;
		s.matchList = matchList;
		for (Slot sl : slotList) {
			s.slotList.add(sl.clone());
		}
		s.setScore(getScore());
		return s;
	}

	public Collection<? extends Object> getProblemFacts() {
		List<Object> l = new ArrayList<Object>();
		l.addAll(getTeams());
		l.addAll(getMatchList());
		l.addAll(getCourts());
		l.addAll(getGroups());

		return l;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TournamentsSolution [groups=");
		builder.append(groups);
		builder.append(", courts=");
		builder.append(courts);
		builder.append(", teams=");
		builder.append(teams);
		builder.append(", score=");
		builder.append(score);
		builder.append("]");
		return builder.toString();
	}

	public HardAndSoftScore getScore() {
		return score;
	}

	public void setScore(HardAndSoftScore arg0) {
		score = arg0;
	}

	public Collection<Team> getTeams() {
		return Collections.unmodifiableCollection(this.teams);
	}

	public Collection<Court> getCourts() {
		return Collections.unmodifiableCollection(this.courts);
	}

	public Collection<Group> getGroups() {
		return Collections.unmodifiableCollection(this.groups);
	}

	// Suppose any order of Teams and generate matches like following:
	// X vs. Y for all X, Y from Teams where X.Group == Y.Group and X != Y and X
	// < Y
	private List<Match> generateMatches() {
		List<Match> matches = new LinkedList<>();
		Team[] teams = this.teams.toArray(new Team[this.teams.size()]);

		for (int i = 0; i < teams.length; i++) {
			for (int j = i + 1; j < teams.length; j++) {
				Team teamA = teams[i];
				Team teamB = teams[j];
				if (teamA.getGroup().equals(teamB.getGroup())) {
					List<Team> teamsInMatch = new LinkedList<>();
					teamsInMatch.add(teamA);
					teamsInMatch.add(teamB);
					TeamsMatch m = new TeamsMatch(teamsInMatch);
					matches.add(m);
				}
			}
		}
		
		int slots = getSlotsPerCourt(matches.size(), getCourts().size()) * getCourts().size();
		if (slots > matches.size()) {
			for (int i = 0, matchesSize = matches.size(); i < slots - matchesSize; i++) {
				matches.add(new PauseMatch());
			}
		}

		return Collections.unmodifiableList(matches);
	}

	public Collection<Match> getMatchList() {
		if (matchList == null || matchList.size() == 0) {
			matchList = generateMatches();
		}
		return this.matchList;
	}
	
	private int getRealMatchCount() {
		Collection<Match> matches = getMatchList();
		int count = 0;
		for (Match m: matches) {
			if (m instanceof TeamsMatch) {
				count++;
			}
		}
		return count;
	}

	public void setMatchList(List<Match> matches) {
		this.matchList = matches;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((matchList == null) ? 0 : matchList.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TournamentsSolution)) {
			return false;
		}
		TournamentsSolution other = (TournamentsSolution) obj;
		if (score == null) {
			if (other.score != null) {
				return false;
			}
		} else if (!score.equals(other.score)) {
			return false;
		}
		if (slotList == null) {
			if (other.slotList != null) {
				return false;
			}
		} else if (!slotList.equals(other.slotList)) {
			return false;
		}
		return true;
	}

}
