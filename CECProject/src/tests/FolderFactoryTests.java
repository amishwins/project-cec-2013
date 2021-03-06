package tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.config.CECConfigurator;
import cec.model.Folder;
import cec.model.FolderFactory;

public class FolderFactoryTests {
	
	Folder folder;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createEmailSystemFolder() {
		folder = FolderFactory.getFolder(CECConfigurator.getReference().get("Inbox"));
		assertTrue(folder.getClass().getName().equals("cec.model.EmailsSystemFolder"));		
	}
	
	@Test
	public void createEmailsUserFolder() {
		folder = FolderFactory.getFolder("BobSinclair");
		assertTrue(folder.getClass().getName().equals("cec.model.EmailsUserFolder"));		
	}
	
	@Test
	public void createMeetingSystemFolder() {
		folder = FolderFactory.getFolder(CECConfigurator.getReference().get("Meetings"));
		assertTrue(folder.getClass().getName().equals("cec.model.MeetingsFolder"));		
	}
	
}
