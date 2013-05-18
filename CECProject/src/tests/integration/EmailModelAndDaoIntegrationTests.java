package tests.integration;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.config.CECConfigurator;
import cec.model.Email;
import cec.model.EmailBuilder;
import cec.persistence.EmailDao;
import cec.persistence.EmailDaoFactory;


// (maybe move this comment) more dangerous because it creates objects and deletes objects in the file system
public class EmailModelAndDaoIntegrationTests {
	
	Email savedEmail;
	Email retrievedEmail;
	EmailBuilder emailBuilder;
	UUID emailId;
	String emailIDString;
	EmailDao emailDao;
	

	@Before
	public void setUp() throws Exception {
		emailId = UUID.randomUUID();
		emailIDString = emailId.toString() + ".xml";
		emailBuilder = new EmailBuilder();
		savedEmail = (new EmailBuilder()).withId(emailId).build();		
		emailDao = EmailDaoFactory.getEmailDaoInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void saveEmailAndThenRetrieve() {
		savedEmail.saveToDraftFolder();
		retrievedEmail = emailBuilder.load(emailDao.loadEmail(CECConfigurator.getReference().get("Drafts"), emailIDString)).build();
		assertEquals(retrievedEmail.getId(), emailId);
		retrievedEmail.delete();
	}

}
