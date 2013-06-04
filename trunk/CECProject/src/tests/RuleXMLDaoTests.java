package tests;

import static org.junit.Assert.*;


import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.persistence.RuleDao;
import cec.persistence.RuleDaoFactory;

public class RuleXMLDaoTests {
	
	// Dangerous test, which actually creates files in the file system and then deletes them
	RuleDao ruleDao;
	Map<String,String> ruleFromFS;
	UUID ruleID;
	String ruleFileName;
	String senders, keywords, tartgetFolder, status, ruleFolder;

	@Before
	public void setUp() throws Exception {
		ruleID = UUID.randomUUID();
		ruleFileName = ruleID.toString()+".xml";
		ruleDao = RuleDaoFactory.getRuleDaoInstance();
		ruleFromFS = new TreeMap<>();
		senders = "a@b.com;a@c.com";
		keywords = "school jokes";
		tartgetFolder = "Inbox/jokes";
		status = "active";
		ruleFolder = "rules";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void saveActuallySavesFileAndThenDeletesIt() {
		ruleDao.save(ruleID, senders, keywords, tartgetFolder, status, ruleFolder);
		ruleFromFS = ruleDao.loadRule(ruleFolder, ruleFileName);
		assertEquals(ruleFromFS.get("Id"), ruleID.toString());
		assertEquals(ruleFromFS.get("Senders").toString(), senders);
		assertEquals(ruleFromFS.get("Keywords").toString(), keywords);
		assertEquals(ruleFromFS.get("TartgetFolder").toString(), tartgetFolder);
		assertEquals(ruleFromFS.get("Status").toString(), status);
		ruleDao.delete(ruleFolder,ruleID);
	}
	
	@Test
	public void saveAndLoadAll() {
		ruleDao.save(ruleID, senders, keywords, tartgetFolder, status, ruleFolder);
		Iterable<Map<String,String>> rules = ruleDao.loadAllRules(ruleFolder);
		for(Map<String,String> rule: rules) {
			ruleFromFS = rule;
			assertNotNull(ruleFromFS);
		}
		ruleDao.delete(ruleFolder,ruleID);
	}
	
}
