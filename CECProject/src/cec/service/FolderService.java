package cec.service;

import java.util.ArrayList;
import java.util.List;

import cec.model.Email;
import cec.model.Folder;
import cec.model.FolderFactory;
import cec.view.EmailViewEntity;

public class FolderService {
	
	static Folder folder;
	
	public Iterable<EmailViewEntity> loadEmails(String folderPath){
		List<EmailViewEntity> emailListInView = new ArrayList<EmailViewEntity>();
		folder = FolderFactory.getFolder(folderPath);
		Iterable<Email> emailListInModel = folder.loadEmails();
		for(Email emailInModel: emailListInModel){
			EmailViewEntity emailInView = new EmailViewEntity();
			emailInView.setId(emailInModel.getId());
			emailInView.setFrom(emailInModel.getFrom());
			emailInView.setTo(emailInModel.getTo());
			emailInView.setCC(emailInModel.getCC());
			emailInView.setSubject(emailInModel.getSubject());
			emailInView.setBody(emailInModel.getBody());
			emailInView.setLastModifiedTime(emailInModel.getLastModifiedTime());
			emailInView.setSentTime(emailInModel.getSentTime());
			emailInView.setFolder(emailInModel.getParentFolder().getPath());
		
			emailListInView.add(emailInView);
		}
		return emailListInView;
	}
	
	

}
