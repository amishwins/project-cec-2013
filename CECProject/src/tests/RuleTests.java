package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Email;
import cec.model.Folder;
import cec.model.Meeting;
import cec.model.Rule;
import cec.model.RuleImpl;

public class RuleTests {
	Rule ruleCUT;

	@Before
	public void setUp() throws Exception {
		ruleCUT = new RuleImpl(1, "amish.gala@gmail.com; pankajkapania@yahoo.com", 
				"jokes, soen", new FolderStub("Test"), true);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}

class FolderStub extends Folder {

	public FolderStub(String path) {
		super(path);
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createSubFolder(String newFolderName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterable<Email> loadEmails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Email> searchEmails(String searchString,
			Folder folderToSearchIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Meeting> loadMeetings() {
		// TODO Auto-generated method stub
		return null;
	}
	
}