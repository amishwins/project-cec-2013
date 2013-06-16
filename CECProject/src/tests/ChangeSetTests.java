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

	@Before
	public void setUp() throws Exception {
		meetingID = UUID.randomUUID();
		ccs = new CommunicationChangeSet(ChangeSetState.CHANGE, meetingID);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void addedChangeIsInChangeSet() {
		
		// should the "before" be calculated by the system? who should populate it? 
		ccs.addChange(ChangeSetFields.BODY, "Before", "After");
		
		Change c = new Change();
		c.field = ChangeSetFields.BODY;
		c.before = "Before";
		c.after = "After";
		
		ArrayList<Change> changes = ccs.getChanges();
		assertTrue(changes.get(0).isEqualTo(c));		
	}

}
