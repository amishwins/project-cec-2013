package cec.model;

/**
 * FolderFactory class provides a static factory method to build Folder objects.
 * This class checks with the system configurator if the folder to be created is
 * a System Folder or a User Folder, and then return the correct type.
 * 
 */
public class RuleSetFactory {

	/**
	 * @param path
	 *            the string path to the folder on the file system for which a
	 *            Folder object is to be returned
	 * @return Folder either a SystemFolder or a UserFolder
	 */
	public static RuleSet getRuleSetInstance() {
		return new RuleSetImpl();
	}
}
