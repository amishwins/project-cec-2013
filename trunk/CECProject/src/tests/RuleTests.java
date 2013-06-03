package tests;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Email;
import cec.model.Folder;
import cec.model.Meeting;
import cec.model.Rule;
import cec.model.RuleImpl;
import cec.persistence.RuleDao;

public class RuleTests {
	RuleImplCUT ruleCUT;
	EmailStub email;
	RuleDaoStub ruleDaoStub = new RuleDaoStub();

	@Before
	public void setUp() throws Exception {
		ruleCUT = new RuleImplCUT(UUID.randomUUID(), 1, "amish.gala@gmail.com; pankajkapania@yahoo.com", 
				"jokes, soen", new FolderStub("Test"), true);
		ruleCUT.setRuleDao(ruleDaoStub);
		email = new EmailStub();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void ruleAppliesToEmailAddressEmailMoved() {
		email.from = "pankajkapania@yahoo.com";
		ruleCUT.apply(email);
		assertTrue(email.moveWasCalled);
	}

	@Test
	public void ruleAppliesToWordsInSubjectEmailMoved() {
		email.from = "billybob@gmail.com"; //not found
		email.subject = "These are some jokes for you"; // the word jokes should be found
		ruleCUT.apply(email);
		assertTrue(email.moveWasCalled);
	}

	@Test
	public void ruleAppliesToWordsInBodyEmailMoved() {
		email.from = "billybob@gmail.com"; //not found
		email.subject = "bad man";
		email.body = "These are some jokes for you"; // the word jokes should be found
		ruleCUT.apply(email);
		assertTrue(email.moveWasCalled);
	}
	
	@Test
	public void ruleDoesNotApply() {
		email.from = "billy@gmail.com";
		email.subject = "no joke";
		email.body = "i like coen 491";
		ruleCUT.apply(email);
		assertFalse(email.moveWasCalled);
	}
	
	@Test
	public void saveCallsDaoSave() {
		ruleCUT.save();
		assertTrue(ruleDaoStub.saveWasCalled);
	}

	@Test
	public void deleteCallsDaoSave() {
		ruleCUT.delete();
		assertTrue(ruleDaoStub.deleteWasCalled);
	}
	
	@Test
	public void deactivateIsDeactivatedDoesntApply() {
		ruleCUT.deactivate();
		assertFalse(ruleCUT.isActive());
		ruleCUT.apply(email);
		assertFalse(email.moveWasCalled);		
	}
	
	@Test
	public void activateAfterDeactivate() {
		ruleCUT.deactivate();
		ruleCUT.activate();
		email.from = "pankajkapania@yahoo.com";
		ruleCUT.apply(email);
		assertTrue(ruleCUT.isActive());
		assertTrue(email.moveWasCalled);		
	}	
}

class RuleImplCUT extends RuleImpl {

	public RuleImplCUT(UUID id, int rank, String emailAddresses, String words,
			Folder targetFolder, boolean active) {
		super(id, rank, emailAddresses, words, targetFolder, active);
		// TODO Auto-generated constructor stub
	}
	
	protected void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}
	
}

class RuleDaoStub implements RuleDao {
	public boolean saveWasCalled = false;
	public boolean deleteWasCalled = false;

	@Override
	public void save(UUID id, String rank, String sender, String keyword,
			String tartgetFolder, String status, String pathToSaveRuleFile) {
		saveWasCalled = true;
	}

	@Override
	public void delete(String path, UUID fileName) {
		deleteWasCalled = true;	
	}

	@Override
	public Map<String, String> loadRule(String folder, String ruleXmlFileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Map<String, String>> loadAllRules(String pathToRuleFolder) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

class EmailStub implements Email {
	public String from;
	public String subject;
	public String body;
	public boolean moveWasCalled;

	@Override
	public int compareTo(Email o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isMatch(String pattern) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void send() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveToDraftFolder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move(Folder destDir) {
		moveWasCalled = true;
	}

	@Override
	public UUID getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFrom() {
		return from;	
	}

	@Override
	public String getTo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubject() {
		return subject;
	}

	@Override
	public String getBody() {
		return body;
	}

	@Override
	public String getSentTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLastModifiedTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Folder getParentFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLastModifiedTimeNicelyFormatted() {
		// TODO Auto-generated method stub
		return null;
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