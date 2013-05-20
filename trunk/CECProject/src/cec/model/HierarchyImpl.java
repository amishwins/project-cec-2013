package cec.model;

import java.util.ArrayList;
import java.util.List;

import cec.config.CECConfigurator;
import cec.persistence.FolderDao;
import cec.persistence.FolderDaoFactory;

public class HierarchyImpl implements Hierarchy {
    private List<Folder> hierarchy;
    private FolderDao folderDao;
    
    public HierarchyImpl (){
    	hierarchy = new ArrayList<Folder>();
    	folderDao = FolderDaoFactory.getFolderDaoInstance();
    }

    @Override
	public Iterable<Folder> loadHierarchy(){
        List<Folder> systemFolders= getSystemFolders();
        hierarchy.addAll(systemFolders);
        for(Folder systemFolder:systemFolders){
        	getSubFoldersFromSystemFolder(systemFolder);
        }
    	
        return hierarchy;
    }
   
    protected void getSubFoldersFromSystemFolder(Folder systemFolder){
    	Iterable<String> subFolders = folderDao.loadSubFolders(systemFolder.getPath());
    	for(String subfolder : subFolders){
    		hierarchy.add(FolderFactory.getFolder(subfolder));
    	}
    }
    private List<Folder> getSystemFolders(){
    	CECConfigurator config = CECConfigurator.getReference();
    	List<Folder> systemFolders = new ArrayList<Folder>();
    	systemFolders.add(FolderFactory.getFolder(config.get("Inbox")));
    	systemFolders.add(FolderFactory.getFolder(config.get("Drafts")));
    	systemFolders.add(FolderFactory.getFolder(config.get("Outbox")));
    	systemFolders.add(FolderFactory.getFolder(config.get("Sent")));
    	return systemFolders;
    }
}
   