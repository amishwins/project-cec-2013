/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import model.FolderImpl;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author DW
 */
public class Controller {
        
    FolderImpl Folder = new FolderImpl();
    
    public void getFolderListTree(JTree a){
         
       //  a.setModel(new DefaultTreeModel(Folder.getFolderList()));
         a.setRootVisible(false);
    }
    
}
