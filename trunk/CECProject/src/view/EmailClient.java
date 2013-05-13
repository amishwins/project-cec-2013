/**
 * @author Deyvid William / Created 07-May-13
 */

package view;

import service.Controller;
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
import javax.swing.tree.DefaultTreeCellRenderer;
import org.omg.CORBA.INITIALIZE;


public class EmailClient extends JFrame{
    JTree folderList = new JTree();    
    
    public EmailClient(String title){
        super(title);
        initialize();
    }
    
    private void initialize() {
        setSize(760, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        //Menu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenuBarEntry = new JMenu("File");
        menuBar.add(fileMenuBarEntry);    
        
        JMenuItem newMessageItem = new JMenuItem("New Message");
        fileMenuBarEntry.add(newMessageItem);   

        JMenuItem newMessageItemNewUI = new JMenuItem("New Message New UI");
        fileMenuBarEntry.add(newMessageItemNewUI);
        
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenuBarEntry.add(exitItem);           
        
        newMessageItem.addActionListener(new MenuFileNewMessage());   
        newMessageItemNewUI.addActionListener(new MenuFileNewMessageNewUI());
        exitItem.addActionListener(new MenuFileExit());
        
        //Panels Hierarchy
            //Panel (TOP)
            //Panel (BOTTOM)
                //SPanel (LFET) = LeftPanel
                //Panel (RIGHT) = RightPanel
                        //SPanel (RIGHT-TOP) = RightPanelChildTop
                        //SPanel (RIGHT-BOTTOM) = RightPanelChildBottom
        //Swing Components - Top Panel 
        JPanel topPanel = new JPanel();            
        ImageIcon emailIcon  = new ImageIcon("images/email_at.png");
        JLabel titleLabel = new JLabel("CEC - Collaborative Email Client", emailIcon, JLabel.LEFT); 

        topPanel.setPreferredSize(new Dimension(1024, 45));  
        topPanel.setLayout(new BorderLayout(5,5));
        topPanel.add(titleLabel); 

        //Swing Components - Bottom Panel 
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(2,2));
        //Swing Components - Left Panel         
        //Jtree >
        folderList.setPreferredSize(new java.awt.Dimension(200, 400));
        DefaultTreeCellRenderer render = (DefaultTreeCellRenderer)folderList.getCellRenderer();
        render.setLeafIcon(new ImageIcon("images/folder.gif"));
        render.setOpenIcon(new ImageIcon("images/folder.gif"));
        render.setClosedIcon(new ImageIcon("images/folder.gif"));

        //Generating Folder List
        Controller controller = new Controller();
        controller.getFolderListTree(folderList);

        folderList.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });

        //Adding components to the Left Panel
        JScrollPane leftPanel = new JScrollPane(folderList, 
                                                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //Swing Components - Right Panel 
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());                      

        //Right-TOP
        String[] emailListValues = { "Item 1", "Item 2", "Item 3", "Item 4",  "Item 5", "Item 6", "Item 7", "Item 8","Item 9", };
        JList EmailList = new JList(emailListValues);

        JScrollPane rightPanelChildTop = new JScrollPane(EmailList,
                                             JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                             JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        rightPanelChildTop.setMaximumSize(new Dimension(1200, 150));
        rightPanelChildTop.setMinimumSize(new Dimension(520, 150));
        rightPanelChildTop.setPreferredSize(new Dimension(530, 150));       

        //Right-BOTTOM
        JTextArea emailBody = new JTextArea(10,10);
        JScrollPane rightPanelChildBottom = new JScrollPane(emailBody); 
        rightPanelChildBottom.setLayout(new ScrollPaneLayout());

        rightPanel.add(rightPanelChildTop, BorderLayout.BEFORE_FIRST_LINE);  
        rightPanel.add(rightPanelChildBottom, BorderLayout.CENTER);  
            
        bottomPanel.add(leftPanel, BorderLayout.WEST);    
        bottomPanel.add(rightPanel, BorderLayout.CENTER);   
            
        //Adding components to the Main Panel
        Container container = getContentPane();        
        container.add(topPanel,BorderLayout.NORTH);
        container.add(bottomPanel,BorderLayout.CENTER);        
    }       
    
    
    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {                                    
       
        //System.out.println(FolderList.getLastSelectedPathComponent());
          //System.out.println(FolderList.getSelectionPaths().toString());
            System.out.println(folderList.getSelectionPath());
            //  System.out.println(FolderList.getLeadSelectionRow());

    }    

    
}

    class MenuFileExit implements ActionListener{
        public void actionPerformed (ActionEvent e){
            System.exit(0);
        }
    }

    class MenuFileNewMessageNewUI implements ActionListener{
        public void actionPerformed (ActionEvent e){
            JFrame nm = new NewMessage2();
        }
    }

    class MenuFileNewMessage implements ActionListener{
        public void actionPerformed (ActionEvent e){
            new NewMessageUI().setVisible(true);
        }
    }