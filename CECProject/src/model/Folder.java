/**
 * @author Deyvid William / Created 07-May-13
 */

package model;

import DAO.FolderStructure;
import javax.swing.tree.DefaultMutableTreeNode;


public class Folder {
    
    //Class atributes - Private Variables
    private String Name;
    private String Path;
    
    public DefaultMutableTreeNode getFolderList(){
        FolderStructure a = new FolderStructure();
        return a.getStructure();
    }
}
