package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.net.Change;
import cec.net.ChangeSetFields;
import cec.net.ChangeSetState;
import cec.net.CommunicationChangeSet;

public class ChangeSetTests {
	
	CommunicationChangeSet ccs;
	UUID meetingID;
	String before;
	String after;

	@Before
	public void setUp() throws Exception {
		meetingID = UUID.randomUUID();
		ccs = new CommunicationChangeSet(ChangeSetState.CHANGE, meetingID);
		// should the "before" be calculated by the system? who should populate it? 
		before = "Before";
		after = "After";
		ccs.addChange(ChangeSetFields.BODY, before, after);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void addedChangeIsInChangeSet() {
		Change c = new Change();
		c.field = ChangeSetFields.BODY;
		c.before = before;
		c.after = after;
		
		ArrayList<Change> changes = ccs.getChanges();
		assertTrue(changes.get(0).isEqualTo(c));
		assertEquals(1, changes.size());	
	}
	
	@Test
	public void addMultipleFieldsToChangeSet() {
		ccs.addChange(ChangeSetFields.ATTENDEES, "bob@hope.com", "jimbo@jimbo.com");
		ccs.addChange(ChangeSetFields.END_DATE, "END_DATE", "jimbo1@jimbo.com");
		ccs.addChange(ChangeSetFields.END_TIME, "END_TIME", "jimbo2@jimbo.com");
		ccs.addChange(ChangeSetFields.START_DATE, "START_DATE", "jimbo3@jimbo.com");
		ccs.addChange(ChangeSetFields.START_TIME, "START_TIME", "jimbo4@jimbo.com");
		ccs.addChange(ChangeSetFields.PLACE, "LOCATION", "jimbo5@jimbo.com");
		ccs.addChange(ChangeSetFields.SUBJECT, "SUBJECT", "jimbo6@jimbo.com");
		
		ArrayList<Change> changes = ccs.getChanges();
		assertEquals(8, changes.size());
		
		Change c = new Change(ChangeSetFields.ATTENDEES, "bob@hope.com", "jimbo@jimbo.com");
		assertTrue(changes.get(1).isEqualTo(c));
		
		c = new Change(ChangeSetFields.END_DATE, "END_DATE", "jimbo1@jimbo.com");
		assertTrue(changes.get(2).isEqualTo(c));

		c = new Change(ChangeSetFields.END_TIME, "END_TIME", "jimbo2@jimbo.com");
		assertTrue(changes.get(3).isEqualTo(c));

		c = new Change(ChangeSetFields.START_DATE, "START_DATE", "jimbo3@jimbo.com");
		assertTrue(changes.get(4).isEqualTo(c));
		
		c = new Change(ChangeSetFields.START_TIME, "START_TIME", "jimbo4@jimbo.com");
		assertTrue(changes.get(5).isEqualTo(c));
		
		c = new Change(ChangeSetFields.PLACE, "LOCATION", "jimbo5@jimbo.com");
		assertTrue(changes.get(6).isEqualTo(c));
		
		c = new Change(ChangeSetFields.SUBJECT, "SUBJECT", "jimbo6@jimbo.com");
		assertTrue(changes.get(7).isEqualTo(c));		
	}
	
	@Test
	public void verifyTheStatesGetters() {
		CommunicationChangeSet acc = new CommunicationChangeSet(ChangeSetState.ACCEPT, meetingID);
		assertTrue(acc.isAccept());
		
		acc = new CommunicationChangeSet(ChangeSetState.CHANGE, meetingID);
		assertTrue(acc.isChange());
		
		acc = new CommunicationChangeSet(ChangeSetState.DECLINE, meetingID);
		assertTrue(acc.isDecline());
		
		acc = new CommunicationChangeSet(ChangeSetState.CHANGE_ACCEPTED, meetingID);
		assertTrue(acc.isChangeAccepted());
		
		acc = new CommunicationChangeSet(ChangeSetState.CHANGE_REJECTED, meetingID);
		assertTrue(acc.isChangeRejected());
	}
	
	@Test
	public void tryToAddChangeWithSameBeforeAndAfter() {
		Change c = new Change();
		c.field = ChangeSetFields.SUBJECT;
		c.before = "blah";
		c.after = "blah";
		ccs.addChange(c);	
		assertTrue(ccs.getChanges().get(1).before.equals("blah"));
		assertTrue(ccs.getChanges().get(1).after.equals("blah"));
	}

	
	@Test(expected=IllegalStateException.class)
	public void tryToAddChangesToTheSameFieldTwice() {
		Change c = new Change();
		c.field = ChangeSetFields.BODY;
		c.before = "blah";
		c.after = "blahblah";
		ccs.addChange(c);		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void tryToAddNullChange() {
		Change c = new Change();
		ccs.addChange(c);
	}
}
