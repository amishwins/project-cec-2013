/**
 * @author Deyvid William / Created 07-May-13
 */

package View;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneLayout;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


public class EmailClient extends JFrame{
    
    public EmailClient(String title){
        
        super(title);
        
        //Layout Manager
        setSize(760, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //Menu
        JMenuBar jMenuBar1 = new JMenuBar();
        setJMenuBar(jMenuBar1);
        JMenu file = new JMenu("File");
        jMenuBar1.add(file);
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);               
        exit.addActionListener(new MenuFileExit());
        
         
        //Panels Hierarchy
            //Panel (TOP)
            //Panel (BOTTOM)
                //SPanel (LFET) = LeftPanel
                //Panel (RIGHT) = RightPanel
                        //SPanel (RIGHT-TOP) = RightPanelChildTop
                        //SPanel (RIGHT-BOTTOM) = RightPanelChildBottom
        
                 

        //Swing Components - Top Panel 
            JPanel TopPanel = new JPanel();            
            
            ImageIcon EmailIcon  = new ImageIcon("C:\\Deyvid\\Concordia 2013\\SOEN 6441\\SVN\\trunk\\JavaApplication1\\src\\View\\EmailImg2.png");
            JLabel Title = new JLabel("CEC - Collaborative Email Client", EmailIcon, JLabel.LEFT); 
           
            TopPanel.setPreferredSize(new Dimension(1024, 36));  
  
            TopPanel.setLayout(new BorderLayout(5,5));
            TopPanel.add(Title); 


       //Swing Components - Bottom Panel 
            JPanel BottomPanel = new JPanel();
            BottomPanel.setLayout(new BorderLayout(2,2));
            
                 
            //Swing Components - Left Panel         

                  //Jtree >
                  JTree FolderList = new JTree();

                  FolderList.setPreferredSize(new java.awt.Dimension(200, 400));
                  FolderList.setRootVisible(false);
                  DefaultMutableTreeNode root   = new DefaultMutableTreeNode("Root");
                  DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("Inbox");
                  root.add(child1);
                  DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("Outbox");
                  root.add(child2);            
                  FolderList.setModel(new DefaultTreeModel(root));

                  //Adding components to the Left Panel
                  JScrollPane LeftPanel = new JScrollPane(FolderList, 
                                                          JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                          JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                                      
            
            //Swing Components - Right Panel 

                JPanel RightPanel = new JPanel();
                RightPanel.setLayout(new BorderLayout());                      
            
                    //Right-TOP

                    String[] EmailListValues = { "Item 1", "Item 2", "Item 3", "Item 4",  "Item 5", "Item 6", "Item 7", "Item 8","Item 9", };
                    JList EmailList = new JList(EmailListValues);
                
                    JScrollPane RightPanelChildTop = new JScrollPane(EmailList,
                                                         JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    RightPanelChildTop.setMaximumSize(new Dimension(1200, 120));
                    RightPanelChildTop.setMinimumSize(new Dimension(520, 120));
                    RightPanelChildTop.setPreferredSize(new Dimension(530, 120));       

              
          
                    //Right-BOTTOM
                    
                    JTextArea EmailBody = new JTextArea(10,10);
                    JScrollPane RightPanelChildBottom = new JScrollPane(EmailBody); 
                    RightPanelChildBottom.setLayout(new ScrollPaneLayout());

           
            
            RightPanel.add(RightPanelChildTop, BorderLayout.BEFORE_FIRST_LINE);  
            RightPanel.add(RightPanelChildBottom, BorderLayout.CENTER);  
 
            
        BottomPanel.add(LeftPanel, BorderLayout.WEST);    
        BottomPanel.add(RightPanel, BorderLayout.CENTER);   
            
      //Adding components to the Main Panel
        Container c = getContentPane();        
        c.add(TopPanel,BorderLayout.NORTH);
        c.add(BottomPanel,BorderLayout.LINE_START);        
    }       
}

    class MenuFileExit implements ActionListener{
        public void actionPerformed (ActionEvent e){
            System.exit(0);
        }
    }