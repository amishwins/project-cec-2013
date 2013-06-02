package cec.model;

import java.util.ArrayList;
import java.util.List;

import cec.config.CECConfigurator;
import cec.persistence.FolderDao;
import cec.persistence.FolderDaoFactory;

/**
 * HierarchyImpl is used to create the list of folders which is used to
 * populate the tree hierarchy. The tree hierarchy is used by the JTree
 * component, and also when we move emails. 
 *
 */
public class HierarchyImpl implements Hierarchy {
    private List<Folder> hierarchy;
    private FolderDao folderDao;
    
    public HierarchyImpl (){
    	hierarchy = new ArrayList<Folder>();
    	folderDao = FolderDaoFactory.getFolderDaoInstance();
    }

    @Override
	public Iterable<Folder> loadHierarchy(){
        List<Folder> systemFolders = getSystemFolders();
        hierarchy.addAll(systemFolders);
        for(Folder systemFolder:systemFolders){
        	getSubFoldersFromSystemFolder(systemFolder);
        }
    	
        return hierarchy;
    }
       
    /**
     * This method is protected so that we are able to test better. The system
     * folders are always returned in a certain order. However, at any point of 
     * time there may be many user folders. Thus we allow subclasses to override
     * this method for testing.
     * 
     * @param systemFolder the folder object for which the subfolders are requested
     */
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
    	systemFolders.add(FolderFactory.getFolder(config.get("meetings")));
       	return systemFolders;
    }
}
   