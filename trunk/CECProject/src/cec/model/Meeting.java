package cec.model;

import java.util.UUID;

/**
 * The Interface Meeting exposes the life cycle methods 
 * of an meeting that service layer should call.
 * 
 * Service layer classes are coupled to Model layer 
 * through this interface. 
 *
 * 
 */
public interface Meeting extends Comparable<Meeting> {
   
    
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
     * Gets the To addresses(recipients) of an meeting .
     *
     * @return the to
     */
    public String getAttendees();
    
    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public String getStartTime();
    
    /**
     * Gets the end time.
     *
     * @return the end time
     */
    public String getEndTime();
    
    /**
     * Gets the start date.
     *
     * @return the start date
     */
    public String getStartDate();
    
    /**
     * Gets the end date.
     *
     * @return the end date
     */
    public String getEndDate();
    
    /**
     * Gets the place.
     *
     * @return the place
     */
    public String getPlace();
    
    /**
     * Gets the subject of an meeting.
     *
     * @return the subject
     */
    public String getSubject();
    
    /**
     * Gets the body part of an meeting.
     *
     * @return the body
     */
    public String getBody();
    
    /**
     * Gets the sent time of an meeting.
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
	 
    /**
     * Sends the meeting object.
     */
    public void send();

    /**
     * Save a meeting after accepting the invitation
     */
    public void saveAfterAccept();
    
    
       
    /**
     * Deletes the meeting object from the System.
     */
    public void delete();
    
}
