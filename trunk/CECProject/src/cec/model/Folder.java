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

import cec.persistence.FolderDao;
import cec.persistence.FolderDaoFactory;

public abstract class Folder {
	
	private String name;
	private String path;
	private List<Email> emailsInFolder;
	
    public Folder(String path) {
        this.path = path;
        this.name = extractName(path);
    }
    	
    public abstract void delete();
	
	public Iterable<Email> loadEmails() {
		emailsInFolder = new LinkedList<Email>();
		FolderDao folderDao = FolderDaoFactory.getFolderDaoInstance();
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
       
   
	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}
	
    private String extractName(String path) {
        return path;
    }
}
