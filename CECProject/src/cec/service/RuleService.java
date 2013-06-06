package cec.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cec.config.CECConfigurator;
import cec.model.FolderFactory;
import cec.model.Rule;
import cec.model.RuleBuilder;
import cec.model.RuleSet;
import cec.model.RuleSetFactory;

import cec.view.RuleViewEntity;

/**
 * Represents the actions that can be performed against a Rule from
 * the Presentation Layer (cec.view) such as creating, updating, deleting, 
 * shuffling, apply and apply all; also retrieves information 
 * from model/persistence Layers to be used by the JTable <code>ruleTable</code> 
 * on <code>RuleSettings</code> class. 
 */

public class RuleService {
	
	/** The rule set. */
	private RuleSet ruleSet;	
	
	/**
	 * Instantiates a new rule service.
	 */
	public RuleService() {
		ruleSet = RuleSetFactory.getRuleSetInstance();
	}

	
	
	/**
	 * It saves the rule in the system.
	 * Postcondition: Rule has been saved into the system. 
	 *
	 * @param ruleViewEntity the rule view entity
	 */
	public void save(RuleViewEntity ruleViewEntity) {
		Rule rule = (new RuleBuilder())
				.withId(UUID.randomUUID())
				.withEmailAddresses(ruleViewEntity.getEmailAddresses())
				.withWords(ruleViewEntity.getWords())
				.withTargetFolder(FolderFactory.getFolder(ruleViewEntity.getFolderPath()))
				.withIsActive(true)
				.build();
		
		rule.save();  // should this call the ruleset?
	}	
	
	/**
	 * Precondition: rule exists in the system.
	 * PostCondition: rule does not exist in the system.
	 * this method deletes the rule from the system.
	 *
	 * @param ruleViewEntity the rule view entity
	 */
	public void delete(RuleViewEntity ruleViewEntity) {
		Rule rule = (new RuleBuilder())
				.withId(ruleViewEntity.getID())
				.withRank(ruleViewEntity.getRank())
				.withEmailAddresses(ruleViewEntity.getEmailAddresses())
				.withWords(ruleViewEntity.getWords())
				.withTargetFolder(FolderFactory.getFolder(ruleViewEntity.getFolderPath()))
				.withIsActive(true)
				.build();
		rule.delete();
	}

	/**
	 * Precondition: rule exists in the system.
	 * This method Load all rules from the system.
	 *
	 * @return the iterable
	 */
	public Iterable<RuleViewEntity> loadAllRules() {
		List<RuleViewEntity> listOfRuleViewEntities = new ArrayList<>();
		Iterable<Rule> rules = ruleSet.loadRules();
		for(Rule rule: rules){
			RuleViewEntity ruleViewEntity = new RuleViewEntity();
			ruleViewEntity.setId(rule.getId());
			ruleViewEntity.setRank(rule.getRank());
			ruleViewEntity.setEmailAddresses(rule.getEmailAddresses());
			ruleViewEntity.setWords(rule.getWords());
			ruleViewEntity.setFolderPath(rule.getTargetFolder().getPath());
			listOfRuleViewEntities.add(ruleViewEntity);
		}
		return listOfRuleViewEntities;
	}

	/**
	 * it builds the rule from the RuleViewEntity and then applies to 
	 * email or collection of email(s).
	 * 
	 * @param ruleViewEntity the rule view entity
	 */
	public void apply(RuleViewEntity ruleViewEntity) {
		Rule rule = (new RuleBuilder())
				.withId(ruleViewEntity.getID())
				.withRank(ruleViewEntity.getRank())
				.withEmailAddresses(ruleViewEntity.getEmailAddresses())
				.withWords(ruleViewEntity.getWords())
				.withTargetFolder(FolderFactory.getFolder(ruleViewEntity.getFolderPath()))
				.withIsActive(true)
				.build();
		
		rule.apply();  // should this call the ruleset?
		
	}

	/**
	 * Postcondition: all the emails will be moved to the new folder specified 
	 * by the rule after the invocation of this rule on email(s) present in the 
	 * inbox folder.
	 * it builds the rule from the RuleViewEntity and then applies to 
	 * email or collection of email(s).
	 * 
	 * @param ruleViewEntity the rule view entity
	 */
	public void applyAll() {
		RuleSet ruleSet = RuleSetFactory.getRuleSetInstance();
		ruleSet.apply(FolderFactory.getFolder(CECConfigurator.getReference().get("Inbox")).loadEmails());
		
	}

	/**
	 * Precondition: Rule exist in the system and new values have been 
	 * specified by the user.
	 *  
	 * It builds the rule from the RuleViewEntity and then updates the new data values
	 * for the existing rule.
	 *
	 * @param ruleViewEntity the rule view entity
	 */
	public void update(RuleViewEntity ruleViewEntity) {
		Rule rule = (new RuleBuilder())
				.withId(ruleViewEntity.getID())
				.withRank(ruleViewEntity.getRank())
				.withEmailAddresses(ruleViewEntity.getEmailAddresses())
				.withWords(ruleViewEntity.getWords())
				.withTargetFolder(FolderFactory.getFolder(ruleViewEntity.getFolderPath()))
				.withIsActive(true)
				.build();
		
		rule.update();  // should this call the ruleset?
		
	}

	/**Precondition: Rule specified in the argument previous and current exist with the different 
	 * id and rank exist in the system.
	 * Postcondition: rank of the rules have been swapped.
	 * Invariant: Rule id does not change during the shuffle process.
	 * 
	 * It swaps the ranks of the rules.
	 *
	 * @param previous the previous
	 * @param current the current
	 */
	public void shuffle(RuleViewEntity previous, RuleViewEntity current) {
		Rule previousRule = (new RuleBuilder())
				.withId(previous.getID())
				.withRank(previous.getRank())
				.withEmailAddresses(previous.getEmailAddresses())
				.withWords(previous.getWords())
				.withTargetFolder(FolderFactory.getFolder(previous.getFolderPath()))
				.withIsActive(true)
				.build();
		Rule currentRule = (new RuleBuilder())
				.withId(current.getID())
				.withRank(current.getRank())
				.withEmailAddresses(current.getEmailAddresses())
				.withWords(current.getWords())
				.withTargetFolder(FolderFactory.getFolder(current.getFolderPath()))
				.withIsActive(true)
				.build();
		ruleSet.swapRank(previousRule, currentRule);
	}
}
