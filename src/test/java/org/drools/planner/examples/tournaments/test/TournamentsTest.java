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
import org.drools.planner.core.score.buildin.hardandsoft.HardAndSoftScoreCalculator;
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
		kSession.setGlobal("scoreCalculator", new HardAndSoftScoreCalculator());
		fireAllRules();
		HardAndSoftScoreCalculator score = (HardAndSoftScoreCalculator) kSession.getGlobal("scoreCalculator");
	}

	@SuppressWarnings("unused")
	@Test(enabled = false)
	private void teamOverheadTest() {
		Court cA = new Court(); cA.setName("A"); addFact(cA);
		Court cB = new Court(); cB.setName("B"); addFact(cB);
     	
		Group g = new Group(); g.setName("Z"); addFact(g);
		Team[] teamsZ = new Team[4];
		for (int i = 0; i < 4; i++) {
			teamsZ[i] = new Team();
			teamsZ[i].setName("Z" + i);
			teamsZ[i].setGroup(g);
			addFact(teamsZ[i]);
		}
     	
     	g = new Group(); g.setName("X"); addFact(g);
		Team[] teamsX = new Team[4];
		for (int i = 0; i < 4; i++) {
			teamsX[i] = new Team();
			teamsX[i].setName("X" + i);
			teamsX[i].setGroup(g);
			addFact(teamsX[i]);
		}
		
     	g = new Group(); g.setName("Y"); addFact(g);
		Team[] teamsY = new Team[4];
		for (int i = 0; i < 4; i++) {
			teamsY[i] = new Team();
			teamsY[i].setName("Y" + i);
			teamsY[i].setGroup(g);
			addFact(teamsY[i]);
		}

		g = new Group(); g.setName("U"); addFact(g);
		Team[] teamsU = new Team[4];
		for (int i = 0; i < 4; i++) {
			teamsU[i] = new Team();
			teamsU[i].setName("U" + i);
			teamsU[i].setGroup(g);
			addFact(teamsU[i]);
		}
		
		TeamsMatch mZ1Z2 = new TeamsMatch(Arrays.asList(teamsZ[1], teamsZ[2])); addFact(mZ1Z2);
		TeamsMatch mZ0Z3 = new TeamsMatch(Arrays.asList(teamsZ[0], teamsZ[3])); addFact(mZ0Z3);
		TeamsMatch mY0Y3 = new TeamsMatch(Arrays.asList(teamsY[0], teamsY[3])); addFact(mY0Y3);
		TeamsMatch mU0U2 = new TeamsMatch(Arrays.asList(teamsU[0], teamsU[2])); addFact(mU0U2);
		TeamsMatch mY1Y2 = new TeamsMatch(Arrays.asList(teamsY[1], teamsY[2])); addFact(mY1Y2);
		TeamsMatch mY2Y3 = new TeamsMatch(Arrays.asList(teamsY[2], teamsY[3])); addFact(mY2Y3);
		TeamsMatch mX0X1 = new TeamsMatch(Arrays.asList(teamsX[0], teamsX[1])); addFact(mX0X1);
		TeamsMatch mU0U1 = new TeamsMatch(Arrays.asList(teamsU[0], teamsU[1])); addFact(mU0U1);
		TeamsMatch mX1X3 = new TeamsMatch(Arrays.asList(teamsX[1], teamsX[3])); addFact(mX1X3);
		TeamsMatch mZ0Z2 = new TeamsMatch(Arrays.asList(teamsZ[0], teamsZ[2])); addFact(mZ0Z2);
		TeamsMatch mX1X2 = new TeamsMatch(Arrays.asList(teamsX[1], teamsX[2])); addFact(mX1X2);
		TeamsMatch mX0X3 = new TeamsMatch(Arrays.asList(teamsX[0], teamsX[3])); addFact(mX0X3);
		TeamsMatch mY0Y2 = new TeamsMatch(Arrays.asList(teamsY[0], teamsY[2])); addFact(mY0Y2);
		TeamsMatch mZ0Z1 = new TeamsMatch(Arrays.asList(teamsZ[0], teamsZ[1])); addFact(mZ0Z1);
		TeamsMatch mU1U2 = new TeamsMatch(Arrays.asList(teamsU[1], teamsU[2])); addFact(mU1U2);
		TeamsMatch mU0U3 = new TeamsMatch(Arrays.asList(teamsU[0], teamsU[3])); addFact(mU0U3);
		TeamsMatch mU1U3 = new TeamsMatch(Arrays.asList(teamsU[1], teamsU[3])); addFact(mU1U3);
		TeamsMatch mY0Y1 = new TeamsMatch(Arrays.asList(teamsY[0], teamsY[1])); addFact(mY0Y1);
		TeamsMatch mX0X2 = new TeamsMatch(Arrays.asList(teamsX[0], teamsX[2])); addFact(mX0X2);
		TeamsMatch mY1Y3 = new TeamsMatch(Arrays.asList(teamsY[1], teamsY[3])); addFact(mY1Y3);
		TeamsMatch mU2U3 = new TeamsMatch(Arrays.asList(teamsU[2], teamsU[3])); addFact(mU2U3);
		TeamsMatch mX2X3 = new TeamsMatch(Arrays.asList(teamsX[2], teamsX[3])); addFact(mX2X3);
		TeamsMatch mZ2Z3 = new TeamsMatch(Arrays.asList(teamsZ[2], teamsZ[3])); addFact(mZ2Z3);
		TeamsMatch mZ1Z3 = new TeamsMatch(Arrays.asList(teamsZ[1], teamsZ[3])); addFact(mZ1Z3);

		Slot s;
		s = new Slot(cA, 1); s.setMatch(mX2X3); addFact(s);
		s = new Slot(cA, 2); s.setMatch(mY0Y1); addFact(s);
		s = new Slot(cA, 0); s.setMatch(mX0X1); addFact(s);
		s = new Slot(cA, 3); s.setMatch(mX0X2); addFact(s);
		s = new Slot(cA, 5); s.setMatch(mY0Y2); addFact(s);
		s = new Slot(cA, 6); s.setMatch(mX0X3); addFact(s);
		s = new Slot(cA, 4); s.setMatch(mX1X3); addFact(s);
		
		kSession.setGlobal("scoreCalculator", new HardAndSoftScoreCalculator());
		fireAllRules();
		for (Object o: kSession.getObjects()) {
			System.out.println(o);
		}
		HardAndSoftScoreCalculator score = (HardAndSoftScoreCalculator) kSession.getGlobal("scoreCalculator");
		System.out.printf("-%dhard/-%dsoft", score.getHardConstraintsBroken(), score.getSoftConstraintsBroken());
	}

	private Match addMatch(Team t1, Team t2) {
		TeamsMatch m = new TeamsMatch(Arrays.asList(t1, t2)); addFact(m);
		return m;
	}
	
	@SuppressWarnings("unused")
	@Test(enabled = true)
	private void brokenRuleTest() {
		Court cA = new Court(); cA.setName("A"); addFact(cA);
		Court cB = new Court(); cB.setName("B"); addFact(cB);
     	
		Group g = new Group(); g.setName("Z"); addFact(g);
		Team[] teamsZ = new Team[4];
		for (int i = 0; i < 4; i++) {
			teamsZ[i] = new Team();
			teamsZ[i].setName("Z" + i);
			teamsZ[i].setGroup(g);
			addFact(teamsZ[i]);
		}
     	
     	g = new Group(); g.setName("X"); addFact(g);
		Team[] teamsX = new Team[4];
		for (int i = 0; i < 4; i++) {
			teamsX[i] = new Team();
			teamsX[i].setName("X" + i);
			teamsX[i].setGroup(g);
			addFact(teamsX[i]);
		}
		
     	g = new Group(); g.setName("Y"); addFact(g);
		Team[] teamsY = new Team[4];
		for (int i = 0; i < 4; i++) {
			teamsY[i] = new Team();
			teamsY[i].setName("Y" + i);
			teamsY[i].setGroup(g);
			addFact(teamsY[i]);
		}

		g = new Group(); g.setName("U"); addFact(g);
		Team[] teamsU = new Team[4];
		for (int i = 0; i < 4; i++) {
			teamsU[i] = new Team();
			teamsU[i].setName("U" + i);
			teamsU[i].setGroup(g);
			addFact(teamsU[i]);
		}
		
		addMatch(teamsX[2], teamsX[3]);
		addMatch(teamsX[1], teamsX[3]);
		addMatch(teamsU[1], teamsU[2]);
		addMatch(teamsY[1], teamsY[2]);
		addMatch(teamsU[2], teamsU[3]);
		addMatch(teamsU[0], teamsU[3]);
		addMatch(teamsU[0], teamsU[1]);
		addMatch(teamsZ[1], teamsZ[3]);
		Match mX0X2 = addMatch(teamsX[0], teamsX[2]);
		Match mX0X1 = addMatch(teamsX[0], teamsX[1]);
		addMatch(teamsY[0], teamsY[3]);	
		addMatch(teamsX[1], teamsX[2]);
		addMatch(teamsZ[1], teamsZ[2]);
		addMatch(teamsX[0], teamsX[3]);
		addMatch(teamsZ[2], teamsZ[3]);
		addMatch(teamsY[2], teamsY[3]);
		addMatch(teamsU[1], teamsU[3]);
		addMatch(teamsY[1], teamsY[3]);
		addMatch(teamsZ[0], teamsZ[1]);
		addMatch(teamsU[0], teamsU[2]);
		addMatch(teamsZ[0], teamsZ[2]);
		addMatch(teamsY[0], teamsY[2]);
		addMatch(teamsZ[0], teamsZ[3]);
		addMatch(teamsY[0], teamsY[1]);

		Slot s;
		s = new Slot(cA, 1); s.setMatch(mX0X2); addFact(s);
		s = new Slot(cA, 0); s.setMatch(mX0X1); addFact(s);

		kSession.setGlobal("scoreCalculator", new HardAndSoftScoreCalculator());
		fireAllRules();
		for (Object o: kSession.getObjects()) {
			System.out.println(o);
		}
		HardAndSoftScoreCalculator score = (HardAndSoftScoreCalculator) kSession.getGlobal("scoreCalculator");
		System.out.printf("-%dhard/-%dsoft", score.getHardConstraintsBroken(), score.getSoftConstraintsBroken());
	}
	
	@SuppressWarnings("unused")
	@Test(enabled = false)
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
		kSession.setGlobal("scoreCalculator", new HardAndSoftScoreCalculator());
		fireAllRules();
		for (Object o: kSession.getObjects()) {
			System.out.println(o);
		}
		HardAndSoftScoreCalculator score = (HardAndSoftScoreCalculator) kSession.getGlobal("scoreCalculator");
		System.out.println(score.getSoftConstraintsBroken());
	}
}
