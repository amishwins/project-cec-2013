
package DAO;

import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * FolderStructure generates Tree Nodes from FS
 * @author DW - Created in 10-May-2013
 */
public final class FolderStructure {
    
    DefaultMutableTreeNode root;

    public FolderStructure() {           
        root = new DefaultMutableTreeNode("root", true);
        generateStructure(root, new File("C:\\Deyvid\\Concordia 2013\\SOEN 6441\\CEC\\"));
    }

    public void generateStructure(DefaultMutableTreeNode node, File f) {
        if (f.isDirectory()) {
            
            System.out.println("DIRECTORY  -  " + f.getName());
              
            File fList[] = f.listFiles();
            for (int i = 0; i < fList.length; i++) {                
               
                if (fList[i].isDirectory()) {
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(fList[i].getName());
                    node.add(child);
                    generateStructure(child, fList[i]);
                }
            }
        }
    }
    
    public DefaultMutableTreeNode getStructure(){
        return root;
    }
}
