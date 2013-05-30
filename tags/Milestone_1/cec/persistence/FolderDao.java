package cec.persistence;
import java.util.*;


/**
 * The Interface FolderDao exposes all the life-cycle methods for the folder domain object.
 *  classes implementing this class provide the concrete implementation for the 
 *  life-cycle methods. 
 */
public interface FolderDao {
    
    /**
     * It loads all email objects from the folder specified by argument folder.
     *
     * @param folder the folder
     * @return the iterable
     */
    public Iterable<Map<String,String>> loadEmails(String folder);
    
    /**
     * It loads all the sub folders under the given folder specified by the 
     * argument parentFolder.
     *
     * @param parentFolder the parent folder
     * @return the iterable
     */
    public Iterable<String> loadSubFolders(String parentFolder);
    
    /**
     * It deletes the given folder whose path is given by the argument folderPath.
     *
     * @param folderPath the folder path
     */
    public void delete(String folderPath);
    
    /**
     * Creates  a new sub folder with name specified by argument newChildFolderName
     * under the parent folder specified by argument parentFolder.
     *
     * @param parentFolder the parent folder
     * @param newChildFolderName the new child folder name
     */
    public void createSubFolder(String parentFolder, String newChildFolderName);
}
 