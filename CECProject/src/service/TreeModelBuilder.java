/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author a_gala
 */
public class TreeModelBuilder {
    private DefaultTreeModel model;

    public TreeModelBuilder(DefaultMutableTreeNode root) {
        model = new DefaultTreeModel(root);
    }
    
    public DefaultTreeModel buildTreeNodeFromFileList(List<File> listOfFiles) {
        
        for(File file: listOfFiles) {
            buildTreeFromString(file.getPath());
        }
        
        return model;
    }
    
    private void buildTreeFromString(final String str) {
        // Fetch the root node
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        // Split the string around the delimiter
        String [] strings = str.split("\\\\");

        // Create a node object to use for traversing down the tree as it 
        // is being created
        DefaultMutableTreeNode node = root;

        // Iterate of the string array
        for (String s: strings) {
            // Look for the index of a node at the current level that
            // has a value equal to the current string
            int index = childIndex(node, s);

            // Index less than 0, this is a new node not currently present on the tree
            if (index < 0) {
                // Add the new node
                DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(s);
                node.insert(newChild, node.getChildCount());
                node = newChild;
            }
            // Else, existing node, skip to the next string
            else {
                node = (DefaultMutableTreeNode) node.getChildAt(index);
            }
        }
    }

    /**
     * Returns the index of a child of a given node, provided its string value.
     * 
     * @param node The node to search its children
     * @param childValue The value of the child to compare with
     * @return The index
     */
    private int childIndex(final DefaultMutableTreeNode node, final String childValue) {
        Enumeration<DefaultMutableTreeNode> children = node.children();
        DefaultMutableTreeNode child = null;
        int index = -1;

        while (children.hasMoreElements() && index < 0) {
            child = children.nextElement();

            if (child.getUserObject() != null && childValue.equals(child.getUserObject())) {
                index = node.getIndex(child);
            }
        }

        return index;
    }
}
