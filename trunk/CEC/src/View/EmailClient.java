/**
 * @author Deyvid William / Created 07-May-13
 */

package View;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


public class EmailClient extends JFrame{
    
    public EmailClient(String title){
        
        super(title);
        
        //Layout Manager
        setLayout(new BorderLayout());
        
        //Swing Components
        
            //Jtree >
            JTree FolderList = new JTree();

            FolderList.setPreferredSize(new java.awt.Dimension(200, 250));
            FolderList.setRootVisible(false);

            DefaultMutableTreeNode root   = new DefaultMutableTreeNode("Root");
            DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("Inbox");
            root.add(child1);
            DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("Outbox");
            root.add(child2);
            FolderList.setModel(new DefaultTreeModel(root));

        
        //Adding components to the Panel
        Container c = getContentPane();
        c.add(FolderList,BorderLayout.LINE_START);
        
    }   
    
}