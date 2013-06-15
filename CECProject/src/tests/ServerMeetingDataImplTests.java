package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Folder;
import cec.model.Meeting;
import cec.model.ServerMeetingData;
import cec.model.ServerMeetingDataImpl;

public class ServerMeetingDataImplTests {
	
	ServerMeetingData sm;
	MeetingStub fakeMeeting = new MeetingStub();

	@Before
	public void setUp() throws Exception {
		sm = new ServerMeetingDataImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void twoAttendeesInvited() {
		fakeMeeting.setFrom("a@b.com");
		fakeMeeting.setAttendees("mike@jordan.com; bob@hope.com");
		sm.setup(fakeMeeting);
		
		ArrayList<String> invited = (ArrayList<String>) sm.getUnAnsweredRecipients();
		assertEquals(2, invited.size());
	}
	
	@Test
	public void twoAttendeesInvitedOneAccepts() {
		fakeMeeting.setFrom("a@b.com");
		fakeMeeting.setAttendees("mike@jordan.com; bob@hope.com");
		sm.setup(fakeMeeting);
		sm.setAccepted("bob@hope.com");
		
		ArrayList<String> invited = (ArrayList<String>) sm.getUnAnsweredRecipients();
		assertEquals(1, invited.size());
		assertTrue("mike@jordan.com".equals(invited.get(0)));
		
		ArrayList<String> accepted = (ArrayList<String>) sm.getAcceptedRecipients();
		assertEquals(2, accepted.size());
		assertTrue("a@b.com".equals(accepted.get(0)));
		assertTrue("bob@hope.com".equals(accepted.get(1)));
	}
	
	@Test
	public void twoAttendeesInvitedOneAcceptsOneDeclines() {
		fakeMeeting.setFrom("a@b.com");
		fakeMeeting.setAttendees("mike@jordan.com; bob@hope.com");
		sm.setup(fakeMeeting);
		sm.setAccepted("bob@hope.com");
		sm.setDeclined("mike@jordan.com");
		
		ArrayList<String> invited = (ArrayList<String>) sm.getUnAnsweredRecipients();
		assertEquals(0, invited.size());
		
		ArrayList<String> accepted = (ArrayList<String>) sm.getAcceptedRecipients();
		assertEquals(2, accepted.size());
		assertTrue("a@b.com".equals(accepted.get(0)));
		assertTrue("bob@hope.com".equals(accepted.get(1)));
		
		ArrayList<String> declined = (ArrayList<String>) sm.getDeclinedRecipients();
		assertEquals(1, declined.size());
		assertTrue("mike@jordan.com".equals(declined.get(0)));
	}
	
	@Test(expected=RuntimeException.class)
	public void attendeeWhoDoesntExistTriesToAccept() {
		fakeMeeting.setFrom("a@b.com");
		fakeMeeting.setAttendees("bob@hope.com");
		sm.setup(fakeMeeting);
		sm.setAccepted("fake@guy.com");
	}	
	
	@Test(expected=RuntimeException.class)
	public void poorlyFormattedAttendees() {
		fakeMeeting.setFrom("a@b.com");
		fakeMeeting.setAttendees("a@@b.com; a@c.com");
		sm.setup(fakeMeeting);
	}
}

class MeetingStub implements Meeting {
	
	private String attendees;
	private String from;
	public void setAttendees(String s) {
		attendees = s;
	}
	
	public void setFrom(String s) {
		from = s;
	}
	
	@Override
	public String getFrom() {
		return from;
	}

	@Override
	public String getAttendees() {
		return attendees;
	}	

	@Override
	public int compareTo(Meeting arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UUID getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStartTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEndTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStartDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEndDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSentTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLastModifiedTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Folder getParentFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLastModifiedTimeNicelyFormatted() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void send() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub		
	}	
}