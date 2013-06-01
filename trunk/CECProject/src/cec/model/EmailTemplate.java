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
public interface EmailTemplate extends Comparable<EmailTemplate> {
    
    /**
     * Sends the email object to its destination address.
     */
    public void save();
     
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
     * Gets the name of the template
     *
     * @return the from
     */
    public String getName();
    
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
    
    
    public Iterable<EmailTemplate> loadTemplates();
    
}
