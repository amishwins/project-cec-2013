package tests.integration;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Folder;
import cec.model.FolderFactory;
import cec.model.Rule;
import cec.model.RuleBuilder;
import cec.model.RuleSet;
import cec.model.RuleSetFactory;
import cec.model.RuleSetImpl;
import cec.persistence.RuleDao;
import cec.persistence.RuleDaoFactory;

public class RuleSetModelAndDaoIntegrationTests {

	Rule rule1, rule2;
	RuleBuilder ruleBuilder;
	RuleSet ruleSet;

	UUID id1, id2;
	int rank1, rank2;
	String emailAddresses1, emailAddresses2;
	String words1, words2;
	Folder targetFolder1, targetFolder2;
	Boolean isActive1, isActive2;
	List<Rule> ruleList;

	@Before
	public void setUp() throws Exception {
		ruleSet = RuleSetFactory.getRuleSetInstance();
		ruleList = new ArrayList<>();
		emailAddresses1 = "a@b.com;b@b.com";
		emailAddresses2 = "a@c.com;d@b.com";
		words1 = "jokes jokes";
		words2 = "jokes2 jokes2";
		targetFolder1 = FolderFactory.getFolder("emails/Inbox/Jokes");
		targetFolder2 = FolderFactory.getFolder("emails/Inbox/Jokes");
		isActive1 = true;
		isActive2 = false;

		RuleBuilder ruleBuilder = new RuleBuilder();
		RuleBuilder ruleBuilder2 = new RuleBuilder();
		rule1 = ruleBuilder.computeID().withEmailAddresses(emailAddresses1)
				.withWords(words1).withTargetFolder(targetFolder1)
				.withIsActive(isActive1).build();
		rule2 = ruleBuilder2.computeID().withEmailAddresses(emailAddresses2)
				.withWords(words2).withTargetFolder(targetFolder2)
				.withIsActive(isActive2).build();
		ruleList.add(rule1);
		ruleList.add(rule2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createAndLoadAllRules() {
		int count = 0;
		rule1.save();
		rule2.save();
		Iterable<Rule> rules = ruleSet.loadRules();
		for (Rule rule : rules) {
			if ((rule.getId().compareTo(rule1.getId()) == 0)
					|| (rule.getId().compareTo(rule2.getId()) == 0)) {
				count++;
			}
		}
		assertEquals(count, ruleList.size());
		rule1.delete();
		rule2.delete();
	}

	@Test
	public void createAndLoadOnlyActiveRules() {
		int count = 0;
		rule1.save();
		rule2.save();
		Iterable<Rule> rules = ruleSet.loadActiveRules();
		for (Rule rule : rules) {
			if ((rule.getId().compareTo(rule1.getId()) == 0)) {
				count++;
			}
		}
		assertEquals(count, 1);
		rule1.delete();
		rule2.delete();
	}

}
