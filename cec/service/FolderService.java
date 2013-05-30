package cec.service;

import java.util.ArrayList;
import java.util.List;

import cec.model.Email;
import cec.model.Folder;
import cec.model.FolderFactory;
import cec.model.Hierarchy;
import cec.view.EmailViewEntity;
import cec.model.HierarchyImpl;

/**
 * Class that represents all the actions/methods that can be performed against a Folder from
 * the Presentation Layer (cec.view) such as Creating/Deleting Subfolders and also
 * retrieves information from model/persistence Layers to build
 * the JTree <code>folders</code> and JTable <code>emailTable</code>. 
 */

public class FolderService {
	
	private Folder folder;

	/** Load the hierarchy of folders to build Folder Tree */
	public Iterable<String> loadHierarchy(){
		List<String> hierarchy = new ArrayList<String>();
		Hierarchy cECHierarchy = new HierarchyImpl();
		Iterable<Folder> folders = cECHierarchy.loadHierarchy();
		for(Folder folder: folders){
			hierarchy.add(folder.getPath());
		}
		return hierarchy;
	}
	
	/** Load all emails from a specific folder */
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
			emailInView.setLastModifiedTime(emailInModel.getLastModifiedTimeNicelyFormatted());
			emailInView.setSentTime(emailInModel.getSentTime());
			emailInView.setFolder(emailInModel.getParentFolder().getPath());
		
			emailListInView.add(emailInView);
		}
		return emailListInView;
	}
	
	/** Creates a subfolder once receiving the path of the parent folder and its own name */
	public void createSubFolder(String folderPath, String newSubFolderName) {
		
		
		folder = FolderFactory.getFolder(folderPath);
		folder.createSubFolder(newSubFolderName);
	}
	
	/** Delete a specific subfolder once receiving its path */
	public void delete(String folderPath){
		folder = FolderFactory.getFolder(folderPath);
		folder.delete();
	}
	
	
	
}
