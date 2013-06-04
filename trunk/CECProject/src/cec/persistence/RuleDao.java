package cec.persistence;

import java.util.Map;
import java.util.UUID;

/**
 * The Interface EmailDao exposes the lower level methods to
 * Model layer. 
 * 
 */
public interface RuleDao {
	
	/**
	 * Saves each email object to its equivalent lower level representation( for example : a File).
	 *
	 * @param id the id
	 * @param from the from
	 * @param to the to
	 * @param cc the cc
	 * @param subject the subject
	 * @param body the body
	 * @param lastModifiedTime the last modified time
	 * @param sentTime the sent time
	 * @param location the location
	 */
	public void save(UUID id, String sender, String keyword,
			String tartgetFolder, String status, String pathToSaveRuleFile);
	
	/**
	 * Delete each email object from the persistence layer.
	 *
	 * @param path the path
	 * @param fileName the file name
	 */
	public void delete(String path, UUID fileName);
	

	/**
	 * Loads an equivalent lower level representation of an email from a specific folder.
	 * It basically loads the email field values and returns a Map of 
	 * those values. 
	 *
	 * @param folder the folder
	 * @param FileName the file name
	 * @return the map
	 */
	public Map<String, String> loadRule(String folder, String ruleXmlFileName);
	
	public Iterable<Map<String, String>> loadAllRules(String pathToRuleFolder);
	
}
