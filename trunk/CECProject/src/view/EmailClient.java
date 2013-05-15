/**
 * @author Deyvid William / Created 07-May-13
 */
package view;

import model.FileTreeModel;
import service.Controller;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Vector;
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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import model.Hierarchy;
import persistence.FolderDaoFactory;
import persistence.FolderDaoImpl;
import service.TreeModelBuilder;


public class EmailClient extends JFrame implements TreeSelectionListener {
    JTree folders;    
    Controller controller = new Controller();
    JList emailList;
    
    public EmailClient(String title){
        super(title);
        initialize();
    }
    
    private void initialize() {
        setSize(760, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        setupMenuBar();
        
        // this should go in the design and be removed from here:
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
        
        TreeModelBuilder tmb = new TreeModelBuilder(new DefaultMutableTreeNode());
        List<File> listOfFiles = FolderDaoImpl.getSubFoldersRecursively(new File("emails"));
        TreeModel model = tmb.buildTreeNodeFromFileList(listOfFiles);
  
        folders = new JTree(model);
        folders.setRootVisible(false);
        folders.addTreeSelectionListener(this);
        
        //
        DefaultMutableTreeNode currentNode = ((DefaultMutableTreeNode)model.getRoot()).getNextNode();
        do {
            if (currentNode.getLevel()==1) 
                folders.expandPath(new TreePath(currentNode.getPath()));
            
            currentNode = currentNode.getNextNode();
        }
        while (currentNode != null);
        
        folders.setPreferredSize(new java.awt.Dimension(200, 400));
        DefaultTreeCellRenderer render = (DefaultTreeCellRenderer)folders.getCellRenderer();
        render.setLeafIcon(new ImageIcon("images/folder.gif"));
        render.setOpenIcon(new ImageIcon("images/folder.gif"));
        render.setClosedIcon(new ImageIcon("images/folder.gif"));

//        folders.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
//            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
//                folderSelected(evt);
//            }
//        });

        //Adding components to the Left Panel
        JScrollPane leftPanel = new JScrollPane(folders, 
                                                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //Swing Components - Right Panel 
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());                      

        //Right-TOP
        String[] emailListValues = { "Item 1", "Item 2", "Item 3", "Item 4",  "Item 5", "Item 6", "Item 7", "Item 8","Item 9", };
        emailList = new JList(emailListValues);

        JScrollPane rightPanelChildTop = new JScrollPane(emailList,
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
    
    
    private void folderSelected(javax.swing.event.TreeSelectionEvent evt) {                                    
       
        //System.out.println(FolderList.getLastSelectedPathComponent());
          //System.out.println(FolderList.getSelectionPaths().toString());
            System.out.println(folders.getSelectionPath());
            //  System.out.println(FolderList.getLeadSelectionRow());

    }    

    private void setupMenuBar() {
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
    }

    @Override
    public void valueChanged(TreeSelectionEvent tse) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)                            
                folders.getLastSelectedPathComponent(); 
        TreePath selectedFolderPath = folders.getSelectionPath();
//        Folder selectedFolder = new Folder(pathToFolder);
        
        if (node == null) return;           
        Object nodeInfo = node.getUserObject(); 
        System.out.println(nodeInfo.toString());
        System.out.println(folders.getSelectionPath().toString());
        // emailList.setModel();
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