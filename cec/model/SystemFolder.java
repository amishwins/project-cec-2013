package cec.model;

import exceptions.CannotDeleteSystemFolderException;

/**
 * Class which represents a folder which can not be deleted. A runtime 
 * object of this class is created for all specified system folders.
 *
 */
public class SystemFolder extends Folder {

	public SystemFolder(String path) {
		super(path);
	}

	/* (non-Javadoc)
	 * If the delete is called on a system folder, it will throw an exception
	 * @see cec.model.Folder#delete()
	 */
	@Override
	public void delete() {		
		handleSystemDelete();
	}

	protected void handleSystemDelete() {
		throw new CannotDeleteSystemFolderException();
	}

}
