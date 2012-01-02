package org.drools.planner.examples.tournaments.helper;

import org.drools.impl.StatefulKnowledgeSessionImpl;
import org.jboss.byteman.rule.Rule;
import org.jboss.byteman.rule.helper.Helper;

public class BytemanHelper extends Helper {

	protected BytemanHelper(Rule rule) {
		super(rule);
	}

	public void printWorkingMemory(Object obj) {
		StatefulKnowledgeSessionImpl session = (StatefulKnowledgeSessionImpl) obj;
		debug("vvvvvvvvvvvvvvvvvvvvvvvvvvv");
		debug(session.toString());
		for (Object o: session.getObjects()) {
			debug(o.toString());
		}
		debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^");
	}
	
}
