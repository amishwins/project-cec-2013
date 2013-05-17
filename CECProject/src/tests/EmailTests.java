package tests;

import static org.junit.Assert.*;

import java.util.UUID;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.EmailImpl;
import cec.model.Folder;
import cec.model.FolderFactory;
import cec.persistence.EmailDao;

public class EmailTests {
	
	EmailImplCUT myEmail;
	EmailDaoStub stubbedEmailPersistence;
	Folder folderStub;
	UUID id;

	@Before
	public void setUp() throws Exception {
		id = UUID.randomUUID();
		folderStub = FolderFactory.getFolder("test/name");
		stubbedEmailPersistence = new EmailDaoStub();
		myEmail = new EmailImplCUT(id, "from@email.com", "to@email.com", 
				"cc@email.com", "Subject", "Body", "20130516", "20130515", folderStub);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test 
	public void newEmailAbleToGetFields() {
		assertTrue(myEmail.getId().equals(id));
		assertTrue(myEmail.getTo().equals("to@email.com"));
		assertTrue(myEmail.getFrom().equals("from@email.com"));
		assertTrue(myEmail.getCC().equals("cc@email.com"));
		assertTrue(myEmail.getSubject().equals("Subject"));
		assertTrue(myEmail.getBody().equals("Body"));
		assertTrue(myEmail.getLastModifiedTime().equals("20130516"));
		assertTrue(myEmail.getSentTime().equals("20130515"));
		assertTrue(myEmail.getParentFolder().equals(folderStub));
	}

	@Test
	public void saveWasCalledForSending() {
		myEmail.send();
		assertTrue(((EmailDaoStub)myEmail.getEmailDao()).saveWasCalled);
	}
	
	@Test
	public void saveWasCalledForSavingToDraft() {
		myEmail.saveToDraftFolder();
		assertTrue(((EmailDaoStub)myEmail.getEmailDao()).saveWasCalled);
	}

	@Test
	public void getNicelyFormattedDate() {
		myEmail = new EmailImplCUT(id, "", "", "", "", "", "2013.05.17_At_13.52.03.824",
				"", null);
		String formattedEmail = myEmail.getLastModifiedTimeNicelyFormatted();
		assertEquals(formattedEmail, "Fri, May 17, 2013");		
	}
	
	@Test
	public void delete() {
		myEmail.delete();
		assertTrue(((EmailDaoStub)myEmail.getEmailDao()).deleteWasCalled);
	}
	
	@Test
	public void move() {
		myEmail.move(folderStub);
		assertTrue(((EmailDaoStub)myEmail.getEmailDao()).moveWasCalled);
	}
}

class EmailImplCUT extends EmailImpl {
	
	
	public EmailImplCUT(UUID id, String from, String to, String cc,
			String subject, String body, String lastModifiedTime,
			String sentTime, Folder parentFolder) {
		super(id, from, to, cc, subject, body, lastModifiedTime, sentTime, parentFolder);
		
		// inject test double
		setEmailDao(new EmailDaoStub());
	}
	
	public EmailDao getEmailDao() {
		return emailDao;
	}
	
}

class EmailDaoStub implements EmailDao {
	
	public boolean saveWasCalled = false;
	public boolean deleteWasCalled = false;
	public boolean moveWasCalled = false;

	@Override
	public void save(UUID id, String from,  String to, String cc, String subject, String body,String lastModifiedTime,String sentTime, String location) {
		saveWasCalled = true;
		
	}
	
	@Override
	public void delete(String path, UUID id){
		deleteWasCalled = true;
		
	}
	
	@Override
	public void move(UUID id, String srcDir, String destDir){
		moveWasCalled = true;		
	}
}
