package cec.model;

import exceptions.CannotDeleteSystemFolderException;

/**
 * Class which represents a folder which can not be deleted. A runtime 
 * object of this class is created for all specified system folders.
 *
 */
public class EmailsSystemFolder extends EmailsFolder {

	public EmailsSystemFolder(String path) {
		super(path);
	}

	
	@Override
	public void delete() {		
		handleSystemDelete();
	}

	protected void handleSystemDelete() {
		throw new CannotDeleteSystemFolderException();
	}

}
