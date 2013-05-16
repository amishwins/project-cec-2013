package tests;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Email;
import cec.model.EmailImpl;
import cec.model.Folder;
import cec.persistence.EmailDao;

public class EmailTests {
	
	EmailImplCUT myEmail;
	EmailDaoStub stubbedEmailPersistence;

	@Before
	public void setUp() throws Exception {
		stubbedEmailPersistence = new EmailDaoStub();
		myEmail = new EmailImplCUT(UUID.randomUUID(), "a@b.com", "c@d.com", "", "", "", "", "", null);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void saveWasCalled() {
		myEmail.send();
		assertEquals(true, ((EmailDaoStub)myEmail.getEmailDao()).saveWasCalled);
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

	@Override
	public void save(String to, String cc, String subject, String body,
			String lastAccessedTime, String location) {
		saveWasCalled = true;
		
	}
	
}
