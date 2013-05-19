/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.service;

import java.util.Enumeration;
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
    
    public DefaultTreeModel buildTreeNodeFromFileList(Iterable<String> hierarchy) {
        
        for(String folder: hierarchy) {
            buildTreeFromFolders(folder);
        }
        return model;
    }
    
    private void buildTreeFromFolders(final String str) {
        // Fetch the root node
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        // Split the string around the delimiter
        String [] strings = str.split("/");

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
        @SuppressWarnings("unchecked")
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
