package cec.model;

import java.util.UUID;

import cec.config.CECConfigurator;
import cec.persistence.RuleDao;

public class RuleImpl implements Rule {
	
	private RuleDao ruleDao;
	private UUID id;
	private int rank;
	private String emailAddresses;
	private String words;
	private Folder targetFolder;
	private Boolean isActive;
	private Search searcher;
	
	public RuleImpl(UUID id, int rank, String emailAddresses, String words, Folder targetFolder, boolean active) {
		this.id = id;
		this.rank = rank;
		this.emailAddresses = emailAddresses;
		this.words = words;
		this.targetFolder = targetFolder;
		this.isActive = active;
	}
	
	protected void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
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
	public void apply(Email email) {
		if (!isActive) return;
		// logic which tells the email to move to another folder
		String[] splitEmailAddresses = emailAddresses.split(";");
		boolean match = false;
		
		for(String s: splitEmailAddresses) {
			searcher = new SearchImpl(email.getFrom(), s.trim());
			if (searcher.isMatch()) {
				match = true;
				break;
			}
		}
		
		String [] splitWords = words.split(" ");
		
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
		
		if (match) {
			moveEmail(email);
		}
		
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
		ruleDao.save(UUID.randomUUID(), new Integer(rank).toString(), getEmailAddresses(), getWords(), targetFolder.getPath(), isActive.toString(), 
				CECConfigurator.getReference().get("RuleFolder"));
	}

	@Override
	public void delete() {
		ruleDao.delete(CECConfigurator.getReference().get("RuleFolder"), id);
		
	}

}
