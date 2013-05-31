package cec.persistence;

import java.util.*;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileFilter;
import exceptions.FolderAlreadyExistsException;
import exceptions.StackTrace;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.filefilter.FileFileFilter;


/**
 * 
 * FolderDaoImpl class provides the concrete implementation for the life-cycle 
 * methods for the folder domain object.
 * 
 */
public class FolderDaoImpl implements FolderDao {
	
	static Logger logger = Logger.getLogger(FolderDaoImpl.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( FolderDaoImpl.class.getPackage().getName() ) );
    }

	
	
	 /**
     * It loads all the sub folders under the given folder specified by the 
     * argument folderName.
     *
     * @param folderName the folder
     * @return the Iterable of String paths.
     */
	public Iterable<String> loadSubFolders(String folderName) {
		List<String> subFoldersPath = new ArrayList<String>();
		List<File> subFolders = new ArrayList<File>();
		subFolders = getSubFoldersRecursively(new File(folderName));
		for (File subFolder : subFolders) {
			subFoldersPath.add(subFolder.getPath().replace('\\', '/'));
			logger.info("SubFolder(s) under Folder name "+folderName + " are  : " + subFolder.getPath());
		}
		return subFoldersPath;
	}

	/**
     * It deletes the folder whose path is given by the argument folderPath.
     * It deletes each folder forcefully even if it contains files underneath it.
     * 
     *
     * @param folderPath the folder path
     */
	public void delete(String folderPath) {
		FileDeleteStrategy file = FileDeleteStrategy.FORCE;
		try {

			file.delete(new File(folderPath));
		} catch (Exception fileDeleteException) {
			logger.severe(StackTrace.asString(fileDeleteException));
		}

	}
	 /**
     * Creates  a new sub folder with name specified by argument newChildFolderName
     * under the parent folder specified by argument parentFolder.
     *
     * it throws FolderAlreadyExistsException if folder folder to be created already
     * exists at specified location.
     * 
     * @param parentFolder the parent folder
     * @param newChildFolderName the new child folder name
     * 
     * @throws FolderAlreadyExistsException
     */
	public void createSubFolder(String parentFolder, String newChildFolderName) {
		File newFolder = new File(parentFolder + "/" + newChildFolderName);
		if (newFolder.exists()) {
			throw new FolderAlreadyExistsException();
		}
		try {
			newFolder.mkdir();
		} catch (Exception fileCreationException) {
			logger.severe(StackTrace.asString(fileCreationException));
		}
	}

	/**
	 * It returns the List of sub folders under a given folder.
	 * argument folder gives the folder name under which to look 
	 * for sub folders.
	 *
	 * @param folder the folder
	 * @return the sub folders recursively
	 */
	private List<File> getSubFoldersRecursively(File folder) {
		List<File> subFolders = Arrays.asList(folder
				.listFiles(new FileFilter() {
					public boolean accept(File file) {
						return file.isDirectory();
					}
				}));
		subFolders = new ArrayList<File>(subFolders);
		List<File> innerFoldersList = new ArrayList<File>();
		for (File subFolder : subFolders) {
			innerFoldersList.addAll(getSubFoldersRecursively(subFolder));
		}
		subFolders.addAll(innerFoldersList);
		return subFolders;

	}

	/**
     * It returns the equivalent low level representation of email object under given 
     *  folder specified by the argument folder. It basically returns data pertaining 
     *  to all the emails under a given folder in the form of a collection of 
     *  Map<String,String>.
     * 
     * @param folder the folder
     * @return the iterable
     */
	public Iterable<Map<String, String>> loadEmails(String folder) {
		Collection<Map<String, String>> listOfEmails = new ArrayList<>();
		Map<String, String> email;
		EmailDao emailDao = new EmailXMLDao();

		String[] xmlFilesName = getFileNames(folder);
		for (String xmlFileName : xmlFilesName) {
			email = emailDao.loadEmail(folder, xmlFileName);// read(folder,
															// xmlFileName);
			listOfEmails.add(email);
		}
		return listOfEmails;
	}

	/**
	 * It returns only the file names under a given directory specified by argument 
	 * dir.
	 *
	 * @param dir the dir
	 * @return the file names
	 */
	private String[] getFileNames(String dir) {
		File folder = new File(dir);
		String[] xmlFiles = folder.list(FileFileFilter.FILE);
		return xmlFiles;
	}
}
