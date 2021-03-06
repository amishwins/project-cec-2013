package cec.model;

import cec.config.CECConfigurator;

/**
 * FolderFactory class provides a static factory method to build Folder objects.
 * This class checks with the system configurator if the folder to be created is
 * a System Folder or a User Folder, and then return the correct type.
 * 
 */
public class FolderFactory {

	/**
	 * @param path
	 *            the string path to the folder on the file system for which a
	 *            Folder object is to be returned
	 * @return Folder either a SystemFolder or a UserFolder
	 */
	public static Folder getFolder(String path) {
		// What do we do if path is empty? invalid argument?
		if (CECConfigurator.getReference().isPathForAEmailSystemFolder(path)) {
			return new EmailsSystemFolder(path);
		} else if (CECConfigurator.getReference()
				.isPathForAMeetingSystemFolder(path)) {
			return new MeetingsFolder(path);
		} else
			return new EmailsUserFolder(path);
	}
}
