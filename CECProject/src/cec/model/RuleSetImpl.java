package cec.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cec.config.CECConfigurator;
import cec.persistence.RuleDao;
import cec.persistence.RuleDaoFactory;


/**
 * The Class RuleSetImpl.
 */
public class RuleSetImpl implements RuleSet {
	
	/** The rule dao. */
	RuleDao ruleDao;

	/**
	 * Instantiates a new rule set impl.
	 */
	public RuleSetImpl() {
		setRuleDao(RuleDaoFactory.getRuleDaoInstance());
	}

	/**
	 * Gets the rule dao.
	 *
	 * @return the rule dao
	 */
	public RuleDao getRuleDao() {
		return ruleDao;
	}

	/**
	 * Sets the rule dao.
	 *
	 * @param ruleDao the new rule dao
	 */
	public void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}
	
	
	@Override
	public Iterable<Rule> loadSortedActiveRules() {
		Iterable<Rule> sorted = loadActiveRules();
		Collections.sort((List<Rule>) sorted);
		return sorted;
	}

	/**
	 * Precondition: rules exist in the system.
	 * Postcondition: Rules have been updated with the new rank values.
	 * It loads all the active rules from the system after filtering out active rules from the
	 * system.
	 * @param allRules the all rules
	 * @return the iterable
	 */
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
	
	/**
	 * Filter active rules.
	 *
	 * @param allRules the all rules
	 * @return the iterable
	 */
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


	/**
	 * Precondition: two rules exist in the system.
	 * Postcondition: Rules have been updated with the new rank values.
	 *
	 * @param allRules the all rules
	 * @return the iterable
	 */
	@Override
	public synchronized void  swapRank(Rule first, Rule second) {
		int temp = first.getRank();
		first.setRank(second.getRank());
		second.setRank(temp);
		first.update();
		second.update();
	}

	


}