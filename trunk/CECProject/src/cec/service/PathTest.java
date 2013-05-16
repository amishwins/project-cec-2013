package cec.service;

import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class PathTest {
    public PathTest() {
        // Create the root node, I'm assuming that the delimited strings will have
        // different string value at index 0
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");

        // Create the tree model and add the root node to it
        DefaultTreeModel model = new DefaultTreeModel(root);

        // Create the tree with the new model
      

        
        
        // Build the tree from the various string samples
        buildTreeFromString(model, "inbox");
        buildTreeFromString(model, "draft/child-draft");
        buildTreeFromString(model, "inbox/Folder 1");
        buildTreeFromString(model, "inbox/Folder 2/Child 3");
        buildTreeFromString(model, "draft");
        

        
        JTree tree = new JTree(model);
        
        // UI
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(tree);
        f.setSize(300, 300);
        f.setLocation(200, 200);
        f.setVisible(true);
    }

    /**
     * Builds a tree from a given forward slash delimited string.
     * 
     * @param model The tree model
     * @param str The string to build the tree from
     */
    private void buildTreeFromString(final DefaultTreeModel model, final String str) {
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

    public static void main(String[] args) {
        new PathTest();
    }
}