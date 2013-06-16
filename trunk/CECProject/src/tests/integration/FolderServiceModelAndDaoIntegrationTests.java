package tests.integration;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.config.CECConfigurator;
import cec.exceptions.CannotDeleteSystemFolderException;
import cec.exceptions.FolderAlreadyExistsException;
import cec.exceptions.RootFolderSubfolderCreationException;
import cec.persistence.MeetingDao;
import cec.persistence.MeetingDaoFactory;
import cec.service.FolderService;
import cec.service.MeetingService;
import cec.view.MeetingViewEntity;

public class FolderServiceModelAndDaoIntegrationTests {
	String rootFolder = "emails";
	String meetingSystemFolder = "Meetings";
	String inboxFolder = "Inbox";
	String systemFolderName = "emails/Inbox";
	String userFolder = "integrationtests";
	String completePath = systemFolderName+"/"+userFolder;
	FolderService folderService;
	
	@Before
	public void setUp() throws Exception {
		folderService = new FolderService();
	}

	@After
	public void tearDown() throws Exception {
		folderService.delete(completePath);
	}

	@Test
	public void createAndDeleteFolder() {
		folderService.delete(completePath);
		folderService.createSubFolder(systemFolderName,userFolder);
		List<String> folder = (List<String>)folderService.loadHierarchy();
		assertTrue(folder.contains(completePath));
		folderService.delete(completePath);
	}
	
	@Test (expected=FolderAlreadyExistsException.class)
	public void ShouldThrowWhenFolderAlreadyExists() {
		folderService.delete(completePath);
		folderService.createSubFolder(systemFolderName,userFolder);
		folderService.createSubFolder(systemFolderName,userFolder);
		folderService.delete(completePath);
	}
	
	@Test (expected=RootFolderSubfolderCreationException.class)
	public void ShouldThrowWhenUserTriesToCreateSystemFolder() {
		folderService.createSubFolder(rootFolder,inboxFolder);		
	}
	
	@Test (expected=CannotDeleteSystemFolderException.class)
	public void ShouldThrowWhenUserTriesToDeleteEmailSystemFolder() {
		folderService.delete(rootFolder);		
	}
	
	@Test (expected=CannotDeleteSystemFolderException.class)
	public void ShouldThrowWhenUserTriesToDeleteMeetingSystemFolder() {
		folderService.delete(meetingSystemFolder);		
	}
	
	@Test
	public void sendAndLoadMeetingsFromMeetingsFolder() {
		HelperFolderServiceModelAndDaoIntegrationTest th = new HelperFolderServiceModelAndDaoIntegrationTest();
		th.setUpTestData();
		th.sendMeetings();
		th.loadMeetings();
		th.deleteMeetings();
	}
}

class HelperFolderServiceModelAndDaoIntegrationTest{
	  String MEETING_FILE_EXTENSION = ".xml";
	  String systemFolderName;
	  
	  MeetingService meetingService;
	  FolderService folderService;
	  MeetingViewEntity meetingViewEntity,meetingViewEntity2;
	  MeetingDao meetingDao;	
	  
	  UUID meetingId, meetingId2;
	  String from, from2;
	  String attendees,attendees2;
	  String startDate,startDate2;
	  String endDate,endDate2;
	  String startTime,startTime2;
	  String endTime,endTime2;
	  String place,place2;
	  String subject,subject2;
	  String body,body2;
      String sentTime,sentTime2;
      String lastModifiedTime,lastModifiedTime2;
      String parentFolder,parentFolder2;

      public void setUpTestData() {
	  
    	    systemFolderName = "Meetings";
    	    
    	    meetingService = new MeetingService();
    		folderService = new FolderService();
    		meetingDao = MeetingDaoFactory.getMeetingDaoInstance();
    		
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
    		
    		meetingId2 = UUID.randomUUID();
    		from2 = CECConfigurator.getReference()
    				.getClientEmailAddress();
    		attendees2 = "Pankaj@yahoo.com;amishgala@gmail.com";
    		startDate2 = "12-31-2014";
    		endDate2 = "12-31-2014";
    		startTime2 = "12:00";
    		endTime2 = "23:00";
    		place2 = "H-843 SGW Campus";
    		subject2 = "Project Deliverble 2";
    		body2 = "Lets finish Deliverable 2!";
    		sentTime2 = "2013.05.19_At_05.08.28.457";
    		lastModifiedTime2 = "2013.05.19_At_05.08.28.457";
    		parentFolder2 = systemFolderName;
    		
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
    		
    		meetingViewEntity2 = new MeetingViewEntity();
    		meetingViewEntity2.setId(meetingId2);
    		meetingViewEntity2.setFrom(from2);
    		meetingViewEntity2.setAttendees(attendees2);
    		meetingViewEntity2.setStartDate(startDate2);
    		meetingViewEntity2.setEndDate(endDate2);
    		meetingViewEntity2.setStartTime(startTime2);
    		meetingViewEntity2.setEndTime(endTime2);
    		meetingViewEntity2.setPlace(place2);
    		meetingViewEntity2.setSubject(subject2);
    		meetingViewEntity2.setBody(body2);
    		meetingViewEntity2.setSentTime(sentTime2);
    		meetingViewEntity2.setLastModifiedTime(lastModifiedTime2);
    		meetingViewEntity2.setFolder(parentFolder2);
	}
      
      public void sendMeetings() {
    	  meetingService.sendMeeting(meetingViewEntity);
    	  meetingService.sendMeeting(meetingViewEntity2);
      }
      
      public void loadMeetings() {
    	  MeetingViewEntity matchedMeetingViewEntity = null,matchedMeetingViewEntity2 = null;
    	  
    	  Iterable<MeetingViewEntity> meetings = folderService.loadMeetings(systemFolderName);
    	  Iterator<MeetingViewEntity> itr = meetings.iterator();
    	  while(itr.hasNext()){
    		  MeetingViewEntity  meetingViewEntityReturned = itr.next();
    		  if(meetingViewEntityReturned.getId().compareTo(meetingViewEntity.getId())==0){
    			 matchedMeetingViewEntity = meetingViewEntityReturned;
    		  }
    		  if(meetingViewEntityReturned.getId().compareTo(meetingViewEntity2.getId())==0){
    			 matchedMeetingViewEntity2 = meetingViewEntityReturned;
    		  }
    	  }
        assertEquals(matchedMeetingViewEntity.getId(),meetingId);
  		assertEquals(matchedMeetingViewEntity.getFrom(),from);
  		assertEquals(matchedMeetingViewEntity.getAttendees(),attendees);
  		assertEquals(matchedMeetingViewEntity.getStartDate(),startDate);
  		assertEquals(matchedMeetingViewEntity.getEndDate(),endDate);
  		assertEquals(matchedMeetingViewEntity.getStartTime(),startTime);
  		assertEquals(matchedMeetingViewEntity.getEndTime(),endTime);
  		assertEquals(matchedMeetingViewEntity.getPlace(),place);
  		assertEquals(matchedMeetingViewEntity.getSubject(),subject);
  		assertEquals(matchedMeetingViewEntity.getBody(),body);
  		assertEquals(matchedMeetingViewEntity.getFolder(),parentFolder);
  		
  		assertEquals(matchedMeetingViewEntity2.getId(),meetingId2);
   		assertEquals(matchedMeetingViewEntity2.getFrom(),from2);
   		assertEquals(matchedMeetingViewEntity2.getAttendees(),attendees2);
   		assertEquals(matchedMeetingViewEntity2.getStartDate(),startDate2);
   		assertEquals(matchedMeetingViewEntity2.getEndDate(),endDate2);
   		assertEquals(matchedMeetingViewEntity2.getStartTime(),startTime2);
   		assertEquals(matchedMeetingViewEntity2.getEndTime(),endTime2);
   		assertEquals(matchedMeetingViewEntity2.getPlace(),place2);
   		assertEquals(matchedMeetingViewEntity2.getSubject(),subject2);
   		assertEquals(matchedMeetingViewEntity2.getBody(),body2);
   		assertEquals(matchedMeetingViewEntity2.getFolder(),parentFolder2);
      }
      
      public void deleteMeetings() {
    	  meetingService.delete(meetingViewEntity);
    	  meetingService.delete(meetingViewEntity2);
    	  meetingDao.delete(CECConfigurator.getReference().get("Sent"),meetingId);
    	  meetingDao.delete(CECConfigurator.getReference().get("Sent"),meetingId2);
    	  meetingDao.delete(CECConfigurator.getReference().get("Outbox"), meetingId);
  		  meetingDao.delete(CECConfigurator.getReference().get("Outbox"), meetingId2);
      } 
}
