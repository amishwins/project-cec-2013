package cec.model;

import java.util.UUID;

/**
 * The Interface Email exposes the life cycle methods 
 * of an email that service layer should call.
 * 
 * Service layer classes are coupled to Model layer 
 * through this interface. 
 *
 * 
 */
public interface Email extends Comparable<Email>, Searchable {
    
    /**
     * Sends the email object to its destination address.
     */
    public void send();
    
    /**
     * Saves the email to draft folder.
     */
    public void saveToDraftFolder();
    
    /**
     * Deletes the email from the System.
     */
    public void delete();
    
    /**
     * Moves the email from the source folder to destination 
     * folder.
     *
     * @param destination folder of an email.
     */
    public void move(Folder destDir);
    
    /**
     * Gets the id.
     *
     * @return the id
     */
    public UUID getId();
    
    /**
     * Gets the from.
     *
     * @return the from
     */
    public String getFrom();
    
    /**
     * Gets the To addresses(recipients) of an email .
     *
     * @return the to
     */
    public String getTo();
    
    /**
     * Gets the cc field of an email .
     *
     * @return the cc 
     */
    public String getCC();
    
    /**
     * Gets the subject of an email.
     *
     * @return the subject
     */
    public String getSubject();
    
    /**
     * Gets the body part of an email.
     *
     * @return the body
     */
    public String getBody();
    
    /**
     * Gets the sent time of an email.
     *
     * @return the sent time
     */
    public String getSentTime();
    
    /**
     * Gets the last modified time.
     *
     * @return the last modified time
     */
    public String getLastModifiedTime();
    
    /**
     * Gets the parent folder.
     *
     * @return the parent folder
     */
    public Folder getParentFolder();
	
	/**
	 * returns the last modified time value in a more readable format. 
	 * Converts the LastModifiedTime field value from "yyyy.MM.dd_'At'_HH.mm.ss.SSS" format to 
	 * "EEE, MMM d, yyyy"
	 * 	 *
	 * @return the last modified time nicely formatted
	 */
    
	public String getLastModifiedTimeNicelyFormatted();
}
