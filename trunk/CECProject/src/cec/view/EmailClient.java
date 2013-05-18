/**
 * @author Deyvid William / Created 07-May-13
 */
package cec.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import cec.persistence.FolderDaoImpl;
import cec.service.Controller;
import cec.service.FolderService;
import cec.service.TreeModelBuilder;

public class EmailClient extends JFrame implements TreeSelectionListener {
	private static final long serialVersionUID = 7366789547512037235L;
	
	JTree folders;    
	JTable emailTable = new JTable();    
	JTextArea emailBody;

	Controller controller = new Controller(); 
    FolderService folderService= new FolderService();

    String[] emailTableViewColumns = {"Sent From", "Subject", "Date Sent"};
    
    public EmailClient(String title){
        super(title);
        initialize();
    }
    
    private void initialize() {
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        setupMenuBar();
        
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
        
        // TODO: Move the code to get the subfolder list of paths into the Hierarchy Model object
        List<File> listOfFiles = FolderDaoImpl.getSubFoldersRecursively(new File("emails"));
        TreeModel model = tmb.buildTreeNodeFromFileList(listOfFiles);
  
        folders = new JTree(model);
        folders.setRootVisible(false);
        folders.addTreeSelectionListener(this);
        folders.addMouseListener(new FoldersPopupListener(folders));
                
        
        // Show one level of folders displayed by default
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
        JScrollPane rightPanelChildTop = new JScrollPane(emailTable,
                                             JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                             JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        emailTable.setFillsViewportHeight(true);
        emailTable.getSelectionModel().addListSelectionListener(new RowListener());

        rightPanelChildTop.setMaximumSize(new Dimension(1200, 150));
        rightPanelChildTop.setMinimumSize(new Dimension(550, 200));
        rightPanelChildTop.setPreferredSize(new Dimension(550, 200));       

        //Right-BOTTOM
        emailBody = new JTextArea(10,10);
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
        
        //Focus on inbox folder when the App loads
        folders.setSelectionRow(1);
    }       

    private void setupMenuBar() {
        //Menu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenuBarEntry = new JMenu("File");
        menuBar.add(fileMenuBarEntry);    
        
        JMenuItem newEmail = new JMenuItem("New Email",KeyEvent.VK_N);
        newEmail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_DOWN_MASK));
        fileMenuBarEntry.add(newEmail);
        
        JMenuItem openSelectedEmail = new JMenuItem("Open Selected Email");
        fileMenuBarEntry.add(openSelectedEmail); 
        
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenuBarEntry.add(exitItem);           
        
        // Add all the action listeners for the File menu
        newEmail.addActionListener(new MenuFileNewEmail());
        openSelectedEmail.addActionListener(new MenuFileOpenSelectedEmail());
        exitItem.addActionListener(new MenuFileExit());
        
        // TODO: Add the keyboard shortcuts
        JMenu editMenuBarEntry = new JMenu("Edit");
        menuBar.add(editMenuBarEntry);
        
        JMenuItem deleteSelectedEmail = new JMenuItem("Delete Selected Email");
        editMenuBarEntry.add(deleteSelectedEmail);
        
        JMenuItem deleteSelectedFolder = new JMenuItem("Delete Selected Folder");
        editMenuBarEntry.add(deleteSelectedFolder);
        
        // TODO: Add all the action listeners for the Edit menu
        deleteSelectedEmail.addActionListener(new MenuEditDeleteEmail());
        deleteSelectedFolder.addActionListener(new MenuEditDeleteFolder());
    }

    @Override
    public void valueChanged(TreeSelectionEvent tse) {
    	
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)folders.getLastSelectedPathComponent(); 

        if (node == null) // if user ctrl-clicks on a folder, then it will no longer be selected 
        	return;
        
        Object[] pathComponents = folders.getSelectionPath().getPath();

        StringBuilder sb = new StringBuilder();
        for(Object o: pathComponents) {
            sb.append(o);
            sb.append("/");
        }
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length() - 1);

     
        
        System.out.println("Deyvid > " + sb.toString());
                    
        Iterable<EmailViewEntity> emailsInEachFolder  = folderService.loadEmails(sb.toString());
        emailTable.setModel(new EmailListViewData(emailTableViewColumns, emailsInEachFolder)); 
    }
    
    private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            
            // What do we do if we are changing folders? 
			EmailViewEntity selectedEmailEntity = ((EmailListViewData)(emailTable.getModel()))
					.getViewEntityAtIndex(emailTable.getSelectedRow());
            
            emailBody.setText(selectedEmailEntity.getBody());
        }
    }
    
  //Email Table Mouse Events
    private class EmailTableMouseListener extends MouseAdapter {
    	
    	public void mouseClicked(MouseEvent e) {
    		if (e.getClickCount() == 2) {
    			EmailViewEntity selectedEmailEntity = ((EmailListViewData)(emailTable.getModel()))
    					.getViewEntityAtIndex(emailTable.getSelectedRow());
    			JFrame nm = new ExistingMessage(selectedEmailEntity);
			}	  
    	}
    }
    
    // TODO: tie this into Pankaj's service layer classes 
    
    private class MenuEditDeleteEmail implements ActionListener {
    	public void actionPerformed (ActionEvent e) {
			EmailViewEntity selectedEmailEntity = ((EmailListViewData)(emailTable.getModel()))
					.getViewEntityAtIndex(emailTable.getSelectedRow());
			String output;
			output = selectedEmailEntity.getId() + selectedEmailEntity.getFolder();
			JOptionPane.showMessageDialog(null, output);
    	}
    }
    
    private class MenuEditDeleteFolder implements ActionListener {
    	public void actionPerformed (ActionEvent e) {
    		
    		// TODO: this is duplicate code - refactor!

            DefaultMutableTreeNode node = (DefaultMutableTreeNode)folders.getLastSelectedPathComponent(); 

            if (node == null)  
            	JOptionPane.showMessageDialog(null, "Select a folder to delete");
            
            Object[] pathComponents = folders.getSelectionPath().getPath();

            StringBuilder sb = new StringBuilder();
            for(Object o: pathComponents) {
                sb.append(o);
                sb.append("/");
            }
            sb.deleteCharAt(0);
            sb.deleteCharAt(sb.length() - 1);
            JOptionPane.showMessageDialog(null, sb.toString());
			
		}
    }    
    
}

class MenuFileExit implements ActionListener {
	public void actionPerformed (ActionEvent e) {
		System.exit(0);
	}
}

class MenuFileNewEmail implements ActionListener {
	public void actionPerformed (ActionEvent e) {
		JFrame nm = new NewMessage();
    }
}
   
class MenuFileOpenSelectedEmail implements ActionListener {
	public void actionPerformed (ActionEvent e) {
		JFrame nm = new ExistingMessage(new EmailViewEntity());
	}
}

class PopupDeleteFolder implements ActionListener {
	public void actionPerformed (ActionEvent e) {
    	JOptionPane.showMessageDialog(null, "Operation not allowed");
    }
}            



    
//Folders PopUp Menu (Right-Click)
class FolderTreeContextMenu extends JPopupMenu {
	private static final long serialVersionUID = -5926440670627487856L;
	JMenuItem delFolder;
    JMenuItem newFolder;        
    public FolderTreeContextMenu(){
        newFolder = new JMenuItem("New Folder");
        add(newFolder);        	
    	delFolder = new JMenuItem("Delete Folder");
        add(delFolder);
        
        delFolder.addActionListener(new PopupDeleteFolder());   
    }
}
    
//Folders PopUp Menu Listener
class FoldersPopupListener extends MouseAdapter {
	
	JTree tree;
	int   selNode;
	
	public FoldersPopupListener(JTree foldersTree){
		this.tree = foldersTree;    		
	}    	
	
    public void mousePressed(MouseEvent e){
    	
    	/** Checking if we are clicking on a valid JTree node - If so, we trigger the popup menu */
    	selNode = tree.getRowForLocation(e.getX(), e.getY());
    	TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
    	
    	System.out.println(selNode);
    	System.out.println(selPath);        	
    	
    	if(selNode != -1) {	  
    		tree.setSelectionPath(selPath);
            if (e.isPopupTrigger())
            	Popup(e);
    	}
    }

    public void mouseReleased(MouseEvent e){
    	if(selNode != -1) {	  
            if (e.isPopupTrigger())
            	Popup(e);
    	}
    }

    private void Popup(MouseEvent e){
    	FolderTreeContextMenu menu = new FolderTreeContextMenu();
        menu.show(e.getComponent(), e.getX()+7, e.getY());
    }
}


    