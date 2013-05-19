package cec.persistence;
import java.util.*;

public interface FolderDao {
    
    public Iterable<Map<String,String>> loadEmails(String folder);
    public Iterable<String> loadSubFolders(String parentFolder);
    public void delete(String folderPath);
    public void createSubFolder(String parentFolder, String newChildFolderName);
}
 