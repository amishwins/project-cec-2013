package cec.model;

import java.util.UUID;

import cec.config.CECConfigurator;
import cec.persistence.RuleDao;
import cec.persistence.RuleDaoFactory;

public class RuleImpl implements Rule {
	
	protected RuleDao ruleDao;
	private UUID id;
	private int rank;
	private String emailAddresses;
	private String words;
	private Folder targetFolder;
	private Boolean isActive;
	private Search searcher;
	
	public RuleImpl(UUID id, int rank, String emailAddresses, String words, Folder targetFolder, boolean active) {
		if ((emailAddresses == null || emailAddresses == "") && (words == null || words == "")) {
			throw new IllegalArgumentException("Must supply at least email addresses or search words");
		}
		
		this.id = id;
		this.rank = rank;
		this.emailAddresses = emailAddresses;
		this.words = words;
		this.targetFolder = targetFolder;
		this.isActive = active;
		setRuleDao(RuleDaoFactory.getRuleDaoInstance());
	}
	
	protected void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}
	
	@Override
	public UUID getId(){
		return id;
	}
	
	@Override
	public int getRank() {
		return rank;
	}

	@Override
	public String getEmailAddresses() {
		return emailAddresses;
	}

	@Override
	public String getWords() {
		return words;
	}

	@Override
	public Folder getTargetFolder() {
		return targetFolder;
	}

	@Override
	public void setEmailAddresses(String emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	@Override
	public void setWords(String words) {
		this.words = words;
	}

	@Override
	public void setTargetFolder(Folder target) {
		this.targetFolder = target;
	}

	@Override
	public boolean apply(Email email) {
		if (!isActive) return false;
		
		// if rule has both email address and words, both must apply
		// if rule has only email address, only from applies
		// if rule has only words, only subject and body applies
		boolean emailsSupplied = true;
		boolean wordsSupplied = true;
		if ( (emailAddresses == null) || (emailAddresses.trim().isEmpty()) ) 
			emailsSupplied = false;
		
		if ( (words == null) || (words.trim().isEmpty()) ) 
			wordsSupplied = false;
		
		String[] splitEmailAddresses = emailAddresses.trim().split(";");
		String[] splitWords = words.trim().split(" ");

		boolean match = false;
		if (emailsSupplied && wordsSupplied) {
			match = doesRuleApplyToSender(email, splitEmailAddresses, match) && 
					 doesRuleApplyToSubjectAndBody(email, splitWords, match);
		}
		else if (emailsSupplied) {
			match = doesRuleApplyToSender(email, splitEmailAddresses, match);
		} 
		else if (wordsSupplied) {
			match = doesRuleApplyToSubjectAndBody(email, splitWords, match);
		}
		else {
			throw new RuntimeException("A rule must have at least 1 email or 1 word");
		}
		
		if (match) {
			moveEmail(email);
			return true;
		}
		
		return false;
		
	}

	protected boolean doesRuleApplyToSubjectAndBody(Email email, String[] splitWords, boolean match) {
		for(String s: splitWords) {
			if (email.getSubject() == null || email.getSubject() == "") { 
			} else {
				searcher = new SearchImpl(email.getSubject(), s.trim());
				if (searcher.isMatch()) {
					match = true;
					break;
				}
			}
			if (email.getBody() == null || email.getBody() == "") {
			} else {
				searcher = new SearchImpl(email.getBody(), s.trim());
				if (searcher.isMatch()) {
					match = true;
					break;
				}	
			}
		}
		return match;
	}

	protected boolean doesRuleApplyToSender(Email email, String[] splitEmailAddresses, boolean match) {
		for(String s: splitEmailAddresses) {
			searcher = new SearchImpl(email.getFrom(), s.trim());
			if (searcher.isMatch()) {
				match = true;
				break;
			}
		}
		return match;
	}

	protected void moveEmail(Email email) {
		email.move(targetFolder);
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void activate() {
		isActive = true;
	}

	@Override
	public void deactivate() {
		isActive = false;
	}

	@Override
	public void save() {
		ruleDao.save(id, getEmailAddresses(), getWords(), targetFolder.getPath(), isActive.toString(), 
				CECConfigurator.getReference().get("RuleFolder"));
	}

	@Override
	public void delete() {
		ruleDao.delete(CECConfigurator.getReference().get("RuleFolder"), id);
		
	}

	@Override
	public int compareTo(Rule anotherRule) {
		if (this.rank < anotherRule.getRank())
			return -1;
		else if ( this.rank > anotherRule.getRank())
			return 1;
		else
			throw new RuntimeException("Two rules have the same rank. Call the dev team.");
	}

	@Override
	public void setRank(int rank) {
		this.rank = rank;
		
	}

	@Override
	public void update() {
		ruleDao.update(id, String.valueOf(rank), getEmailAddresses(), getWords(), targetFolder.getPath(), isActive.toString(), 
				CECConfigurator.getReference().get("RuleFolder"));
	}

	@Override
	public void apply() {
		Iterable<Email> emailsInInbox = FolderFactory.getFolder(CECConfigurator.getReference().get("Inbox")).loadEmails();
		for(Email e: emailsInInbox)
			apply(e);
	}
}
