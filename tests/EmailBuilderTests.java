package tests;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.config.CECConfigurator;
import cec.model.Email;
import cec.model.EmailBuilder;
import cec.model.Folder;
import cec.model.FolderFactory;

public class EmailBuilderTests {
	
	EmailBuilder cut;
	UUID emailId;
	Folder myFolder;
	Email email;
	HashMap<String, String> fields;
	
	@Before
	public void setUp() throws Exception {
		cut = new EmailBuilder();
		emailId = UUID.randomUUID();	
		myFolder = FolderFactory.getFolder("test/folder");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void buildSimpleEmail() {
		email = cut.withId(emailId)
			.withFrom(CECConfigurator.getReference().get("ClientEmail"))
			.withTo("PankajKapania@yahoo.com")
			.withCC("PankajKapania@gmail.com")
			.withSubject("TestSubject1")
			.withBody("Body1")
			.withLastModifiedTime("2013.05.12_At_14.07.56.874")
			.withSentTime("2013.05.13_At_14.07.56.874")
			.withParentFolder(myFolder)
			.build();
		
		assertTrue(email.getId().equals(emailId));
		assertTrue(email.getFrom().equals("cec.user@cec.com"));
		assertTrue(email.getTo().equals("PankajKapania@yahoo.com"));
		assertTrue(email.getCC().equals("PankajKapania@gmail.com"));
		assertTrue(email.getSubject().equals("TestSubject1"));
		assertTrue(email.getBody().equals("Body1"));
		assertTrue(email.getLastModifiedTime().equals("2013.05.12_At_14.07.56.874"));
		assertTrue(email.getSentTime().equals("2013.05.13_At_14.07.56.874"));
		assertEquals(email.getParentFolder(), myFolder);
	}
	
	@Test
	public void verifyComputedFields() {
		email = cut.computeID()
			.computelastModifiedTime()
			.computeSentTime()
			.build();
		
		// Not really great tests. Need to think about this a bit more
		assertNotSame(UUID.randomUUID(), email.getId());
		assertNotSame(new Date(), email.getLastModifiedTime());
		assertNotSame(new Date(), email.getSentTime());				
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
		email = cut.build();
		assertNotNull(email);
	}
	
	// TODO: make a better test for the load method. Fill in a full HashMap.
}
