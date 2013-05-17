/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.service;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import cec.model.Hierarchy;

/**
 *
 * @author DW
 */
public class Controller {

    public void getFolderListTree(JTree a){
         
       //  a.setModel(new DefaultTreeModel(Folder.getFolderList()));
         a.setRootVisible(false);
    }
    
    public DefaultMutableTreeNode getFolderModel() {
        Hierarchy hierarchy = new Hierarchy();
        DefaultMutableTreeNode root = processHierarchy(hierarchy.getHierarchy());
        return root;
    }

    // logic taken from: http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-JTree.html
    private DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
        DefaultMutableTreeNode child;
        for(int i=1; i<hierarchy.length; i++) {
            Object nodeSpecifier = hierarchy[i];
            if (nodeSpecifier instanceof Object[])  // Ie node with children
                child = processHierarchy((Object[])nodeSpecifier);
            else
                child = new DefaultMutableTreeNode(nodeSpecifier); // Ie Leaf
          node.add(child);
        }
        return(node);
    }    
}
