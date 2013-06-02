/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.model;

import cec.persistence.FolderDao;
import cec.persistence.FolderDaoFactory;

/**
 * Folder model object. A runtime folder object represents a physical folder from the file system
 *
 */
public abstract class Folder {
	
	private String path;
	protected FolderDao folderDao;
	
    public Folder(String path) {
        this.path = path;
        setFolderDao(FolderDaoFactory.getFolderDaoInstance());
    }

	/**
	 * @return the path to the file object relative to the system emails folder
	 */
	public String getPath() {
		return path;
	}
	
    protected void setFolderDao(FolderDao folderDao){
    	this.folderDao = folderDao;    	
    }
    
    /**
     * Abstract delete method to be implemented by the children of this class
     */
    public abstract void delete();
    
    /**
     * Create a subfolder in the file system underneath this folder 
     * 
     * @param newFolderName the foldername which is to be created under the current folder
     */
    public abstract void createSubFolder(String newFolderName);
	
	/**
	 * @return the collection of emails which are currently in this folder
	 */
	public abstract Iterable<Email> loadEmails();
	
	public abstract Iterable<Email> searchEmails(String searchString, Folder folderToSearchIn);
	
	public abstract Iterable<Meeting> loadMeetings();	
		
}
