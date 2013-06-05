package cec.model;

import java.util.UUID;

public interface Rule extends Comparable<Rule> {
	public UUID getId();
	public int getRank();
	public void setRank(int rank);
	public String getEmailAddresses();
	public String getWords();
	public Folder getTargetFolder();
	public boolean isActive();
	
	public void setEmailAddresses(String emailAddresses);
	public void setWords(String words);
	public void setTargetFolder(Folder target);
	public void activate();
	public void deactivate();
	
	public void save();
	public void update();
	public void delete();

	public boolean apply(Email email);
	
	// apply to all emails
	public void apply();
}
