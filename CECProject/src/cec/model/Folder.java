/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import cec.persistence.FolderDao;
import cec.persistence.FolderDaoFactory;

public abstract class Folder {
	
	private String name;
	private String path;
	private Collection<Email> emailsInFolder;
	
    public Folder(String path) {
        this.path = path;
        this.name = extractName(path);
    }
    	
    public abstract void delete();
	
	public Iterable<Email> loadEmails(String folder) {
		emailsInFolder = new LinkedList<Email>();
		FolderDao folderDao = FolderDaoFactory.getFolderDaoInstance();
		Iterable<Map<String,String>> emailsData = folderDao.loadEmails(folder);
		
		for(Map<String,String> emailData: emailsData) {
			EmailBuilder emailBuilder = new EmailBuilder();
			Email email = emailBuilder.withTo(emailData.get("To"))
				.withCC(emailData.get("CC"))
				.withSubject(emailData.get("Subject"))
				.withBody(emailData.get("Body"))
				.withLastModifiedTime(emailData.get("LastAccessedTime"))
				.build();
			emailsInFolder.add(email);
		}	
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
