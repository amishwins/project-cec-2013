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
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import cec.service.FolderService;
import cec.service.EmailService;
import cec.service.TreeModelBuilder;

public class EmailClient extends JFrame implements TreeSelectionListener {
	private static final long serialVersionUID = 7366789547512037235L;

	JTree folders;
	JTable emailTable = new JTable();
	JTextArea emailBody;

	FolderService folderService = new FolderService();
	EmailService emailService = new EmailService();

	EmailViewEntity selectedEmailEntity;
	String lastSelectedFolder;

	String[] emailTableViewColumns = { "Sent From", "Subject", "Date Sent" };

	public EmailClient(String title) {
		super(title);
		initialize();
	}

	private void initialize() {

		// Windows look and Feel
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

		topPanel.setPreferredSize(new Dimension(1024, 45));
		topPanel.setLayout(new BorderLayout(5, 5));
		topPanel.add(titleLabel);

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

		emailTable.setFillsViewportHeight(true);
		emailTable.getSelectionModel().addListSelectionListener(new RowListener());
		emailTable.addMouseListener(new EmailTableMouseListener());
		emailTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane rightPanelChildTop = new JScrollPane(emailTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		rightPanelChildTop.setMaximumSize(new Dimension(1200, 150));
		rightPanelChildTop.setMinimumSize(new Dimension(550, 200));
		rightPanelChildTop.setPreferredSize(new Dimension(550, 200));

		emailBody = new JTextArea(10, 10);
		emailBody.setEditable(false);
		JScrollPane rightPanelChildBottom = new JScrollPane(emailBody);
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

	private void updateJTree() {
		TreeModelBuilder tmb = new TreeModelBuilder(new DefaultMutableTreeNode());
		Iterable<String> hierarchy = folderService.loadHierarchy();
		TreeModel treeModel = tmb.buildTreeNodeFromFileList(hierarchy);
		folders.setModel(treeModel);
	}

	private void setupMenuBar() {
		// Menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenuBarEntry = new JMenu("File");
		fileMenuBarEntry.setMnemonic('F');
		menuBar.add(fileMenuBarEntry);

		JMenuItem newEmail = new JMenuItem("New Email", KeyEvent.VK_N);
		newEmail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(newEmail);

		JMenuItem newSubfolder = new JMenuItem("New Sub-folder", KeyEvent.VK_F);
		newSubfolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(newSubfolder);

		fileMenuBarEntry.addSeparator();

		JMenuItem openSelectedEmail = new JMenuItem("Open Selected Email",
				KeyEvent.VK_O);
		openSelectedEmail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(openSelectedEmail);

		fileMenuBarEntry.addSeparator();

		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenuBarEntry.add(exitItem);

		// Add all the action listeners for the File menu
		newEmail.addActionListener(new MenuFileNewEmail());
		newSubfolder.addActionListener(new MenuFileNewSubFolder());
		openSelectedEmail.addActionListener(new MenuFileOpenSelectedEmail());
		exitItem.addActionListener(new MenuFileExit());

		JMenu editMenuBarEntry = new JMenu("Edit");
		editMenuBarEntry.setMnemonic('E');
		menuBar.add(editMenuBarEntry);

		JMenuItem moveSelectedEmail = new JMenuItem("Move Selected Email",
				KeyEvent.VK_M);
		moveSelectedEmail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(moveSelectedEmail);

		editMenuBarEntry.addSeparator();

		JMenuItem deleteSelectedEmail = new JMenuItem("Delete Selected Email",
				KeyEvent.VK_E);
		deleteSelectedEmail.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(deleteSelectedEmail);

		JMenuItem deleteSelectedFolder = new JMenuItem(
				"Delete Selected Folder", KeyEvent.VK_R);
		deleteSelectedFolder.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(deleteSelectedFolder);

		// Add all the action listeners for the Edit menu
		deleteSelectedEmail.addActionListener(new MenuEditDeleteEmail());
		deleteSelectedFolder.addActionListener(new MenuEditDeleteFolder());
		moveSelectedEmail.addActionListener(new MenuEditMoveEmail());
	}

	// FOLDER TREE MAIN LISTENER
	@Override
	public void valueChanged(TreeSelectionEvent tse) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) folders
				.getLastSelectedPathComponent();

		if (node == null) // if user ctrl-clicks on a folder, then it will no
							// longer be selected
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

		Iterable<EmailViewEntity> emailsInEachFolder = folderService
				.loadEmails(lastSelectedFolder);
		emailTable.setModel(new EmailListViewData(emailTableViewColumns,
				emailsInEachFolder));

		selectedEmailEntity = null;

	}

	// EMAIL TABLE MAIN LISTENER
	private class RowListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (event.getValueIsAdjusting()) {
				return;
			}

			// What do we do if we are changing folders?
			selectedEmailEntity = ((EmailListViewData) (emailTable.getModel()))
					.getViewEntityAtIndex(emailTable.getSelectedRow());

			emailBody.setText(selectedEmailEntity.getBody());
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

				String folderName = JOptionPane.showInputDialog(null,
						"Folder Name");

				if (folderName != null) {

					String tryingtocreate = lastSelectedFolder + "/"
							+ folderName;
					Iterable<String> hierarchy = folderService.loadHierarchy();
					for (String folder : hierarchy) {
						if (tryingtocreate.equals(folder)) {
							JOptionPane.showInputDialog(null,
									"Already Exists!! ");
						}
					}

					if (folderName.trim().length() > 0) {
						folderService.createSubFolder(lastSelectedFolder,
								folderName);
						updateJTree();
						// TODO: Make it refresh!
						// Force Refresh - Making ROOT the selected node - NOT
						// WORKING

						// folders.setSelectionRow(0);

						// TODO: Move the code to get the subfolder list of
						// paths into the Hierarchy Model object
						// List<File> listOfFiles =
						// FolderDaoImpl.getSubFoldersRecursively(new
						// File("emails"));

					} else
						JOptionPane.showMessageDialog(null,
								"Invalid Folder name");
				}
			}

		}
	}

	// FILE > OPEN SELECTED EMAIL
	class MenuFileOpenSelectedEmail implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFrame nm = new Email(selectedEmailEntity);
		}
	}

	// EDIT > MOVE EMAIL >
	private class MenuEditMoveEmail implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (selectedEmailEntity == null) {
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
					// Moving
					emailService.move(selectedEmailEntity, mov.toString());
					// Refreshing Email Table Content - Asking Controller
					Iterable<EmailViewEntity> emailsInEachFolder = folderService
							.loadEmails(lastSelectedFolder);
					emailTable.setModel(new EmailListViewData(
							emailTableViewColumns, emailsInEachFolder));
				}
			}
		}
	}

	// EDIT > DELETE EMAIL >
	private class MenuEditDeleteEmail implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (selectedEmailEntity == null)
				JOptionPane.showMessageDialog(null, "SELECT EMAIL FIRST");

			if (selectedEmailEntity != null) {
				emailService.delete(selectedEmailEntity);

				// Refreshing Email Table Content - Asking Controller
				Iterable<EmailViewEntity> emailsInEachFolder = folderService
						.loadEmails(lastSelectedFolder);
				emailTable.setModel(new EmailListViewData(
						emailTableViewColumns, emailsInEachFolder));
			}
		}
	}

	// EDIT > DELETE FOLDER >
	private class MenuEditDeleteFolder implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) folders
					.getLastSelectedPathComponent();

			if (node == null)
				JOptionPane
						.showMessageDialog(null, "Select a folder to delete");

			folderService.delete(lastSelectedFolder);
			updateJTree();

		}
	}

	// FOLDER TREE CONTEXT MENU (Right-Click)
	class FolderTreeContextMenu extends JPopupMenu {
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
	class EmailTableContextMenu extends JPopupMenu {
		private static final long serialVersionUID = 1L;
		JMenuItem movEmail;
		JMenuItem delEmail;

		public EmailTableContextMenu() {
			movEmail = new JMenuItem("Move Email...");
			add(movEmail);
			delEmail = new JMenuItem("Delete Email");
			add(delEmail);

			movEmail.addActionListener(new MenuEditMoveEmail());
			delEmail.addActionListener(new MenuEditDeleteEmail());
		}
	}

	// FOLDER TREE MOUSE LISTENER
	class FolderTreeMouseListener extends MouseAdapter {

		JTree tree;
		int selNode;

		public FolderTreeMouseListener(JTree foldersTree) {
			this.tree = foldersTree;
		}

		public void mousePressed(MouseEvent e) {

			/**
			 * Checking if we are clicking on a valid JTree node - If so, we
			 * trigger the popup menu
			 */
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

			selRow = emailTable.rowAtPoint(e.getPoint());

			if (selRow != -1) {
				emailTable.setRowSelectionInterval(selRow, selRow);
				if (e.isPopupTrigger())
					Popup(e);
			}
		}

		public void mouseClicked(MouseEvent e) {
			if ((e.getClickCount() == 2) && (selRow != -1)) {
				JFrame nm = new Email(selectedEmailEntity);
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
}

class MenuFileExit implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}

class MenuFileNewEmail implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		JFrame nm = new Email();
	}
}
