package cec.persistence;

import java.util.Map;
import java.util.UUID;

/**
 * The Interface EmailDao exposes the lower level methods to
 * Model layer. 
 * 
 */
public interface EmailDao {
	
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
	public void save(UUID id, String from, String to, String cc,
			String subject, String body, String lastModifiedTime,
			String sentTime, String location);
	
	/**
	 * Delete each email object from the persistence layer.
	 *
	 * @param path the path
	 * @param fileName the file name
	 */
	public void delete(String path, UUID fileName);
	
	/**
	 * Moves an email from source folder to some different folder.
	 * Other folder or destination folder is specified by an argument desDir.
	 *
	 * @param fileName the file name
	 * @param srcDir the source  directory
	 * @param destDir the destination  directory
	 */
	public void move(UUID fileName, String srcDir, String destDir);
	
	/**
	 * Loads an equivalent lower level representation of an email from a specific folder.
	 * It basically loads the email field values and returns a Map of 
	 * those values. 
	 *
	 * @param folder the folder
	 * @param FileName the file name
	 * @return the map
	 */
	public Map<String, String> loadEmail(String folder, String FileName);
}
