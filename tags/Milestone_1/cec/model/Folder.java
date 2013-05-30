/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cec.config.CECConfigurator;
import cec.persistence.FolderDao;
import cec.persistence.FolderDaoFactory;
import exceptions.RootFolderSubfolderCreationException;

/**
 * Folder model object. A runtime folder object represents a physical folder from the file system
 *
 */
public abstract class Folder {
	
	private String path;
	private List<Email> emailsInFolder;
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
    public void createSubFolder(String newFolderName){
    	if (getPath().equals(CECConfigurator.getReference().get("emails"))) {
    		throw new RootFolderSubfolderCreationException();
    	}
    	folderDao.createSubFolder(getPath(), newFolderName);
    }
	
	/**
	 * @return the collection of emails which are currently in this folder
	 */
	public Iterable<Email> loadEmails() {
		emailsInFolder = new LinkedList<Email>();
		Iterable<Map<String,String>> emailsData = folderDao.loadEmails(path);
		
		for(Map<String,String> emailData: emailsData) {
			EmailBuilder emailBuilder = new EmailBuilder();
			Email email = emailBuilder.withId(UUID.fromString(emailData.get("Id")))
				.withFrom(emailData.get("From"))
				.withTo(emailData.get("To"))
				.withCC(emailData.get("CC"))
				.withSubject(emailData.get("Subject"))
				.withBody(emailData.get("Body"))
				.withLastModifiedTime(emailData.get("LastModifiedTime"))
				.withSentTime(emailData.get("SentTime"))
				.withParentFolder(FolderFactory.getFolder(emailData.get("ParentFolder")))
				.build();
			emailsInFolder.add(email);
		}	
		Collections.sort(emailsInFolder);
		return emailsInFolder;
	}
	
}
