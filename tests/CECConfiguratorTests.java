package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.config.CECConfigurator;

public class CECConfiguratorTests {
	
	private CECConfigurator cut;

	@Before
	public void setUp() throws Exception {
		cut = CECConfigurator.getReference();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void addedValueCanBeRetrieved() {
		cut.put("Dog", "Bark");
		assertTrue(cut.get("Dog").equals("Bark"));
	}
	
	@Test(expected=NullPointerException.class)
	public void valueNotFoundThrowsException() {
		@SuppressWarnings("unused")
		String meow = cut.get("Cat");
	}
	
	@Test
	public void getDefaultValues() {
		// two ways to retrieve the client email
		assertTrue(cut.get("ClientEmail").equals("cec.user@cec.com"));
		assertTrue(cut.getClientEmailAddress().equals("cec.user@cec.com"));
		
		assertTrue(cut.get("Inbox").equals("emails/Inbox"));
		assertTrue(cut.get("Drafts").equals("emails/Drafts"));
		assertTrue(cut.get("Outbox").equals("emails/Outbox"));
		assertTrue(cut.get("Sent").equals("emails/Sent"));
		assertTrue(cut.get("DateFormat").equals("yyyy.MM.dd_'At'_HH.mm.ss.SSS"));
	}
}
