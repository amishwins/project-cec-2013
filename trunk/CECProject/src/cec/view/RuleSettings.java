package cec.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cec.service.FolderService;

public class RuleSettings extends JFrame {
	
	
	
	Object rowData[][] = { { "Row1-Column1", "Row1-Column2", "Row1-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            { "Row2-Column1", "Row2-Column2", "Row2-Column3"},
            
	
	
	
	};
Object columnNames[] = { "Column One", "Column Two", "Column Three"};
JTable ruleTable = new JTable(rowData, columnNames);
FolderService folderService = new FolderService();

EmailViewEntity selectedRuleEntity;


	
	public RuleSettings(){
		initialize();
	}
	
	public RuleSettings(String rule){
		initialize();
	}	
	
	private void initialize() {		
		setSize(515, 370);
		setLayout(new BorderLayout());
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);

		setupMenuBar();
		setupToolBar();
		setupRuleTable();
		loadRuleTable();
	}

	private void setupMenuBar() {
		JMenuItem editItem = new JMenuItem("Edit Rule", KeyEvent.VK_D);
		JMenuItem deleteItem = new JMenuItem("Delete Rule", KeyEvent.VK_E);
		JMenuItem exitItem = new JMenuItem("Exit");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenuBarEntry = new JMenu("File");
		fileMenuBarEntry.setMnemonic('F');
		menuBar.add(fileMenuBarEntry);
		JMenu editMenuBarEntry = new JMenu("Edit");
		editMenuBarEntry.setMnemonic('E');
		menuBar.add(editMenuBarEntry);	
		
		exitItem.setAccelerator(KeyStroke.getKeyStroke("ESCAPE"));
		fileMenuBarEntry.add(exitItem);

		editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(editItem);
		editMenuBarEntry.addSeparator();
		deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(deleteItem);			

			
		editItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuEditEditRule();
			}
		});
		
		deleteItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuEditDeleteRule();
			}
		});
		
		exitItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitRuleSett();
			}
		});		
	}

	
	private void setupToolBar() {
		JToolBar bar = new JToolBar();
		bar.setPreferredSize(new Dimension(460, 60));
		ImageIcon applyIcon = new ImageIcon("images/rule_save.png");
		ImageIcon updateIcon = new ImageIcon("images/rule_edit.png");		
		ImageIcon deleteIcon = new ImageIcon("images/rule_delete.png");
		JButton apply = new JButton(" Apply");
		JButton applyAll = new JButton(" Apply All >>");
		JButton edit = new JButton("Edit");
		JButton delete = new JButton("Delete");
		
		apply.setIcon(applyIcon);
		applyAll.setIcon(applyIcon);
		edit.setIcon(updateIcon);
		delete.setIcon(deleteIcon);
		
		bar.setFloatable(false);
		bar.add(apply);
		bar.add(applyAll);
		bar.add(edit);		
		bar.add(delete);
		
		add(bar, BorderLayout.NORTH);		
		
		apply.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//applyRule();
			}
		});	

		applyAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//applyAllRules();
			}
		});	

		edit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuEditEditRule();
			}
		});		
		delete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//deleteRule();
			}
		});	
	}
	
	private void setupRuleTable() {

		JPanel midPanel = new JPanel(new BorderLayout());
		JPanel botPanel = new JPanel(new BorderLayout());
		JPanel botRight = new JPanel();
		JScrollPane botLeft = new JScrollPane(ruleTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		
		
		JButton moveTop = new JButton("Top ");
		JButton moveDown = new JButton("Down");
		
		botLeft.setPreferredSize(new Dimension(430, 250));	
		botRight.setPreferredSize(new Dimension(55, 50));		
				
		JSeparator hBar = new JSeparator();
		midPanel.add(hBar, BorderLayout.NORTH);
		add(midPanel);
		
		botRight.add(moveTop,BorderLayout.NORTH);		
		botRight.add(moveDown,BorderLayout.SOUTH);		
		botPanel.add(botLeft, BorderLayout.WEST);
		botPanel.add(botRight, BorderLayout.CENTER);

		add(botPanel, BorderLayout.SOUTH);	
		
		ruleTable.setFillsViewportHeight(true);
		ruleTable.getSelectionModel().addListSelectionListener(new ruleTableRowListener());
		ruleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
	}
	
	private void defineRuleTableLayout() {
		ruleTable.getColumnModel().getColumn(0).setPreferredWidth(70);		
		ruleTable.getColumnModel().getColumn(1).setPreferredWidth(70);
		ruleTable.getColumnModel().getColumn(2).setPreferredWidth(70);
		ruleTable.getColumnModel().getColumn(2).setPreferredWidth(30);	
	}	
	
	
	public void loadRuleTable() {
		String[] ruleTableViewColumns = { "From", "Words", "Folder","Rank" };
		Iterable<EmailViewEntity> emailsInEachFolder = folderService.loadEmails("emails\\Inbox");
		ruleTable.setModel(new EmailListViewData(ruleTableViewColumns, emailsInEachFolder));	
		defineRuleTableLayout();
	}	
	
	private void setSelectedRuleEntity(EmailViewEntity emailViewEntity) {
		selectedRuleEntity = emailViewEntity;
	}
	
	private EmailViewEntity getSelectedRuleEntity() {
		return selectedRuleEntity; 
	}
	
		
	// RULE TABLE MAIN LISTENER
	private class ruleTableRowListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (event.getValueIsAdjusting()) {
				return;
			} else {
				setSelectedRuleEntity(((EmailListViewData) (ruleTable
						.getModel())).getViewEntityAtIndex(ruleTable
						.getSelectedRow()));
			}
		}
	}

	// Actions > Update Rule
	private void menuEditEditRule() {			
			if (getSelectedRuleEntity() == null) {
				JOptionPane.showMessageDialog(null, "Select a rule to update");
			} else {
				new RuleFrame();
				// new RuleFrame(selectedRuleEntity);

			}
		}
	
	// Actions > Delete Rule
	private void menuEditDeleteRule() {			
			if (getSelectedRuleEntity() == null) {
				JOptionPane.showMessageDialog(null, "Select a rule to delete");
			} else {
				//new RuleFrame();
				// new RuleFrame(selectedRuleEntity);
			}
		}	
	
	

	// Actions > Exit
	private void exitRuleSett() {
		this.dispose();
	}
	
}

