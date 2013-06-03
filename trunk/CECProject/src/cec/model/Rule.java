package cec.model;

public interface Rule {
	
	public int getRank();
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
	public void delete();
	
	public void apply(Email email);
}
