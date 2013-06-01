package cec.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * The Interface EmailTemplateDao exposes the lower level methods to
 * Model layer. 
 * 
 */
public interface TemplateDao {
	
	/**
	 * Saves the template object to its equivalent lower level representation( for example : a File).
	 *
	 * @param name the name of the file and template
	 * @param to the to
	 * @param cc the cc
	 * @param subject the subject
	 * @param body the body
	 */
	public void saveAsTemplate(String name, String to, String cc, String subject, String body);
	
	/**
	 * Delete the template object from the persistence layer.
	 *
	 * @param fileName the file name
	 */
	public void deleteTemplate(String fileName);
	
	/**
	 * Loads an equivalent lower level representation of an email from a specific folder.
	 * It basically loads the email field values and returns a Map of 
	 * those values. 
	 *
	 * @param folder the folder
	 * @param FileName the file name
	 * @return the map
	 */
	public Map<String, String> loadTemplate(String fileName);
	
	
	/**
	 * Loads all template files currently in the system
	 * 
	 * @return
	 */
	public Iterable<Map<String, String>> loadAllTemplates();
}
