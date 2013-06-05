package cec.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cec.model.FolderFactory;
import cec.model.Rule;
import cec.model.RuleBuilder;
import cec.model.RuleSet;
import cec.model.RuleSetFactory;

import cec.view.RuleViewEntity;

public class RuleService {
	private RuleSet ruleSet;	
	
	public RuleService() {
		ruleSet = RuleSetFactory.getRuleSetInstance();
	}
	
	public void save(RuleViewEntity ruleViewEntity) {
		Rule rule = (new RuleBuilder())
				.withId(UUID.randomUUID())
				.withRank(ruleSet.getNextRank())
				.withEmailAddresses(ruleViewEntity.getEmailAddresses())
				.withWords(ruleViewEntity.getWords())
				.withTargetFolder(FolderFactory.getFolder(ruleViewEntity.getFolderPath()))
				.withIsActive(true)
				.build();
		
		rule.save();  // should this call the ruleset?
	}	
	
	public void delete(RuleViewEntity ruleViewEntity) {
		Rule rule = (new RuleBuilder())
				.withId(ruleViewEntity.getID())
				.build();
		rule.delete();
	}

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
}
