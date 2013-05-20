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

public abstract class Folder {
	
	private String path;
	private List<Email> emailsInFolder;
	protected FolderDao folderDao;
	
    public Folder(String path) {
        this.path = path;
        setFolderDao(FolderDaoFactory.getFolderDaoInstance());
    }

	public String getPath() {
		return path;
	}
	
    protected void setFolderDao(FolderDao folderDao){
    	this.folderDao = folderDao;    	
    }
    
    public abstract void delete();
    
    public void createSubFolder(String newFolderName){
    	if (getPath().equals(CECConfigurator.getReference().get("emails"))) {
    		throw new RootFolderSubfolderCreationException();
    	}
    	folderDao.createSubFolder(getPath(), newFolderName);
    }
	
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
