package tests.integration;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Email;
import cec.model.Folder;
import cec.model.FolderFactory;
import cec.model.Rule;
import cec.model.RuleBuilder;
import cec.model.RuleSet;
import cec.model.RuleSetFactory;

public class RuleSetModelAndDaoIntegrationTests {

	Email email1, email2;
	Rule rule1, rule2, rule3;
	Rule integrationRule1, integrationRule2, integrationRule3;
	RuleBuilder ruleBuilder, ruleBuilder2;
	RuleSet ruleSet;

	UUID id1, id2;
	int rank1, rank2;
	String emailAddresses1, emailAddresses2;
	String words1, words2;
	Folder targetFolder1, targetFolder2;
	Folder inbox, createdFolder1, createdFolder2;
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

		
		inbox = FolderFactory.getFolder("emails/Inbox");

		createdFolder1 = FolderFactory.getFolder("emails/Inbox/rule1");
		createdFolder2 = FolderFactory.getFolder("emails/Inbox/rule2");
		createdFolder1.delete();
		createdFolder2.delete();
		inbox.createSubFolder("rule1");
		inbox.createSubFolder("rule2");
		createdFolder1 = FolderFactory.getFolder("emails/Inbox/rule1");
		createdFolder2 = FolderFactory.getFolder("emails/Inbox/rule2");
		
		isActive1 = true;
		isActive2 = false;

		ruleBuilder = new RuleBuilder();
		ruleBuilder2 = new RuleBuilder();
		rule1 = ruleBuilder.computeID().withEmailAddresses(emailAddresses1)
				.withWords(words1).withTargetFolder(createdFolder1)
				.withIsActive(isActive1).build();
		rule2 = ruleBuilder2.computeID().withEmailAddresses(emailAddresses2)
				.withWords(words2).withTargetFolder(createdFolder2)
				.withIsActive(isActive2).build();
		ruleList.add(rule1);
		ruleList.add(rule2);
	}

	@After
	public void tearDown() throws Exception {
		createdFolder1.delete();
		createdFolder2.delete();

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

	@Test
	public void createLoadAndSortRules() {
		List<Rule> rulesToBeSorted = new ArrayList<>();
		int count = 0;
		rule1.save();
		rule2.save();
		Iterable<Rule> rules = ruleSet.loadRules();
		for (Rule rule : rules) {
			if ((rule.getId().compareTo(rule1.getId()) == 0)
					|| (rule.getId().compareTo(rule2.getId()) == 0)) {
				count++;
				rulesToBeSorted.add(rule);
			}
		}

		Collections.sort(rulesToBeSorted);
		boolean expression = rulesToBeSorted.get(0).getRank() < rulesToBeSorted
				.get(1).getRank();
		assertTrue(expression);
		assertEquals(count, ruleList.size());
		rule1.delete();
		rule2.delete();
	}

	@Test
	public void createLoadAndSwapRules() {
		List<Rule> rulesToBeSwapped = new ArrayList<>();
		int count = 0;
		rule1.save();
		rule2.save();
		Iterable<Rule> rules = ruleSet.loadRules();
		for (Rule rule : rules) {
			if ((rule.getId().compareTo(rule1.getId()) == 0)
					|| (rule.getId().compareTo(rule2.getId()) == 0)) {
				count++;
				rulesToBeSwapped.add(rule);
			}
		}
		Collections.sort(rulesToBeSwapped);
		int rule1BeforeSwapping = rulesToBeSwapped.get(0).getRank();
		int rule2BeforeSwapping = rulesToBeSwapped.get(1).getRank();
		ruleSet.swapRank(rulesToBeSwapped.get(0), rulesToBeSwapped.get(1));
		int rule1AfterSwapping = rulesToBeSwapped.get(0).getRank();
		int rule2AfterSwapping = rulesToBeSwapped.get(1).getRank();
		assertEquals(rule1BeforeSwapping, rule2AfterSwapping);
		assertEquals(rule2BeforeSwapping, rule1AfterSwapping);

		rule1.delete();
		rule2.delete();
	}

	@Test
	public void createLoadAndSwapAndUpdateRules() {
		List<Rule> rulesToBeSwapped = new ArrayList<>();
		rule1.save();
		rule2.save();
		Map<UUID, Integer> idToRankBefore = new HashMap<>();
		Map<UUID, Integer> idToRankAfter = new HashMap<>();
		Iterable<Rule> rulesBeforeSwapping = ruleSet.loadRules();
		for (Rule rule : rulesBeforeSwapping) {
			if ((rule.getId().compareTo(rule1.getId()) == 0)
					|| (rule.getId().compareTo(rule2.getId()) == 0)) {
				rulesToBeSwapped.add(rule);
				idToRankBefore.put(rule.getId(), rule.getRank());
			}
		}

		Collections.sort(rulesToBeSwapped);

		ruleSet.swapRank(rulesToBeSwapped.get(0), rulesToBeSwapped.get(1));
		Iterable<Rule> rulesAfterSwapping = ruleSet.loadRules();
		for (Rule rule : rulesAfterSwapping) {
			if ((rule.getId().compareTo(rule1.getId()) == 0)
					|| (rule.getId().compareTo(rule2.getId()) == 0)) {

				idToRankAfter.put(rule.getId(), rule.getRank());
			}
		}

		assertEquals(idToRankBefore.get(rule1.getId()),
				idToRankAfter.get(rule2.getId()));
		assertEquals(idToRankBefore.get(rule2.getId()),
				idToRankAfter.get(rule1.getId()));
		rule1.delete();
		rule2.delete();
	}

}
