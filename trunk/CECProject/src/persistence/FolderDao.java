/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;
import java.io.File;
import java.util.*;
/**
 *
 * @author Pankaj Kapania
 */
public interface FolderDao {
    public Iterable<Map<String,String>> loadEmails(String dir);
    //public Iterable<List<String>> loadFolders();
}
 