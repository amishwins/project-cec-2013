package tests;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.config.CECConfigurator;
import cec.model.Folder;
import cec.model.FolderFactory;
import cec.model.Meeting;
import cec.model.MeetingBuilder;

public class MeetingBuilderTests {
	
	MeetingBuilder cut;
	Folder myFolder;
	Meeting meeting;
	Map<String, String> fields;
	
	UUID meetingId;
	String from;
	String attendees;
	String startDate;
	String endDate;
	String startTime;
	String endTime;
	String place;
	String subject;
	String body;
    String sentTime;
    String lastModifiedTime;
    String systemFolderName = "Meetings";
	
	@Before
	public void setUp() throws Exception {
		systemFolderName = "Meetings";
		meetingId = UUID.randomUUID();
		from = CECConfigurator.getReference()
				.getClientEmailAddress();
		attendees = "Pankaj@yahoo.com;amishgala@gmail.com";
		startDate = "12-31-2014";
		endDate = "12-31-2014";
		startTime = "12:00";
		endTime = "23:00";
		place = "H-843 SGW Campus";
		subject = "Project Deliverble 2";
		body = "Lets finish Deliverable 2!";
		sentTime = "2013.05.19_At_05.08.28.457";
		lastModifiedTime = "2013.05.19_At_05.08.28.457";
		
		cut = new MeetingBuilder();
		myFolder = FolderFactory.getFolder(systemFolderName);
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void buildSimpleEmail() {
		meeting = cut.withId(meetingId)
				.withFrom(from)
				.withAttendees(attendees)
				.withStartDate(startDate)
				.withEndDate(endDate)
				.withStartTime(startTime)
				.withEndTime(endTime)
				.withPlace(place)
				.withSubject(subject)
				.withBody(body)
				.withLastModifiedTime(lastModifiedTime)
				.withSentTime(sentTime)
				.withParentFolder(myFolder)
			    .build();
		
     	assertTrue(meeting.getId().equals(meetingId));
		assertTrue(meeting.getFrom().equals("cec.user@cec.com"));
		assertTrue(meeting.getAttendees().equals(attendees));
		assertTrue(meeting.getStartDate().equals(startDate));
		assertTrue(meeting.getEndDate().equals(endDate));
		assertTrue(meeting.getStartTime().equals(startTime));
		assertTrue(meeting.getEndTime().equals(endTime));
		assertTrue(meeting.getPlace().equals(place));
		assertTrue(meeting.getSubject().equals(subject));
		assertTrue(meeting.getBody().equals(body));
		assertTrue(meeting.getLastModifiedTime().equals(lastModifiedTime));
		assertTrue(meeting.getSentTime().equals(sentTime));
		assertEquals(meeting.getParentFolder(), myFolder);
	}
	
	@Test
	public void verifyComputedFields() {
		meeting = cut.computeID()
			.computelastModifiedTime()
			.computeSentTime()
			.build();
		
		// Not really great tests. Need to think about this a bit more
		assertNotSame(UUID.randomUUID(), meeting.getId());
		assertNotSame(new Date(), meeting.getLastModifiedTime());
		assertNotSame(new Date(), meeting.getSentTime());				
	}
	
	@Test(expected=RuntimeException.class)
	public void verifyLoadWithEmptyHashMap() {
		fields = new HashMap<String, String>();
		cut = cut.load(fields);
	}
	
	@Test
	public void verifyLoadWithSomeFields() {
		fields = new HashMap<String, String>();
		fields.put("Id", UUID.randomUUID().toString());
		cut = cut.load(fields);
		meeting = cut.build();
		assertNotNull(meeting);
	}
	
}
