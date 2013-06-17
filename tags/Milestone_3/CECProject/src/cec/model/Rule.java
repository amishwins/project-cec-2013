package cec.model;

import java.util.UUID;



/**
 * The Interface Rule.
 * Rules defines the movement of emails from
 * one folder to another folder on certain events.
 */
public interface Rule extends Comparable<Rule> {
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public UUID getId();
	
	/**
	 * Gets the rank.
	 *
	 * @return the rank
	 */
	public int getRank();
	
	/**
	 * Sets the rank.
	 *
	 * @param rank the new rank
	 */
	public void setRank(int rank);
	
	/**
	 * Gets the email addresses.
	 *
	 * @return the email addresses
	 */
	public String getEmailAddresses();
	
	/**
	 * Gets the words.
	 *
	 * @return the words
	 */
	public String getWords();
	
	/**
	 * Gets the target folder.
	 *
	 * @return the target folder
	 */
	public Folder getTargetFolder();
	
	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public boolean isActive();
	
	/**
	 * Sets the email addresses.
	 *
	 * @param emailAddresses the new email addresses
	 */
	public void setEmailAddresses(String emailAddresses);
	
	/**
	 * Sets the words.
	 *
	 * @param words the new words
	 */
	public void setWords(String words);
	
	/**
	 * Sets the target folder.
	 *
	 * @param target the new target folder
	 */
	public void setTargetFolder(Folder target);
	
	/**
	 * Activate.
	 */
	public void activate();
	
	/**
	 * Deactivate.
	 */
	public void deactivate();
	
	/**
	 * Save.
	 */
	public void save();
	
	/**
	 * Update.
	 */
	public void update();
	
	/**
	 * Delete.
	 */
	public void delete();

	/**
	 * Apply.
	 *
	 * @param email the email
	 * @return true, if successful
	 */
	public boolean apply(Email email);
	
	/**
	 * Apply.
	 */
	public void apply();
}
