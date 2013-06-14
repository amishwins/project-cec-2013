package cec.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import cec.config.CECConfigurator;
import cec.net.NetworkHelper;
import cec.service.FolderService;
import cec.service.EmailService;
import cec.service.MeetingService;
import cec.service.RuleService;
import cec.service.TemplateService;
import cec.service.TreeModelBuilder;
import exceptions.CannotDeleteSystemFolderException;
import exceptions.FolderAlreadyExistsException;
import exceptions.RootFolderSubfolderCreationException;
import exceptions.SourceAndDestinationFoldersAreSameException;

/**
* Implements SWING packages to create the main window of C.E.C application's
* user interface (UI) collecting user input and displaying output data from lower layers.
* Extends the JFRAME top-level container that represents a specialized window
* with OS controls/event handlers and contains all Swing components.   
* <p> 
* 
* Most important graphic components comprises: 
* - JTree <code>folders</code> which shows Email Directory structure
* - JTable <code>emailTable</code> which shows the Emails of each selected folder
* - JTextArea <code>emailBody</code> which shows the content of selected Email 
* 
* Different methods and inner classes interact with these objects retrieving values 
* and keeping them updated.
* <p>
* The JFrame also implements a Tree Selection Listener interface to be notified 
* when the user selects a node (folder) in the <code>folders</code> JTree. 
* Thus, whenever the value of the selection changes the method 
* <code>valueChanged()</code> is called.
*/

public class EmailClient extends JFrame implements TreeSelectionListener {
	private static final long serialVersionUID = 7366789547512037235L;

	JTree folders;
	JTable emailOrMeetingTable = new JTable();
	JTextArea emailOrMeetingBody;

	FolderService folderService = new FolderService();
	EmailService emailService = new EmailService();
	TemplateService templateService = new TemplateService();
	MeetingService meetingService = new MeetingService();

	EmailViewEntity selectedEmailEntity;
	MeetingViewEntity selectedMeetingEntity;
	String lastSelectedFolder;
	JTextField searchField = new JTextField(null, 22);
	
	EmailClientTimer tm = new EmailClientTimer(); //Timer
	JMenuItem timerAct = new JMenuItem("Start Timer");
	boolean timerStatus = true; 
	
	private static EmailClient instance;
	
	/***
	 * Returns a reference for the current instance (Main Window) to be used
	 * by the <code>Email</code> class to refresh the JTable <code>emailTable</code> content 
	 * when the user "Send" or "Save as a Draft" an Email.
	 *  
	 * @return	a reference for the current instance
	 */	
	public static EmailClient getReference() {
		if (instance == null) {
			instance = new EmailClient("Collaborative Email Client");
		}
		return instance;
	}

	/***
	 * Refreshes the content of the JTable <code>emailTable</code> 
	 * requesting the persistence layer to check the Operating
	 * system's File System and load an updated list of Email Entities (XML Files). 
	 */	
	public void updateEmailsTable() {
		if (CECConfigurator.getReference().isPathForAMeetingSystemFolder(lastSelectedFolder)) return;		
		String[] emailTableViewColumns = { "From", "Subject", "Date" };
		Iterable<EmailViewEntity> emailsInEachFolder = folderService.loadEmails(lastSelectedFolder);
		emailOrMeetingTable.setModel(new EmailListViewData(emailTableViewColumns, emailsInEachFolder));	
		defineEmailsOrMeetingsTableLayout();
	}

	public void updateMeetingsTable() {
		if (CECConfigurator.getReference().isPathForAEmailSystemFolder(lastSelectedFolder)) return;
		String[] meetingsTableViewColumns = { "From", "Subject", "Date" };
		Iterable<MeetingViewEntity> meetingsInEachFolder = folderService.loadMeetings(lastSelectedFolder);
		emailOrMeetingTable.setModel(new MeetingListViewData(meetingsTableViewColumns, meetingsInEachFolder));	
		defineEmailsOrMeetingsTableLayout();
	}
	
	private void setSelectedEmailEntity(EmailViewEntity emailViewEntity) {
		selectedEmailEntity = emailViewEntity;
	}
	
	private EmailViewEntity getSelectedEmailEntity() {
		return selectedEmailEntity; 
	}

	private void setSelectedMeetingEntity(MeetingViewEntity meetingViewEntity) {
		selectedMeetingEntity = meetingViewEntity;
	}
	
	private MeetingViewEntity getSelectedMeetingEntity() {
		return selectedMeetingEntity; 
	}
	
	
	private EmailClient(String title) {
		super(title);
		initialize();		
	}

	private void initialize() {

		// OS look and Feel
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (Exception e) {
			System.err.println("It was not possible to load Windows look and feel");
		}

		// Layout Settings
		setSize(1024, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);

		setupMenuBar();

		JPanel topPanel = new JPanel();
		ImageIcon emailIcon = new ImageIcon("images/email_at.png");
		JLabel titleLabel = new JLabel("CEC - Collaborative Email Client", emailIcon, JLabel.LEFT);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
		
		//Search Panel
		JPanel searchPanel = new JPanel();
		JButton searchBT = new JButton("Search");			
		searchField.setFont(new Font("Arial", Font.PLAIN, 12));			
		searchPanel.add(searchField, BorderLayout.CENTER);
		searchPanel.add(searchBT, BorderLayout.EAST);
		searchBT.addActionListener(new BarSearchEmails());	
		searchField.addKeyListener(new KeySearchEmails());		
		
		//Top Panel
		topPanel.setPreferredSize(new Dimension(1024, 45));
		topPanel.setLayout(new BorderLayout(5, 5));
		topPanel.add(titleLabel, BorderLayout.WEST);		
		topPanel.add(searchPanel, BorderLayout.EAST);		

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout(2, 2));

		TreeModelBuilder tmb = new TreeModelBuilder(new DefaultMutableTreeNode());
		Iterable<String> hierarchy = folderService.loadHierarchy();
		TreeModel treeModel = tmb.buildTreeNodeFromFileList(hierarchy);

		folders = new JTree(treeModel);
		folders.setRootVisible(false);
		folders.addTreeSelectionListener(this);
		folders.addMouseListener(new FolderTreeMouseListener(folders));
		showOneLevelOfTreeDisplayed(treeModel);

		folders.setPreferredSize(new java.awt.Dimension(200, 400));

		JScrollPane leftPanel = new JScrollPane(folders,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		emailOrMeetingTable.setFillsViewportHeight(true);
		emailOrMeetingTable.getSelectionModel().addListSelectionListener(new RowListener());
		emailOrMeetingTable.addMouseListener(new EmailTableMouseListener());
		emailOrMeetingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane rightPanelChildTop = new JScrollPane(emailOrMeetingTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		rightPanelChildTop.setMaximumSize(new Dimension(1200, 150));
		rightPanelChildTop.setMinimumSize(new Dimension(550, 200));
		rightPanelChildTop.setPreferredSize(new Dimension(550, 200));

		emailOrMeetingBody = new JTextArea(10, 10);
		emailOrMeetingBody.setEditable(false);
		JScrollPane rightPanelChildBottom = new JScrollPane(emailOrMeetingBody);
		rightPanelChildBottom.setLayout(new ScrollPaneLayout());

		rightPanel.add(rightPanelChildTop, BorderLayout.BEFORE_FIRST_LINE);
		rightPanel.add(rightPanelChildBottom, BorderLayout.CENTER);

		bottomPanel.add(leftPanel, BorderLayout.WEST);
		bottomPanel.add(rightPanel, BorderLayout.CENTER);

		Container container = getContentPane();
		container.add(topPanel, BorderLayout.NORTH);
		container.add(bottomPanel, BorderLayout.CENTER);

		selectInboxByDefault();
	}

	private void selectInboxByDefault() {
		folders.setSelectionRow(1); 
	}

	private void showOneLevelOfTreeDisplayed(TreeModel model) {
		DefaultMutableTreeNode currentNode = ((DefaultMutableTreeNode) model.getRoot()).getNextNode();
		do {
			if (currentNode.getLevel() == 1)
				folders.expandPath(new TreePath(currentNode.getPath()));

			currentNode = currentNode.getNextNode();
		} while (currentNode != null);
	}
	
	private void updateFolderTree() {
		TreeModelBuilder tmb = new TreeModelBuilder(new DefaultMutableTreeNode());
		Iterable<String> hierarchy = folderService.loadHierarchy();
		TreeModel treeModel = tmb.buildTreeNodeFromFileList(hierarchy);
		folders.setModel(treeModel);
		showOneLevelOfTreeDisplayed(treeModel);
	}

	private void setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		setupFileMenu(menuBar);
		setupEditMenu(menuBar);
		setupRulesMenu(menuBar);
		setupTimerMenu(menuBar);
		setupAuthenticationMenu(menuBar);
	}
	
	private void setupFileMenu(JMenuBar menuBar) {	
		
		
		JMenu fileMenuBarEntry = new JMenu("File");
		fileMenuBarEntry.setMnemonic('F');
		menuBar.add(fileMenuBarEntry);

		JMenuItem newEmail = new JMenuItem("New Email", KeyEvent.VK_N);
		newEmail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(newEmail);
				
		JMenuItem newEmailFromTemplate = new JMenuItem("New Email From Template", KeyEvent.VK_C);
		newEmailFromTemplate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(newEmailFromTemplate);
		fileMenuBarEntry.addSeparator();
		
		JMenuItem newMeeting = new JMenuItem("New Meeting", KeyEvent.VK_G);
		newMeeting.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(newMeeting);
		fileMenuBarEntry.addSeparator();
		
		JMenuItem newTemplate = new JMenuItem("New Template", KeyEvent.VK_T);
		newTemplate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(newTemplate);
		fileMenuBarEntry.addSeparator();
		
		JMenuItem newSubfolder = new JMenuItem("New Sub-folder", KeyEvent.VK_F);
		newSubfolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(newSubfolder);
		fileMenuBarEntry.addSeparator();

		JMenuItem openSelectedEmail = new JMenuItem("Open Email", KeyEvent.VK_O);
		openSelectedEmail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(openSelectedEmail);

		fileMenuBarEntry.addSeparator();

		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenuBarEntry.add(exitItem);

		// Add all the action listeners for the File menu
		newEmail.addActionListener(new MenuFileNewEmail());
		newEmailFromTemplate.addActionListener(new MenuFileNewEmailFromTemplate());
		newMeeting.addActionListener(new MenuFileNewMeeting());
		newTemplate.addActionListener(new MenuFileNewTemplate());
		newSubfolder.addActionListener(new MenuFileNewSubFolder());
		openSelectedEmail.addActionListener(new MenuFileOpenSelectedEmail());
		exitItem.addActionListener(new MenuFileExit());		
	}
	

	private void setupEditMenu(JMenuBar menuBar) {
		JMenu editMenuBarEntry = new JMenu("Edit");
		editMenuBarEntry.setMnemonic('E');
		menuBar.add(editMenuBarEntry);

		JMenuItem moveSelectedEmail = new JMenuItem("Move Email...", KeyEvent.VK_M);
		moveSelectedEmail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(moveSelectedEmail);

		editMenuBarEntry.addSeparator();
		
		JMenuItem deleteSelectedEmail = new JMenuItem("Delete Email", KeyEvent.VK_E);
		deleteSelectedEmail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(deleteSelectedEmail);
		
		editMenuBarEntry.addSeparator();
		
		JMenuItem editSelectedMeeting = new JMenuItem("Edit Meeting", KeyEvent.VK_I);
		editSelectedMeeting.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(editSelectedMeeting);

		editMenuBarEntry.addSeparator();
		
		JMenuItem deleteSelectedMeeting = new JMenuItem("Delete Meeting", KeyEvent.VK_B);
		deleteSelectedMeeting.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(deleteSelectedMeeting);
		
		editMenuBarEntry.addSeparator();
		
		JMenuItem deleteSelectedFolder = new JMenuItem("Delete Folder", KeyEvent.VK_R);
		deleteSelectedFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(deleteSelectedFolder);

		editMenuBarEntry.addSeparator();
		
		JMenuItem editTemplate = new JMenuItem("Edit Template", KeyEvent.VK_Z);
		editTemplate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(editTemplate);

		JMenuItem deleteTemplate = new JMenuItem("Delete Template", KeyEvent.VK_P);
		deleteTemplate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(deleteTemplate);
		
		// Add all the action listeners for the Edit menu
		moveSelectedEmail.addActionListener(new MenuEditMoveEmail());
		deleteSelectedEmail.addActionListener(new MenuEditDeleteEmail());
		editSelectedMeeting.addActionListener(new MenuEditEditMeeting());
		deleteSelectedMeeting.addActionListener(new MenuEditDeleteMeeting());
		deleteSelectedFolder.addActionListener(new MenuEditDeleteFolder());
		editTemplate.addActionListener(new MenuEditEditTemplate());
		deleteTemplate.addActionListener(new MenuEditDeleteTemplate());
	}
	
	private void setupRulesMenu(JMenuBar menuBar) {
		JMenu rulesMenuBarEntry = new JMenu("Rules");
		rulesMenuBarEntry.setMnemonic('R');
		menuBar.add(rulesMenuBarEntry);

		JMenuItem newRule = new JMenuItem("New Rule...", KeyEvent.VK_U);
		newRule.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
		rulesMenuBarEntry.add(newRule);

		JMenuItem ruleSett = new JMenuItem("Rule Settings...", KeyEvent.VK_S);
		ruleSett.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		rulesMenuBarEntry.add(ruleSett);
		
		// Add all the action listeners for the Rules menu
		newRule.addActionListener(new MenuRulesNewRule());
		ruleSett.addActionListener(new MenuRulesRuleSett());
	}
	
	// Timer
	private void setupTimerMenu(JMenuBar menuBar) {
		
		JMenu timerMenuBarEntry = new JMenu("Timer");		
		timerMenuBarEntry.setMnemonic('I');
		menuBar.add(timerMenuBarEntry);
		
		timerMenuBarEntry.add(timerAct);
	
		timerAct.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tm.enable(timerStatus);
			}
		});	

	}		
	
	private void setupAuthenticationMenu(JMenuBar menuBar)
	{		
		
		JMenu registration = new JMenu("Authentification");		
		registration.setMnemonic('W');
		menuBar.add(registration);
					
		JMenuItem registerMenuBarEntry = new JMenuItem("Connect");		
		registerMenuBarEntry.setMnemonic('E');
		registration.add(registerMenuBarEntry);
		registerMenuBarEntry.addActionListener(new ConnectToServer());
		
		
		JMenuItem registerMenuBarQuit = new JMenuItem("Disconnect");		
		registerMenuBarQuit.setMnemonic('Q');
		registration.add(registerMenuBarQuit);	
		registerMenuBarQuit.addActionListener(new DisconnectFromServer());
		
		
		
	}
	
	
	private void defineEmailsOrMeetingsTableLayout() {
		emailOrMeetingTable.getColumnModel().getColumn(0).setPreferredWidth(100);		
		emailOrMeetingTable.getColumnModel().getColumn(1).setPreferredWidth(300);
		emailOrMeetingTable.getColumnModel().getColumn(2).setPreferredWidth(100);		
	}	

	
	/*** 
	 * Tree Selection Listener for JTree <code>folders</code>.
	 * It's responsible for identifying the path of the selected node (folder)
	 * and calling the <code>updateEmailTable()</code> that refreshes the Email table.
	 */	
	
	@Override
	public void valueChanged(TreeSelectionEvent tse) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) folders.getLastSelectedPathComponent();

		if (node == null)
			return;

		Object[] pathComponents = folders.getSelectionPath().getPath();

		StringBuilder sb = new StringBuilder();
		for (Object o : pathComponents) {
			sb.append(o);
			sb.append("/");
		}
		sb.deleteCharAt(0);
		sb.deleteCharAt(sb.length() - 1);
		lastSelectedFolder = sb.toString();
		
		selectEmailsOrMeetings();
		
		setSelectedEmailEntity(null);
		setSelectedMeetingEntity(null);
	}
	
	public void selectEmailsOrMeetings(){
		if(lastSelectedFolder.equals(CECConfigurator.getReference().get("Meetings"))){
			updateMeetingsTable();
		}else{
			updateEmailsTable();
		}
	}

	// EMAIL TABLE MAIN LISTENER
	private class RowListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (event.getValueIsAdjusting()) {
				return;
			}
			
			if(emailOrMeetingTable.getModel() instanceof MeetingListViewData){
				setSelectedMeetingEntity(((MeetingListViewData) (emailOrMeetingTable.getModel())).getViewEntityAtIndex(emailOrMeetingTable.getSelectedRow()));
				emailOrMeetingBody.setText(getSelectedMeetingEntity().getBody());
			} else{
				setSelectedEmailEntity(((EmailListViewData) (emailOrMeetingTable.getModel())).getViewEntityAtIndex(emailOrMeetingTable.getSelectedRow()));
				emailOrMeetingBody.setText(getSelectedEmailEntity().getBody());
			}
			
			
		}
	}

	// FILE > NEW SUB-FOLDER >
	private class MenuFileNewSubFolder implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) folders
					.getLastSelectedPathComponent();

			if (node == null)
				JOptionPane.showMessageDialog(null, "Select a parent folder");
			else {

				String folderName = JOptionPane.showInputDialog(null, "Folder Name");
				Validator validator = new Validator();
				
				if (folderName != null) {

					if (folderName.trim().length() > 0 && validator.isValidFolderName(folderName)) {
						try {
							folderService.createSubFolder(lastSelectedFolder, folderName);
							updateFolderTree();
						} catch (RootFolderSubfolderCreationException ex) {
							JOptionPane.showMessageDialog(null, "You may not create a subfolder under the root directory.");
						}						
						catch (FolderAlreadyExistsException folderAlreadyExistsException) { 	
							JOptionPane.showMessageDialog(null, "A folder with that name already exists. Please enter another name.");
						} 	
					} else {
						JOptionPane.showMessageDialog(null,	"Invalid Folder name");
					}
				}
			}
		}
	}
	
	
	// EDIT > DELETE FOLDER >
		private class MenuEditDeleteFolder implements ActionListener {
			public void actionPerformed(ActionEvent e) {

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) folders
						.getLastSelectedPathComponent();

				if (node == null)
					JOptionPane.showMessageDialog(null, "Select a folder to delete");
				
				try {
					folderService.delete(lastSelectedFolder);
					updateFolderTree();
				} catch (CannotDeleteSystemFolderException ex) { 
					JOptionPane.showMessageDialog(null, "Cannot delete system folders");
				}
			}
		}

	// FILE > OPEN SELECTED EMAIL
	private class MenuFileOpenSelectedEmail implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if (getSelectedEmailEntity() == null) { 
				JOptionPane.showMessageDialog(null,	"Select an email to display");
			}
			else {
				new EmailFrame(selectedEmailEntity);
			}
		}
	}

	// EDIT > EDIT TEMPLATE
	private class MenuEditEditTemplate implements ActionListener {
		public void actionPerformed(ActionEvent e) { 
			templateToUse = getSelectedTemplateFromDialog();
			
			if (templateToUse != null)
				new EmailFrame(TemplateContext.EDIT);
		}
	}
	
	// EDIT > DELETE TEMPLATE
	private class MenuEditDeleteTemplate implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object template = getSelectedTemplateFromDialog();
			if (template != null)
				templateService.deleteTemplate(template.toString());
		}		
	}

	// EDIT > MOVE EMAIL >
	private class MenuEditMoveEmail implements ActionListener {
		public void actionPerformed(ActionEvent e) {
          
			if(emailOrMeetingTable.getModel() instanceof MeetingListViewData){
				if (getSelectedMeetingEntity() != null) {
					JOptionPane.showMessageDialog(null, "You Cannot move meeting to any other folder!");
					setSelectedMeetingEntity(null);
					return;
				}
			}
            
			if (getSelectedEmailEntity() == null) {
				JOptionPane.showMessageDialog(null, "Select an email first");
			} else {

				ArrayList<String> listOfFolders = (ArrayList<String>) folderService
						.loadHierarchy();
				String[] selValues = new String[listOfFolders.size()];
				int index = 0;
				for (String folder : listOfFolders) {
					selValues[index] = folder;
					index++;					
				}

				int messageType = JOptionPane.QUESTION_MESSAGE;
				Object mov = JOptionPane.showInputDialog(null,
						"Select the destination folder", "Move Email",
						messageType, null, selValues, null);

				if (mov != null) {
					
					try {
						emailService.move(getSelectedEmailEntity(), mov.toString());
						updateEmailsTable();
						setSelectedEmailEntity(null);
					}
					catch (SourceAndDestinationFoldersAreSameException ex) {
						
					}
				}
			}
		}
	}

	// EDIT > DELETE EMAIL >
	private class MenuEditDeleteEmail implements ActionListener {
		public void actionPerformed(ActionEvent e) {
				if (getSelectedEmailEntity() == null)
					JOptionPane.showMessageDialog(null, "Select Email First");

				if (getSelectedEmailEntity() != null) {
					emailService.delete(getSelectedEmailEntity());
					updateEmailsTable();
					setSelectedEmailEntity(null);
				}
			}
	}

		
	// EDIT > EDIT Meeting >
		private class MenuEditEditMeeting implements ActionListener {
			public void actionPerformed(ActionEvent e) {
					if (getSelectedMeetingEntity() == null)
						JOptionPane.showMessageDialog(null, "Select Meeting First");

					if (getSelectedMeetingEntity() != null) {
						new MeetingFrame(selectedMeetingEntity);
					}
				}
		}
	
	
	// EDIT > DELETE Meeting >
	private class MenuEditDeleteMeeting implements ActionListener {
		public void actionPerformed(ActionEvent e) {
				if (getSelectedMeetingEntity() == null)
					JOptionPane.showMessageDialog(null, "Select Meeting First");

				if (getSelectedMeetingEntity() != null) {
					meetingService.delete(getSelectedMeetingEntity());
					updateMeetingsTable();
					setSelectedMeetingEntity(null);
				}
			}
	}
	

	


	// FOLDER TREE CONTEXT MENU (Right-Click)
	private class FolderTreeContextMenu extends JPopupMenu {
		private static final long serialVersionUID = -5926440670627487856L;

		JMenuItem newFolder;
		JMenuItem delFolder;

		public FolderTreeContextMenu() {
			newFolder = new JMenuItem("New Sub-folder...");
			add(newFolder);
			delFolder = new JMenuItem("Delete Folder");
			add(delFolder);

			newFolder.addActionListener(new MenuFileNewSubFolder());
			delFolder.addActionListener(new MenuEditDeleteFolder());
		}
	}

	// EMAIL TABLE CONTEXT MENU (Right-Click)
	private class EmailTableContextMenu extends JPopupMenu {
		private static final long serialVersionUID = 1L;
		JMenuItem movEmail;
		JMenuItem delEmail;
		String moveEmailOrMeetingLabelContextMenu ="Move Email..." ;
		String deletEmailOrMeetingLabelContextMenu ="Delete Email" ;
		public EmailTableContextMenu() {
			if(emailOrMeetingTable.getModel() instanceof MeetingListViewData){
				if (getSelectedMeetingEntity() != null){
					deletEmailOrMeetingLabelContextMenu = "Delete Meeting";
				}
			}else{
				movEmail = new JMenuItem(moveEmailOrMeetingLabelContextMenu);
				add(movEmail);
				movEmail.addActionListener(new MenuEditMoveEmail());
			}
			
			delEmail = new JMenuItem(deletEmailOrMeetingLabelContextMenu);
			add(delEmail);
			delEmail.addActionListener(new MouseContextMenuDeleteEmail());
		}
	}
	
	private class MouseContextMenuDeleteEmail implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(emailOrMeetingTable.getModel() instanceof MeetingListViewData){
				if (getSelectedMeetingEntity() == null)
					JOptionPane.showMessageDialog(null, "Select Meeting First");

				if (getSelectedMeetingEntity() != null) {
					meetingService.delete(getSelectedMeetingEntity());
					updateMeetingsTable();
					setSelectedMeetingEntity(null);
				}
			}else{
				if (getSelectedEmailEntity() == null)
					JOptionPane.showMessageDialog(null, "Select Email First");

				if (getSelectedEmailEntity() != null) {
					emailService.delete(getSelectedEmailEntity());
					updateEmailsTable();
					setSelectedEmailEntity(null);
				}
			}
			
		}
	}
	

	// FOLDER TREE MOUSE LISTENER
	private class FolderTreeMouseListener extends MouseAdapter {

		JTree tree;
		int selNode;

		public FolderTreeMouseListener(JTree foldersTree) {
			this.tree = foldersTree;
		}

		public void mousePressed(MouseEvent e) {

			selNode = tree.getRowForLocation(e.getX(), e.getY());
			TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());


			if (selNode != -1) {
				tree.setSelectionPath(selPath);
				if (e.isPopupTrigger())
					Popup(e);
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (selNode != -1) {
				if (e.isPopupTrigger())
					Popup(e);
			}
		}

		private void Popup(MouseEvent e) {
			FolderTreeContextMenu menu = new FolderTreeContextMenu();
			menu.show(e.getComponent(), e.getX() + 7, e.getY());
		}
	}

	// EMAIL TABLE MOUSE LISTENER
	private class EmailTableMouseListener extends MouseAdapter {

		int selRow;

		public void mousePressed(MouseEvent e) {

			selRow = emailOrMeetingTable.rowAtPoint(e.getPoint());

			if (selRow != -1) {
				emailOrMeetingTable.setRowSelectionInterval(selRow, selRow);
				if (e.isPopupTrigger())
					Popup(e);
			}
		}

		public void mouseClicked(MouseEvent e) {
			if ((e.getClickCount() == 2) && (selRow != -1)) {
				if(emailOrMeetingTable.getModel() instanceof MeetingListViewData){
					new MeetingFrame(selectedMeetingEntity);
				}else{
					new EmailFrame(selectedEmailEntity);
				}
				
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (selRow != -1) {
				if (e.isPopupTrigger())
					Popup(e);
			}
		}

		private void Popup(MouseEvent e) {
			EmailTableContextMenu menu = new EmailTableContextMenu();
			menu.show(e.getComponent(), e.getX() + 7, e.getY());
		}

	}
	
	private class MenuFileNewEmail implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new EmailFrame();
		}
	}

	// FILE -> NEW EMAIL FROM TEMPLATE
	Object templateToUse;
	protected Object getSelectedTemplate() {
		return templateToUse;
	}
	private class MenuFileNewEmailFromTemplate implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			templateToUse = getSelectedTemplateFromDialog();
			
			if (templateToUse != null)
				new EmailFrame(TemplateContext.APPLY);
		}
	}
	
	private class MenuFileNewMeeting implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			new MeetingFrame();
		}
	}
	
	private class MenuFileNewTemplate implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new EmailFrame(TemplateContext.NEW);
		}
	}
	
	private class MenuFileExit implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	//RULES -> NEW RULE
	private class MenuRulesNewRule implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new RuleFrame();
		}
	}

	private class ConnectToServer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String clientEmailAddress = JOptionPane.showInputDialog(null, "Enter your email address");
			if (!clientEmailAddress.isEmpty())
			{
				// TODO: check if this email is valid
				CECConfigurator.getReference().put("ClientEmail", clientEmailAddress);
				NetworkHelper nw = new NetworkHelper();
				nw.connectToServer();
				
	
			}
		}
	}
	
	private class DisconnectFromServer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	//RULES -> RULE SETTINGS
	private class MenuRulesRuleSett implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new RuleSettings();
		}
	}
	
	private void updateEmailTableFound(String toFind) {
		selectInboxByDefault(); //? Keep? (Deyvid) it moves the cursor to Inbox folder, but loads its emails
		String[] emailTableViewColumns = { "From", "Subject", "Date" };			
		Iterable<EmailViewEntity> emailsFoundInFolder=folderService.searchEmails(toFind);		
		emailOrMeetingTable.setModel(new EmailListViewData(emailTableViewColumns, emailsFoundInFolder));			
		defineEmailsOrMeetingsTableLayout();
	}
	
	public Object getSelectedTemplateFromDialog() {
		String[] selValues = templateService.getTemplateNames();			

		int messageType = JOptionPane.QUESTION_MESSAGE;
		Object mov = JOptionPane.showInputDialog(null,
				"Select the template", "Choose Template",
				messageType, null, selValues, null);

		return mov;
	}

	private void search()
	{
		String toFind = searchField.getText();	
		Validator v =new Validator();
		if(v.isValidSearched(toFind))//if(toFind.trim().length()>0)					
			updateEmailTableFound(toFind);					
	}
	private class BarSearchEmails implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			search();
		}
	}
	
	private class KeySearchEmails implements KeyListener {
	    public void keyPressed(KeyEvent e) 
	    {
	        if ( e.getKeyCode() == KeyEvent.VK_ENTER ){
	        	search();	         
	        }	        	
	    }
	    public void keyReleased(KeyEvent e) {}	    
	    public void keyTyped(KeyEvent e) {}
	}
	
	//TIMER TEMPORARY SOLUTION FOR DEMO PRESENTATION
	
	private class EmailClientTimer {
	
		Timer timer = new Timer(5000, new newEmailForTestingRules());
		
		public EmailClientTimer() {			 
			 timer.setInitialDelay(1000);
		}
		
		public void enable(boolean flag) {

			if (flag)
				 timer.start();
			else
				timer.stop();
			
			timerStatus = !timerStatus;
			
			if (timerStatus)
				timerAct.setText("Start Timer");
			else
				timerAct.setText("Stop Timer");			
		}
		
		private boolean flag = false;
		
		private class newEmailForTestingRules implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				
				if (flag) {				
					UUID emailId;
					EmailViewEntity emailViewEntity;
					
					emailId = UUID.randomUUID();
					emailViewEntity = new EmailViewEntity();
					emailViewEntity.setId(emailId);
					emailViewEntity.setFrom("user@cec.com");
					emailViewEntity.setTo("deyvid.william@gmail.com");
					emailViewEntity.setCC("");
					emailViewEntity.setSubject("New Project "+emailId);
					emailViewEntity.setBody("Dears, we have to work on the new project asap.");
					emailViewEntity.setSentTime("2013.06.04_At_18.08.28.457");
					emailViewEntity.setLastModifiedTime("2013.06.04_At_18.08.28.457");				
				//	emailService.draftEmail(emailViewEntity);
				//	emailService.move(emailViewEntity, "emails/Inbox");		
					emailService.saveToInbox(emailViewEntity);//, "emails/Inbox");	
					
	
					emailViewEntity = null;
					emailId = UUID.randomUUID();
					emailViewEntity = new EmailViewEntity();
					emailViewEntity.setId(emailId);
					emailViewEntity.setFrom("user@cec.com");
					emailViewEntity.setTo("pankajkapania@yahoo.com");
					emailViewEntity.setCC("");
					emailViewEntity.setSubject("Basketball "+emailId);
					emailViewEntity.setBody("Dears, let's play basketball tonight!");
					emailViewEntity.setSentTime("2013.06.03_At_18.08.28.457");
					emailViewEntity.setLastModifiedTime("2013.06.03_At_18.08.28.457");				
					//emailService.draftEmail(emailViewEntity);
					//emailService.move(emailViewEntity, "emails/Inbox");	
					emailService.saveToInbox(emailViewEntity);
	
/*					emailViewEntity = null;
					emailId = UUID.randomUUID();
					emailViewEntity = new EmailViewEntity();
					emailViewEntity.setId(emailId);
					emailViewEntity.setFrom("user@cec.com");
					emailViewEntity.setTo("romeo.honvo@gmail.com");
					emailViewEntity.setCC("");
					emailViewEntity.setSubject("Jokes "+emailId);
					emailViewEntity.setBody("Jokes here.");
					emailViewEntity.setSentTime("2013.06.02_At_18.08.28.457");
					emailViewEntity.setLastModifiedTime("2013.06.02_At_18.08.28.457");				
					//emailService.draftEmail(emailViewEntity);
					//emailService.move(emailViewEntity, "emails/Inbox");	
					emailService.saveToInbox(emailViewEntity);*/
									
					updateEmailsTable();
					flag = !flag;
				
				} 				
				else {
					//RuleService ruleService = new RuleService();
					//ruleService.applyAll();
					updateEmailsTable();
					flag = !flag;
				}
							
			}
		}
	}
	
	//TIMER TEMPORARY SOLUTION FOR DEMO PRESENTATION
	/*	private class EmailClientTimer {
		
			Timer timer = new Timer(5000, new refreshJTable());
			
			public EmailClientTimer() {			 
				 timer.setInitialDelay(1000);
				 timer.start();
			}
			
			
			private class refreshJTable implements ActionListener {
				public void actionPerformed(ActionEvent e) {	
					RuleService ruleService = new RuleService();
					ruleService.applyAll();
					updateEmailsTable();				
								
				}
			}
		}*/
	
}

