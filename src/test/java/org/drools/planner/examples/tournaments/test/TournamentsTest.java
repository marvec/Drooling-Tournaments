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
        kBuilder.add(ResourceFactory.newClassPathResource(RULES), ResourceType.DRL);

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

    protected void updateFact(Object fact) {
        kSession.update(kSession.getFactHandle(fact), fact);
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

    private Match addMatch(Team t1, Team t2) {
        TeamsMatch m = new TeamsMatch(Arrays.asList(t1, t2));
        addFact(m);
        return m;
    }

    @SuppressWarnings("unused")
    @Test(enabled = true)
    private void brokenRuleTest() {
        Court cA = new Court();
        cA.setName("A");
        addFact(cA);

        Group g = new Group();
        g.setName("X");
        addFact(g);
        Team[] teamsX = new Team[4];
        for (int i = 0; i < 4; i++) {
            teamsX[i] = new Team();
            teamsX[i].setName("X" + i);
            teamsX[i].setGroup(g);
            addFact(teamsX[i]);
        }

        Match mX0X3 = addMatch(teamsX[0], teamsX[3]);
        Match mX1X2 = addMatch(teamsX[1], teamsX[2]);
        Match mX0X1 = addMatch(teamsX[0], teamsX[1]);
        Match mX0X2 = addMatch(teamsX[0], teamsX[2]);

        Slot s;
        s = new Slot(cA, 0);
        s.setMatch(mX0X1);
        addFact(s);

        kSession.setGlobal("scoreCalculator", new HardAndSoftScoreCalculator());
        HardAndSoftScoreCalculator score = (HardAndSoftScoreCalculator) kSession.getGlobal("scoreCalculator");

        System.out.println("= Run no. 1 ============================================");
        fireAllRules();

        for (Object o : kSession.getObjects()) {
            System.out.println("1) " + o);
        }
        System.out.printf("1) -%dhard/-%dsoft%n%n", score.getHardConstraintsBroken(), score.getSoftConstraintsBroken());

        System.out.println("= Run no. 2 ============================================");
        s.setMatch(mX0X2);
        updateFact(s);

        fireAllRules();
        for (Object o : kSession.getObjects()) {
            System.out.println("2) " + o);
        }
        System.out.printf("2) -%dhard/-%dsoft%n%n", score.getHardConstraintsBroken(), score.getSoftConstraintsBroken());

        System.out.println("= Run no. 3 ============================================");
        s.setMatch(mX0X3);
        updateFact(s);

        fireAllRules();
        for (Object o : kSession.getObjects()) {
            System.out.println("3) " + o);
        }
        System.out.printf("3) -%dhard/-%dsoft%n%n", score.getHardConstraintsBroken(), score.getSoftConstraintsBroken());

        System.out.println("= Run no. 4 ============================================");
        s.setMatch(mX1X2);
        updateFact(s);

        fireAllRules();
        for (Object o : kSession.getObjects()) {
            System.out.println("4) " + o);
        }
        System.out.printf("4) -%dhard/-%dsoft%n%n", score.getHardConstraintsBroken(), score.getSoftConstraintsBroken());
    }

}
