package cec.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cec.config.CECConfigurator;
import cec.persistence.RuleDao;
import cec.persistence.RuleDaoFactory;

public class RuleSetImpl implements RuleSet {
	RuleDao ruleDao = RuleDaoFactory.getRuleDaoInstance();

	public RuleSetImpl(RuleDao ruleDao) {
		super();
		this.ruleDao = ruleDao;
	}

	public RuleDao getRuleDao() {
		return ruleDao;
	}

	public void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}

	@Override
	public void swapRank(Rule first, Rule second) {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterable<Rule> loadRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Rule> loadActiveRules() {
		List<Rule> listOfActiveRules = new ArrayList<>();
		Iterable<Rule> listOfAllRules = buildAllRules();
     //   filterActiverules(); 
		return null;
	}

	private Iterable<Rule> buildAllRules() {
		Iterable<Map<String, String>> rulesData = ruleDao
				.loadAllRules(CECConfigurator.getReference().get("Rules"));
		List<Rule> rules = new ArrayList<>();
		for (Map<String, String> ruleData : rulesData) {
			UUID id = UUID.fromString(ruleData.get("Id"));
			String rank = ruleData.get("Rank");
			String senders = ruleData.get("Senders");
			String keywords = ruleData.get("Keywords");
			String tartgetFolder = ruleData.get("TartgetFolder");
			String status = ruleData.get("Status");
			Rule rule = new RuleImpl(id, Integer.parseInt(rank), senders,
					keywords, FolderFactory.getFolder(tartgetFolder),
					Boolean.parseBoolean(status));
            rules.add(rule);
		}
		return rules;
	}

	@Override
	public void apply(Iterable<Email> targets) {
		// TODO Auto-generated method stub

	}

}
