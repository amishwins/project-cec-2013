package cec.model;

/**
 * Class which represents a folder which has been created by a user. A runtime 
 * object of this class is created for all user provided folder names. The 
 * delete method communicates with the persistence layer
 *
 */
public class EmailsUserFolder extends EmailsFolder {

	public EmailsUserFolder(String path) {
		super(path);
	}

	@Override
	public void delete() {
		// okay - do the delete
       folderDao.delete(getPath());
	}

}
