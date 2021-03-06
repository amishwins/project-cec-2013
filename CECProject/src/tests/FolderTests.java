package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.exceptions.CannotDeleteSystemFolderException;
import cec.exceptions.RootFolderSubfolderCreationException;
import cec.model.EmailsSystemFolder;
import cec.model.EmailsUserFolder;
import cec.model.MeetingsFolder;
import cec.persistence.FolderDao;

public class FolderTests {
	
	SystemFolderCUT systemFolder;
	UserFolderCUT userFolder;
	MeetingsFolderCUT meetingFolder;
	String userFolderPath;
	String systemFolderPath;
	String newFolderName;
	String systemFolderName;
	String userFolderName;
	String meetingFolderPath;
	

	@Before
	public void setUp() throws Exception {
		
		userFolderPath = "emails/Inbox/Jokes";
		userFolderName = "Jokes";
		systemFolderPath = "emails/Outbox";
		systemFolderName = "Outbox";
		meetingFolderPath = "Meetings";
		
		systemFolder = new SystemFolderCUT(systemFolderPath);
		userFolder = new UserFolderCUT(userFolderPath);
		meetingFolder = new MeetingsFolderCUT(meetingFolderPath);
		newFolderName = "temp";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test 
	public void FolderToGetFields() {
		assertTrue(systemFolder.getPath().equals(systemFolderPath));
		assertTrue(userFolder.getPath().equals(userFolderPath));
	}
	
	@Test 
	public void loadEmailsFromSystemFolder() {
		systemFolder.loadEmails();
		assertTrue(((FolderDaoStub)systemFolder.getFolderDao()).loadEmailsWasCalled);
	}
	
	@Test 
	public void loadEmailsFromUserFolder() {
		userFolder.loadEmails();
		assertTrue(((FolderDaoStub)userFolder.getFolderDao()).loadEmailsWasCalled);
	}

	@Test 
	public void deleteUserFolder() {
		userFolder.delete();
		assertTrue(((FolderDaoStub)userFolder.getFolderDao()).deleteWasCalled);
	}
	
	@Test 
	public void createFolderInsideSystemFolder() {
		systemFolder.createSubFolder(newFolderName);
		assertTrue(((FolderDaoStub)systemFolder.getFolderDao()).createWasCalled);
	}

	@Test 
	public void createFolderInsideUserFolder() {
		userFolder.createSubFolder(newFolderName);
		assertTrue(((FolderDaoStub)userFolder.getFolderDao()).createWasCalled);
	}
	
	@Test
	(expected = UnsupportedOperationException.class)
	public void shouldThrowExceptionWhenTryToLoadMeetingsFromUserEmailsFolders(){
		userFolder.loadMeetings();
	}
	
	@Test
	(expected = UnsupportedOperationException.class)
	public void shouldThrowExceptionWhenTryToLoadMeetingsFromSystemEmailsFolders(){
		systemFolder.loadMeetings();
	}
	
	@Test
	(expected = CannotDeleteSystemFolderException.class)
	public void shouldThrowExceptionWhenTryToDeleteSystemEmailsFolders(){
		systemFolder.delete();
	}
	
	@Test
	(expected = CannotDeleteSystemFolderException.class)
	public void shouldThrowExceptionWhenTryToDeleteMeetingsFolder(){
		meetingFolder.delete();
	}
	
	@Test
	(expected = UnsupportedOperationException.class)
	public void shouldThrowExceptionWhenTryToLoadEmailsFromMeetingsFolder(){
		meetingFolder.loadEmails();
	}
	
	@Test
	(expected = RootFolderSubfolderCreationException.class)
	public void shouldThrowExceptionWhenTryToCreateSubFoldersInsideMeetingsFolder(){
		meetingFolder.createSubFolder(meetingFolderPath);
	}
	
}

class SystemFolderCUT extends EmailsSystemFolder{
	public boolean exceptionWasThrown = false;
	
	public SystemFolderCUT(String path) {
		super(path);
		
		// inject test double
		setFolderDao(new FolderDaoStub());
	}
	
	public FolderDao getFolderDao() {
		return folderDao;
	}
}

class UserFolderCUT extends EmailsUserFolder {
	public UserFolderCUT(String path) {
		super(path);
		
		// inject test double
		setFolderDao(new FolderDaoStub());
	}
	
	public FolderDao getFolderDao() {
		return folderDao;
	}
}

class MeetingsFolderCUT extends MeetingsFolder {
	public MeetingsFolderCUT(String path) {
		super(path);
		
		// inject test double
		setFolderDao(new FolderDaoStub());
	}
	
	public FolderDao getFolderDao() {
		return folderDao;
	}
}

class FolderDaoStub implements FolderDao {
	
	public boolean loadEmailsWasCalled = false;
	public boolean deleteWasCalled = false;
	public boolean createWasCalled = false;
		
	@Override
	public Iterable<Map<String,String>> loadEmails(String dir){
		List<Map<String,String>> emails = new ArrayList<Map<String,String>>();
		buildEmailDummyData(emails);
		loadEmailsWasCalled = true;
		return emails;
    }
	
	public void buildEmailDummyData(List<Map<String,String>> emails){
		Map<String,String> email = new HashMap<String,String>();
		email.put("Id","06e80a38-2e68-4ff5-b6a1-ffe6ac82c2cd");
		email.put("From","cec@gmail.com");
		email.put("To", "PankajKapania@yahoo.com");
		email.put("CC","amish.gala@gmail.com;dayevid@yahoo.com;romeo@gmail.co");
		email.put("Subject","test email");
		email.put("Body","email");
		email.put("LastModifiedTime","2013.05.17_At_13.51.56.616");
		email.put("SentTime","2013.05.17_At_13.51.56.616");
		email.put("ParentFolder","emails/Outbox");
		
		emails.add(email);	
	}
	
	@Override
	public void delete(String folderPath){
		deleteWasCalled = true;
	}
    
	@Override
	public void createSubFolder(String parentFolder, String newChildFolderName){
		createWasCalled = true;
	}

	@Override
	public Iterable<String> loadSubFolders(String parentFolder) {
	   return null;
	}
	
	@Override
	public Iterable<Map<String,String>> loadMeetings(String parentFolder) {
	   return null;
	}
}