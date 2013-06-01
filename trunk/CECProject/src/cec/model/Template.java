package cec.model;

import java.util.UUID;

/**
 * The Interface Email exposes the life cycle methods 
 * of an email template that service layer should call.
 * 
 * Service layer classes are coupled to Model layer 
 * through this interface. 
 *
 * 
 */
public interface Template extends Comparable<Template> {
    
    /**
     * Sends the email object to its destination address.
     */
    public void save();
     
    /**
     * Deletes the email from the System.
     */
    public void delete();
    
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
    
}
