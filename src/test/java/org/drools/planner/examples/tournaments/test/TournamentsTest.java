package org.drools.planner.examples.tournaments.test;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.base.RuleNameEqualsAgendaFilter;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.ObjectInsertedEvent;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.AgendaFilter;
import org.drools.FactHandle;
import org.drools.planner.core.score.calculator.DefaultHardAndSoftConstraintScoreCalculator;
import org.drools.planner.core.score.calculator.HardAndSoftConstraintScoreCalculator;
import org.mockito.ArgumentCaptor;
import org.mvel2.MVEL;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import org.drools.planner.examples.tournaments.model.Court;
import org.drools.planner.examples.tournaments.model.Match;
import org.drools.planner.examples.tournaments.model.Group;
import org.drools.planner.examples.tournaments.model.Slot;
import org.drools.planner.examples.tournaments.model.Team;
import org.drools.planner.examples.tournaments.model.TeamsMatch;

@Test
public class TournamentsTest {

	public static final String RULES = "scoreRules.drl";

	private KnowledgeBuilder kBuilder = null;
	private KnowledgeBase kBase = null;
	private StatefulKnowledgeSession kSession = null;

	protected AgendaEventListener agendaListener = null;
	protected WorkingMemoryEventListener wmListener = null;

	@BeforeTest(alwaysRun = true)
	public void beforeTest() {
		kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kBuilder.add(ResourceFactory.newClassPathResource(RULES),
				ResourceType.DRL);

		if (kBuilder.hasErrors()) {
			for (KnowledgeBuilderError err : kBuilder.getErrors()) {
				System.err.println(err.toString());
			}
			throw new IllegalStateException("DRL errors");
		}
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		kBase = KnowledgeBaseFactory.newKnowledgeBase();
		kBase.addKnowledgePackages(kBuilder.getKnowledgePackages());
		kSession = kBase.newStatefulKnowledgeSession();

		agendaListener = mock(AgendaEventListener.class);
		kSession.addEventListener(agendaListener);

		wmListener = mock(WorkingMemoryEventListener.class);
		kSession.addEventListener(wmListener);
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod() {
		if (kSession != null) {
			kSession.dispose();
			kSession = null;
			agendaListener = null;
			wmListener = null;
		}
	}

	protected void addFact(Object fact) {
		kSession.insert(fact);
	}

	protected int fireAllRules() {
		return kSession.fireAllRules();
	}

	protected int fireAllRules(int limit) {
		return kSession.fireAllRules(limit);
	}

	protected int fireAllRules(AgendaFilter filter) {
		return kSession.fireAllRules(filter);
	}

	protected int fireAllRules(AgendaFilter filter, int limit) {
		return kSession.fireAllRules(filter, limit);
	}

	@SuppressWarnings("unused")
	@Test(enabled = false)
	private void accumulateTest() {
		Court c = new Court(); c.setName("A");
     	addFact(c);
     	Group x = new Group(); x.setName("X"); addFact(x);
     	Group y = new Group(); y.setName("Y"); addFact(y);
		Team[] teams = new Team[10];
		for (int i = 0; i < 5; i++) {
			teams[i] = new Team();
			teams[i].setName("X" + i);
			teams[i].setGroup(x);
			addFact(teams[i]);
		}
		for (int i = 0; i < 5; i++) {
			teams[i + 5] = new Team();
			teams[i + 5].setName("Y" + i);
			teams[i + 5].setGroup(y);
			addFact(teams[i + 5]);
		}
		TeamsMatch[] matches = new TeamsMatch[20];
		int cnt = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = i + 1; j < 5; j++) {
				matches[cnt] = new TeamsMatch(Arrays.asList(teams[i], teams[j]));
				addFact(matches[cnt++]);
			}
		}
		for (int i = 0; i < 5; i++) {
			for (int j = i + 1; j < 5; j++) {
				matches[cnt] = new TeamsMatch(Arrays.asList(teams[i + 5], teams[j + 5]));
				addFact(matches[cnt++]);
			}
		}
		Slot s;
		s = new Slot(c, 0); s.setMatch(matches[0]); addFact(s);
		s = new Slot(c, 1); s.setMatch(matches[8]); addFact(s);
		s = new Slot(c, 2); s.setMatch(matches[8]); addFact(s);
		Slot sa = new Slot(c, 2); sa.setMatch(matches[10]); addFact(sa);
		Slot sb = new Slot(c, 2); sb.setMatch(matches[10]); addFact(sb);
		Assert.assertEquals(sa, sb);
		s = new Slot(c, 3); s.setMatch(matches[1]); addFact(s);
		s = new Slot(c, 4); s.setMatch(matches[5]); addFact(s);
		s = new Slot(c, 5); s.setMatch(matches[11]); addFact(s);
		s = new Slot(c, 5); s.setMatch(matches[11]); addFact(s);
		s = new Slot(c, 6); s.setMatch(matches[2]); addFact(s);
		s = new Slot(c, 7); s.setMatch(matches[14]); addFact(s);
	
		
/*		Slot [Court A, 0, match=Match [teamsInMatch=[Team X0, Team X1]]], 
		Slot [Court A, 1, match=Match [teamsInMatch=[Team X2, Team X3]]], 
		Slot [Court A, 2, match=Match [teamsInMatch=[Team Y0, Team Y1]]], 
		Slot [Court A, 2, match=Match [teamsInMatch=[Team Y0, Team Y1]]], 
		Slot [Court A, 3, match=Match [teamsInMatch=[Team X0, Team X2]]], 
		Slot [Court A, 4, match=Match [teamsInMatch=[Team X1, Team X3]]], 
		Slot [Court A, 5, match=Match [teamsInMatch=[Team Y0, Team Y2]]], 
		Slot [Court A, 5, match=Match [teamsInMatch=[Team Y0, Team Y2]]], 
		Slot [Court A, 6, match=Match [teamsInMatch=[Team X0, Team X3]]], 
		Slot [Court A, 7, match=Match [teamsInMatch=[Team Y1, Team Y2]]]] Team Y0 0 7 10 0
	*/	
		kSession.setGlobal("scoreCalculator", new DefaultHardAndSoftConstraintScoreCalculator());
		fireAllRules();
		HardAndSoftConstraintScoreCalculator score = (HardAndSoftConstraintScoreCalculator) kSession.getGlobal("scoreCalculator");
	}

	@SuppressWarnings("unused")
	@Test(enabled = true)
	private void softConstraintTest() {
		Court c = new Court(); c.setName("A");
     	addFact(c);
     	Group x = new Group(); x.setName("X"); addFact(x);
		Team[] teams = new Team[5];
		for (int i = 0; i < 5; i++) {
			teams[i] = new Team();
			teams[i].setName("X" + i);
			teams[i].setGroup(x);
			addFact(teams[i]);
		}
		TeamsMatch[] matches = new TeamsMatch[10];
		int cnt = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = i + 1; j < 5; j++) {
				matches[cnt] = new TeamsMatch(Arrays.asList(teams[i], teams[j]));
				addFact(matches[cnt++]);
			}
		}
		Slot s;
		s = new Slot(c, 0); s.setMatch(matches[0]); addFact(s);
		s = new Slot(c, 2); s.setMatch(matches[1]); addFact(s);
		s = new Slot(c, 4); s.setMatch(matches[2]); addFact(s);
		kSession.setGlobal("scoreCalculator", new DefaultHardAndSoftConstraintScoreCalculator());
		fireAllRules();
		HardAndSoftConstraintScoreCalculator score = (HardAndSoftConstraintScoreCalculator) kSession.getGlobal("scoreCalculator");
		System.out.println(score.getSoftConstraintsBroken());
	}
}
