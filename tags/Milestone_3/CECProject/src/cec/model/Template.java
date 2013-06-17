package cec.model;

/**
 * The Interface Template exposes the lifecycle methods 
 * of a template that service layer should call.
 * 
 * Service layer classes are coupled to Model layer 
 * through this interface. 
 *
 * 
 */
public interface Template extends Comparable<Template> {
    
    /**
     * Saves the template
     */
    public void save();
     
    /**
     * Deletes the template from the System.
     */
    public void delete();
    
    /**
     * Gets the name of the template
     *
     * @return the from
     */
    public String getName();
    
    /**
     * Gets the To addresses(recipients) of the template .
     *
     * @return the to
     */
    public String getTo();
    
    /**
     * Gets the cc field of the template .
     *
     * @return the cc 
     */
    public String getCC();
    
    /**
     * Gets the subject of the template .
     *
     * @return the subject
     */
    public String getSubject();
    
    /**
     * Gets the body part of the template.
     *
     * @return the body
     */
    public String getBody();   
    
}
