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
import cec.model.RuleImpl;
import cec.persistence.RuleDao;

public class RuleTests {
	RuleImplCUT ruleOnlyEmailAddressesCUT;
	RuleImplCUT ruleOnlyWordsCUT;
	RuleImplCUT ruleBothEmailAddressesAndWordsCUT;
	EmailStub email;
	RuleDaoStub ruleDaoStub = new RuleDaoStub();

	@Before
	public void setUp() throws Exception {
		ruleOnlyEmailAddressesCUT = new RuleImplCUT(UUID.randomUUID(), 1, "amish.gala@gmail.com; pankajkapania@yahoo.com", "", 
				new FolderStub("test"), true);
		ruleOnlyEmailAddressesCUT.setRuleDao(ruleDaoStub);
		
		ruleOnlyWordsCUT = new RuleImplCUT(UUID.randomUUID(), 1, "", "jokes, soen", 
				new FolderStub("test"), true);
		ruleOnlyWordsCUT.setRuleDao(ruleDaoStub);
		
		ruleBothEmailAddressesAndWordsCUT = new RuleImplCUT(UUID.randomUUID(), 1, "amish.gala@gmail.com; pankajkapania@yahoo.com", 
				"jokes, soen", new FolderStub("Test"), true);
		ruleBothEmailAddressesAndWordsCUT.setRuleDao(ruleDaoStub);
		
		email = new EmailStub();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void ruleForEmailAddressesAppliesEmailMoved() {
		email.from = "pankajkapania@yahoo.com";
		ruleOnlyEmailAddressesCUT.apply(email);
		assertTrue(email.moveWasCalled);
	}

	@Test
	public void ruleForEmailAddressesDoesNotApplyEmailNotMoved() {
		email.from = "bobby@yahoo.com";
		ruleOnlyEmailAddressesCUT.apply(email);
		assertFalse(email.moveWasCalled);
	}
	
	@Test
	public void ruleForWordsAppliesEmailMoved() {
		email.from = "bobby@yahoo.com";
		email.subject = "Today I love soen and maybe coen";
		ruleOnlyWordsCUT.apply(email);
		assertTrue(email.moveWasCalled);
	}
	
	@Test
	public void ruleForWordsDoesNotApplyEmailNotMoved() {
		email.from = "bobby@yahoo.com";
		email.subject = "Today I love cows and maybe goats";
		ruleOnlyWordsCUT.apply(email);
		assertFalse(email.moveWasCalled);		
	}
	
	@Test
	public void ruleForBothDoesNotApplyIfOnlyAddressMatch() {
		email.from = "pankajkapania@yahoo.com";
		email.body = "dogs and goats";
		ruleBothEmailAddressesAndWordsCUT.apply(email);
		assertFalse(email.moveWasCalled);
	}	
	
	@Test (expected=IllegalArgumentException.class)
	public void noEmailSuppliedAndNoTermsSuppliedThrowsException() {
		@SuppressWarnings("unused")
		RuleImplCUT emptyRule = new RuleImplCUT(UUID.randomUUID(), 1, "", "", new FolderStub("Test"), true);
	}
	
	@Test
	public void saveCallsDaoSave() {
		ruleBothEmailAddressesAndWordsCUT.save();
		assertTrue(ruleDaoStub.saveWasCalled);
	}

	@Test
	public void deleteCallsDaoSave() {
		ruleBothEmailAddressesAndWordsCUT.delete();
		assertTrue(ruleDaoStub.deleteWasCalled);
	}
	
	@Test
	public void deactivateIsDeactivatedDoesntApply() {
		ruleBothEmailAddressesAndWordsCUT.deactivate();
		assertFalse(ruleBothEmailAddressesAndWordsCUT.isActive());
		ruleBothEmailAddressesAndWordsCUT.apply(email);
		assertFalse(email.moveWasCalled);		
	}
	
	@Test
	public void activateAfterDeactivate() {
		ruleBothEmailAddressesAndWordsCUT.deactivate();
		ruleBothEmailAddressesAndWordsCUT.activate();
		email.from = "pankajkapania@yahoo.com";
		email.subject = "hi soen jokes";
		ruleBothEmailAddressesAndWordsCUT.apply(email);
		assertTrue(ruleBothEmailAddressesAndWordsCUT.isActive());
		assertTrue(email.moveWasCalled);		
	}	
}

class RuleImplCUT extends RuleImpl {

	public RuleImplCUT(UUID id, int rank, String emailAddresses, String words,
			Folder targetFolder, boolean active) {
		super(id, rank, emailAddresses, words, targetFolder, active);
	}
	
	protected void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}
	
}

class RuleDaoStub implements RuleDao {
	public boolean saveWasCalled = false;
	public boolean deleteWasCalled = false;

	@Override
	public void save(UUID id, String sender, String keyword,
			String tartgetFolder, String status, String pathToSaveRuleFile) {
		saveWasCalled = true;
	}

	@Override
	public void delete(String path, UUID fileName) {
		deleteWasCalled = true;	
	}

	@Override
	public Map<String, String> loadRule(String folder, String ruleXmlFileName) {
		return null;
	}

	@Override
	public Iterable<Map<String, String>> loadAllRules(String pathToRuleFolder) {
		return null;
	}

	@Override
	public void update(UUID id, String rank, String sender, String keyword,
			String tartgetFolder, String status, String pathToSaveRuleFile) {
		
	}

}

class EmailStub implements Email {
	public String from;
	public String subject;
	public String body;
	public boolean moveWasCalled;

	@Override
	public int compareTo(Email o) {
		return 0;
	}

	@Override
	public boolean isMatch(String pattern) {
		return false;
	}

	@Override
	public void send() {
				
	}

	@Override
	public void saveToDraftFolder() {
	
	}
	
	@Override
	public void saveToInboxFolder() {
		
	}

	@Override
	public void delete() {
		
	}

	@Override
	public void move(Folder destDir) {
		moveWasCalled = true;
	}

	@Override
	public UUID getId() {
		return null;
	}

	@Override
	public String getFrom() {
		return from;	
	}

	@Override
	public String getTo() {
		return null;
	}

	@Override
	public String getCC() {
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
	  return null;
	}

	@Override
	public String getLastModifiedTime() {
		return null;
	}

	@Override
	public Folder getParentFolder() {
		return null;
	}

	@Override
	public String getLastModifiedTimeNicelyFormatted() {
		return null;
	}
	
}

class FolderStub extends Folder {

	public FolderStub(String path) {
		super(path);
	}

	@Override
	public void delete() {
	}

	@Override
	public void createSubFolder(String newFolderName) {
	
	}

	@Override
	public Iterable<Email> loadEmails() {
		return null;
	}

	@Override
	public Iterable<Email> searchEmails(String searchString,
			Folder folderToSearchIn) {
		return null;
	}

	@Override
	public Iterable<Meeting> loadMeetings() {
		return null;
	}

	@Override
	public Iterable<Email> loadRuleApplicableEmails() {
		return null;
	}
	
}