package cec.persistence;

import java.util.Map;
import java.util.UUID;

/**
 * The Interface RuleDao exposes the lower level methods to
 * Model layer. 
 * 
 */
public interface RuleDao {
	
	/**
	 * 
	 * Saves each rule object to its equivalent lower level representation( for example : a File).
	 * Postcondition: Each rule will have its rank and persisted on the file system.
	 * 
	 */
	public void save(UUID id, String sender, String keyword,
			String tartgetFolder, String status, String pathToSaveRuleFile);
	
	/**
	 * Invariant: rule ID will not be changed even after the modification of the other fields.
	 * Saves each rule object with latest changes to its equivalent lower level representation( for example : a File).
	 * Postconditions: Rule changes will be persisted on the system.
	 * 
	 */
	public void update(UUID id, String rank, String sender, String keyword,
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
