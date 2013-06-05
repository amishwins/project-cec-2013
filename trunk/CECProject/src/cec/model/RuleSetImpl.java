package cec.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cec.config.CECConfigurator;
import cec.persistence.RuleDao;
import cec.persistence.RuleDaoFactory;

public class RuleSetImpl implements RuleSet {
	RuleDao ruleDao;

	public RuleSetImpl() {
		setRuleDao(RuleDaoFactory.getRuleDaoInstance());
	}

	public RuleDao getRuleDao() {
		return ruleDao;
	}

	public void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}
	
	@Override
	public Iterable<Rule> loadSortedActiveRules() {
		Iterable<Rule> sorted = loadActiveRules();
		Collections.sort((List<Rule>) sorted);
		return sorted;
	}

	@Override
	public Iterable<Rule> loadActiveRules() {
		Iterable<Rule> listOfAllRules = loadRules();
		Iterable<Rule> listOfActiveRules = filterActiveRules(listOfAllRules); 
		return listOfActiveRules;
	}
	@Override
	public Iterable<Rule> loadRules() {
		Iterable<Map<String, String>> rulesData = ruleDao
				.loadAllRules(CECConfigurator.getReference().get("RuleFolder"));
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
		Collections.sort((List<Rule>) rules);
		return rules;
	}
	
	protected Iterable<Rule> filterActiveRules(Iterable<Rule> allRules){
		List<Rule> activeRules = new ArrayList<Rule>();
		for (Rule rule : allRules){
			if(rule.isActive()){
				activeRules.add(rule);
			}
		}
		return activeRules;
	}

	@Override
	public void apply(Iterable<Email> targets) {
		for(Email e: targets) {
			for(Rule r: loadSortedActiveRules()) {
				if (r.apply(e))
					break;
			}
		}
	}	

	@Override
	public synchronized void  swapRank(Rule first, Rule second) {
		int temp = first.getRank();
		first.setRank(second.getRank());
		second.setRank(temp);
		first.update();
		second.update();
	}

	@Override
	public int getNextRank() {
		ArrayList<Rule> rules = (ArrayList<Rule>)loadRules();
		int highest = 0;
		for(Rule r: rules) {
			if (highest > r.getRank())
				highest = r.getRank();
		}
		
		return highest + 1;
	}


}