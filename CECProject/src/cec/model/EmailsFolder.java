/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cec.config.CECConfigurator;
import exceptions.RootFolderSubfolderCreationException;

/**
 * Folder model object. A runtime folder object represents a physical folder from the file system
 *
 */
public abstract class EmailsFolder extends Folder {
	
	private List<Email> emailsInFolder;
	public EmailsFolder(String path) {
        super(path);
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
   @Override
   public void createSubFolder(String newFolderName){
    	if (getPath().equals(CECConfigurator.getReference().get("emails"))) {
    		throw new RootFolderSubfolderCreationException();
    	}
    	folderDao.createSubFolder(getPath(), newFolderName);
    }
    
   @Override 
   public Iterable<Meeting> loadMeetings(){
	   throw new UnsupportedOperationException();
   }
	
	/**
	 * @return the collection of emails which are currently in this folder
	 */
   
   @Override
   public Iterable<Email> loadEmails() {
		emailsInFolder = new LinkedList<Email>();
		Iterable<Map<String,String>> emailsData = folderDao.loadEmails(getPath());
		
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
   
   private Iterable<Folder>loadAllSubFolderUnderSearchableFolder(Folder searchableFolder){
	   List<Folder> foldersToSearchIn = new ArrayList<>();
	   Iterable<String> foldersPath = folderDao.loadSubFolders(searchableFolder.getPath());
	   for(String folderPath: foldersPath){
		   Folder newFolder = FolderFactory.getFolder(folderPath);
		   foldersToSearchIn.add(newFolder);
	   }
	   return foldersToSearchIn;
   }
   
   public Iterable<Email> searchEmails(String toFind, Folder searchableFolder) {
	   Iterable<Folder> folderList = loadAllSubFolderUnderSearchableFolder(searchableFolder);
	   emailsInFolder = new LinkedList<Email>();
	   for(Folder folder: folderList){
		   Iterable<Map<String,String>> emailsData = folderDao.loadEmails(folder.getPath());
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
				
				if(email.isMatch(toFind))
					emailsInFolder.add(email);
			}	
	   }
		Collections.sort(emailsInFolder);
		return emailsInFolder;
	}
	
}
