package cec.model;

import cec.persistence.RuleDao;

public class RuleImpl implements Rule {
	
	private RuleDao ruleDao;
	private int rank;
	private String emailAddresses;
	private String words;
	private Folder targetFolder;
	private Boolean isActive;
	
	public RuleImpl(int rank, String emailAddresses, String words, Folder targetFolder, boolean active) {
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
		// logic which tells the email to move to another folder
		String[] splitEmailAddresses = emailAddresses.split(";");
		boolean match = false;
		
		for(String s: splitEmailAddresses) {
			if (email.getFrom().toString().toUpperCase().equals(s.toUpperCase())) {
				match = true;
			}
		}
		
		String [] splitWords = words.split(" ");
		for(String s: splitWords) {
			if (email.getSubject().toString().toUpperCase().contains(s.toUpperCase())) {
				match = true;
			}
			if (email.getBody().toString().toUpperCase().contains(s.toUpperCase())) {
				match = true;
			}
		}
		
		if (match) {
			email.move(targetFolder);
		}
		
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

}
