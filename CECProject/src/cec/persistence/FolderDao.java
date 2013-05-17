/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.persistence;
import java.util.*;

public interface FolderDao {
    
    public Iterable<Map<String,String>> loadEmails(String dir);
    //public Iterable<List<String>> loadFolders();
}
 