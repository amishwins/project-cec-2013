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

}
