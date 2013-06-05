package cec.model;

public interface RuleSet {
	public void swapRank(Rule first, Rule second);
	public Iterable<Rule> loadSortedActiveRules();
	public Iterable<Rule> loadRules();
	public Iterable<Rule> loadActiveRules();
	public void apply(Iterable<Email> targets);
	public int getNextRank();

}
