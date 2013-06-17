package cec.view;

import java.util.UUID;

/**
 * Representation of the Rule object in the View Layer
 * 
 * */
public class RuleViewEntity {
	
	/** The id. */
	private UUID id;
	
	/** The rank. */
	private int rank;
	
	/** The emailAddresses used to filter */
	private String emailAddresses = "";

	/** The words used to filter */	
	private String words = "";
	
	/** The folderPath that contains the destiny for emails that match the filters*/		
	private String folderPath = "";
	
	/**
	 * Gets the emailAddresses.
	 * 
	 * @return the emailAddresses
	 */
	public String getEmailAddresses() {
		return emailAddresses;
	}

	/**
	 * Gets the words.
	 * 
	 * @return the words
	 */
	public String getWords() {
		return words;
	}

	/**
	 * Gets the folderPath.
	 * 
	 * @return the folderPath
	 */
	public String getFolderPath() {
		return folderPath;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public UUID getID() {
		return id;
	}

	/**
	 * Gets the rank.
	 * 
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Sets the words.
	 * 
	 * @param words
	 *            the new words
	 */
	public void setWords(String words) {
		this.words = words;
	}

	/**
	 * Sets the emailAddresses.
	 * 
	 * @param emailAddresses
	 *            the new emailAddresses
	 */
	public void setEmailAddresses(String emailAddresses) {
		this.emailAddresses = emailAddresses;
	}
	
	/**
	 * Sets the folderPath.
	 * 
	 * @param folderPath
	 *            the new folderPath
	 */	
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */	
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Sets the rank.
	 * 
	 * @param rank
	 *            the new rank
	 */	
	public void setRank(int rank){
		this.rank = rank;
	}

}
