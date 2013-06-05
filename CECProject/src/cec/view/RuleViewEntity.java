package cec.view;

import java.util.UUID;

public class RuleViewEntity {

	// private class attributes
	private UUID id;
	private String emailAddresses = "";
	private String words = "";
	private String folderPath = "";
	private Boolean isActive = false;
	
	public String getEmailAddresses() {
		return emailAddresses;
	}

	public String getWords() {
		return words;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public UUID getID() {
		return id;
	}
	
	public void setWords(String words) {
		this.words = words;
	}
	public void setEmailAddresses(String emailAddresses) {
		this.emailAddresses = emailAddresses;
	}
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	public void setId(UUID id) {
		this.id = id;
	}

}
