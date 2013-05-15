/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Amish
 * http://java.dzone.com/news/taking-new-swing-tree-table-a-
 */
 class FileTreeModel2 implements TreeModel {

    private File root;

    public FileTreeModel2(File root) {
        this.root = root;
    }

    @Override
    public void addTreeModelListener(javax.swing.event.TreeModelListener l) {
        //do nothing
    }

    @Override
    public Object getChild(Object parent, int index) {
        
	FileFilter directoryFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };

        File f = (File) parent;
        return f.listFiles(directoryFilter)[index];
    }

    @Override
    public int getChildCount(Object parent) {
        File f = (File) parent;
        if (!f.isDirectory()) {
            return 0;
        } else {
            return f.list().length;
        }
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        File par = (File) parent;
        File ch = (File) child;
        return Arrays.asList(par.listFiles()).indexOf(ch);
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public boolean isLeaf(Object node) {
        File f = (File) node;
        return !f.isDirectory();
    }

    @Override
    public void removeTreeModelListener(javax.swing.event.TreeModelListener l) {
        //do nothing
    }

    @Override
    public void valueForPathChanged(javax.swing.tree.TreePath path, Object newValue) {
        //do nothing
    }

}


public class FileTreeModel implements TreeModel {
  // We specify the root directory when we create the model.
  protected File root;
  public FileTreeModel(File root) { this.root = root; }

  // The model knows how to return the root object of the tree
  public Object getRoot() { return root; }

  // Tell JTree whether an object in the tree is a leaf
  public boolean isLeaf(Object node) {  return ((File)node).isFile(); }

  // Tell JTree how many children a node has
  public int getChildCount(Object parent) {
    String[] children = ((File)parent).list();
    if (children == null) return 0;
    return children.length;
  }

  // Fetch any numbered child of a node for the JTree.
  // Our model returns File objects for all nodes in the tree.  The
  // JTree displays these by calling the File.toString() method.
  public Object getChild(Object parent, int index) {
   
 String[] children = ((File)parent).list();
    	FileFilter directoryFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
     
    File[] fchildren = ((File)parent).listFiles(directoryFilter);
    if ((fchildren == null) || (index >= fchildren.length)) return null;
    return new File((File) parent, fchildren[index].getName());
  }

  // Figure out a child's position in its parent node.
  public int getIndexOfChild(Object parent, Object child) {
    String[] children = ((File)parent).list();
    if (children == null) return -1;
    String childname = ((File)child).getName();
    for(int i = 0; i < children.length; i++) {
      if (childname.equals(children[i])) return i;
    }
    return -1;
  }
    @Override
    public void valueForPathChanged(TreePath tp, Object o) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addTreeModelListener(TreeModelListener tl) {
  //      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeTreeModelListener(TreeModelListener tl) {
  //      throw new UnsupportedOperationException("Not supported yet.");
    }
}
