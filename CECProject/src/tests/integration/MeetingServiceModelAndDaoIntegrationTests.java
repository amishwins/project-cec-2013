package tests.integration;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.config.CECConfigurator;
import cec.persistence.MeetingDao;
import cec.persistence.MeetingDaoFactory;
import cec.service.MeetingService;
import cec.view.MeetingViewEntity;

public class MeetingServiceModelAndDaoIntegrationTests {
	
    String MEETING_FILE_EXTENSION = ".xml";
    
    MeetingService meetingService;
   
	MeetingDao meetingDao;
	MeetingViewEntity meetingViewEntity;
		
	String systemFolderName;
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
    String parentFolder;
	
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
		parentFolder = systemFolderName;
		
		meetingViewEntity = new MeetingViewEntity();
		meetingViewEntity.setId(meetingId);
		meetingViewEntity.setFrom(from);
		meetingViewEntity.setAttendees(attendees);
		meetingViewEntity.setStartDate(startDate);
		meetingViewEntity.setEndDate(endDate);
		meetingViewEntity.setStartTime(startTime);
		meetingViewEntity.setEndTime(endTime);
		meetingViewEntity.setPlace(place);
		meetingViewEntity.setSubject(subject);
		meetingViewEntity.setBody(body);
		meetingViewEntity.setSentTime(sentTime);
		meetingViewEntity.setLastModifiedTime(lastModifiedTime);
		meetingViewEntity.setFolder(parentFolder);

		meetingService = new MeetingService();
		meetingDao = MeetingDaoFactory.getMeetingDaoInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void sendLoadAndDeleteMeeting() {
		meetingService.sendMeeting(meetingViewEntity);
		Map<String,String> meetingData = meetingDao.loadMeeting(systemFolderName, meetingId.toString()+MEETING_FILE_EXTENSION);
		assertEquals(UUID.fromString(meetingData.get("Id")),meetingId);
		assertEquals(meetingData.get("From"),from);
		assertEquals(meetingData.get("Attendees"),attendees);
		assertEquals(meetingData.get("MeetingStartDate"),startDate);
		assertEquals(meetingData.get("MeetingEndDate"),endDate);
		assertEquals(meetingData.get("MeetingStartTime"),startTime);
		assertEquals(meetingData.get("MeetingEndTime"),endTime);
		assertEquals(meetingData.get("MeetingPlace"),place);
		assertEquals(meetingData.get("Subject"),subject);
		assertEquals(meetingData.get("Body"),body);
		assertEquals(meetingData.get("ParentFolder"),parentFolder);
		meetingService.delete(meetingViewEntity);
		meetingDao.delete(CECConfigurator.getReference().get("Sent"),meetingId);
		meetingDao.delete(CECConfigurator.getReference().get("Outbox"), meetingId);
	}

}
