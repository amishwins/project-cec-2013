package cec.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

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
import cec.service.RuleService;

/**
 * RuleSettings Class extends JFRAME to show a graphic windows allowing the user to check 
 * all the Rules configured in the Application and perform actions against them
 * such as Edit, Delete, Reorder(Up,Down) and Apply them individually/all.  * 
 */


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
RuleService ruleService = new RuleService();

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
		JMenuItem moveUpItem = new JMenuItem("Move Rule Up", KeyEvent.VK_U);
		JMenuItem moveDownItem = new JMenuItem("Move Rule Down", KeyEvent.VK_W);
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
		editMenuBarEntry.addSeparator();		
		moveUpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(moveUpItem);		
		moveDownItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
		editMenuBarEntry.add(moveDownItem);				

			
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
		moveUpItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				moveUpSelectedRuleEntity();
			}
		});
		moveDownItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				moveDownSelectedRuleEntity();
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
				menuEditDeleteRule();
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
		
		ImageIcon topIcon = new ImageIcon("images/arr_up.png");		
		ImageIcon downIcon = new ImageIcon("images/arr_down.png");		
		
		JButton moveUp = new JButton(" Up ");
		//moveUp.setIcon(topIcon);
		//moveUp.setPreferredSize(new Dimension(50, 50));	
		JButton moveDown = new JButton("Down ");
		//moveDown.setIcon(downIcon);
		//moveDown.setPreferredSize(new Dimension(50,50));
		
		botLeft.setPreferredSize(new Dimension(430, 250));	
		botRight.setPreferredSize(new Dimension(55, 50));		
				
		JSeparator hBar = new JSeparator();
		midPanel.add(hBar, BorderLayout.NORTH);
		add(midPanel);
		
		botRight.add(moveUp,BorderLayout.NORTH);		
		botRight.add(moveDown,BorderLayout.SOUTH);		
		botPanel.add(botLeft, BorderLayout.WEST);
		botPanel.add(botRight, BorderLayout.CENTER);

		add(botPanel, BorderLayout.SOUTH);	
		
		ruleTable.setFillsViewportHeight(true);
		ruleTable.getSelectionModel().addListSelectionListener(new ruleTableRowListener());
		ruleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		moveUp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				moveUpSelectedRuleEntity();
			}
		});		
		moveDown.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				moveDownSelectedRuleEntity();
			}
		});			
	}
	
	private void defineRuleTableLayout() {
		ruleTable.getColumnModel().getColumn(0).setPreferredWidth(70);		
		ruleTable.getColumnModel().getColumn(1).setPreferredWidth(70);
		ruleTable.getColumnModel().getColumn(2).setPreferredWidth(70);
		ruleTable.getColumnModel().getColumn(2).setPreferredWidth(30);	
	}		
	
	public void loadRuleTable() {
		String[] ruleTableViewColumns = { "From", "Words", "Folder","Rank" };
		Iterable<RuleViewEntity> rules = ruleService.loadAllRules();
		ruleTable.setModel(new RuleListViewData(ruleTableViewColumns, rules));	
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
				JOptionPane.showMessageDialog(null, "Select a Rule to update");
			} else {
				new RuleFrame();
				// new RuleFrame(selectedRuleEntity);

			}
		}
	
	// Actions > Delete Rule
	private void menuEditDeleteRule() {			
			if (getSelectedRuleEntity() == null) {
				JOptionPane.showMessageDialog(null, "Select a Rule to delete");
			} else {
				//new RuleFrame();
				// new RuleFrame(selectedRuleEntity);
			}
		}	
	
	// Actions > Move Rule Up
	public void moveUpSelectedRuleEntity() {
		if (getSelectedRuleEntity() == null) {
			JOptionPane.showMessageDialog(null, "Select a Rule to move");
		} else {
			// update rank to UP
			// reload table
			loadRuleTable();
		}
	}	
	// Actions > Move Rule Down
	public void moveDownSelectedRuleEntity() {
		if (getSelectedRuleEntity() == null) {
			JOptionPane.showMessageDialog(null, "Select a rule to move");
		} else {
			// update rank to UP
			// reload table
			loadRuleTable();
		}
	}	

	// Actions > Exit
	private void exitRuleSett() {
		this.dispose();
	}
	
}

