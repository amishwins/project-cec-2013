package cec.model;

public interface RuleSet {
	public void swapRank(Rule first, Rule second);
	public Iterable<Rule> loadRules();
	public Iterable<Rule> loadActiveRules();
}
