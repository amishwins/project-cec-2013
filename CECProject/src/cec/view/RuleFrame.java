package cec.view;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import cec.config.CECConfigurator;
import cec.service.RuleService;
import cec.service.FolderService;

public class RuleFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JCheckBox from = new JCheckBox("From:        ");
	JCheckBox content = new JCheckBox("Containing:");
	JTextField fromField = new JTextField("", 40);
	JTextField contentField = new JTextField("", 40);
	Choice folderChoosed= new Choice();
	
	@SuppressWarnings("unused")
	private UUID id = null;
	private Validator emailValidator = new Validator();
	RuleService ruleService = new RuleService();
	RuleViewEntity ruleViewEntity;
	RuleSettings rs;
	
	public RuleFrame(){
		ruleViewEntity = new RuleViewEntity();
		id = UUID.randomUUID();
		initialize();
	}
	
	public RuleFrame(RuleViewEntity existingRule,RuleSettings rs){
		ruleViewEntity = existingRule;
		this.rs =rs;
		setRuleFields();
		initialize();
	}	
	
	private void initialize() {		
		setSize(460, 278);
		setLayout(new BorderLayout());
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);

		setupMenuBar();
		setupToolBar();
		setupEntryFields();
	}

	private void setupMenuBar() {
		JMenuItem saveItem = new JMenuItem("Save", KeyEvent.VK_S);
		JMenuItem exitItem = new JMenuItem("Exit");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenuBarEntry = new JMenu("File");
		fileMenuBarEntry.setMnemonic('F');
		menuBar.add(fileMenuBarEntry);

		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(saveItem);

		exitItem.setAccelerator(KeyStroke.getKeyStroke("ESCAPE"));
		fileMenuBarEntry.add(exitItem);
			
		saveItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveRule();
			}
		});
		
		exitItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitRule();
			}
		});		
	}

	
	private void setupToolBar() {
		JToolBar bar = new JToolBar();
		bar.setPreferredSize(new Dimension(460, 60));
		ImageIcon sendIcon = new ImageIcon("images/email_send.png");
		JButton save = new JButton(" Save >>   ");
		new JSeparator();
		
		save.setIcon(sendIcon);
		bar.setFloatable(false);
		bar.add(save);		
		add(bar, BorderLayout.NORTH);		
		
		save.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveRule();
			}
		});		
	}
	
	private void setupEntryFields() {
		
		JPanel midPanel = new JPanel(new BorderLayout());
		JPanel botPanel = new JPanel(new BorderLayout());
		
		JPanel botTop = new JPanel();
		JPanel botMid = new JPanel();
		JPanel botBot = new JPanel();
		
		botTop.setPreferredSize(new Dimension(460, 50));	
		botMid.setPreferredSize(new Dimension(460, 50));		
		botBot.setPreferredSize(new Dimension(460, 50));
		
		JSeparator hBar = new JSeparator();
		JLabel when = new JLabel("  When receiving Emails");
		midPanel.add(hBar, BorderLayout.NORTH);
		midPanel.add(when, BorderLayout.SOUTH);		
		
		botTop.add(from,BorderLayout.WEST);
		botTop.add(fromField,BorderLayout.EAST);		

		botMid.add(content,BorderLayout.WEST);
		botMid.add(contentField,BorderLayout.EAST);		
				
		JLabel move = new JLabel("        Move to:    ");

		//Choice folderChoosed=new Choice();
		folderChoosed.setPreferredSize(new Dimension(330, 00));
		FolderService folderService = new FolderService();		
		ArrayList<String> listOfFolders = (ArrayList<String>) folderService.loadSubFolders(
				CECConfigurator.getReference().get("Inbox"));
		
		for (String folder : listOfFolders)
			folderChoosed.add(folder);
		
		
			
		botBot.add(move,BorderLayout.WEST);
		botBot.add(folderChoosed,BorderLayout.EAST);
		
		
		add(midPanel);
		
		botPanel.add(botTop, BorderLayout.NORTH);
		botPanel.add(botMid, BorderLayout.CENTER);
		botPanel.add(botBot, BorderLayout.SOUTH);
		add(botPanel, BorderLayout.SOUTH);
		
		from.setSelected(true);	
		from.addItemListener(new ruleSelection());
		content.setSelected(true);
		content.addItemListener(new ruleSelection());		
		
	}
	
	//Actions > Save Rule
	private void saveRule(){
		
		// ensure both checkboxes are checked
		if ((!from.isSelected()) &&(!content.isSelected())){
			JOptionPane.showMessageDialog(null, "Select at least one of the methods of implementation");
		}
		else
		{
			saveRuleAndExit();			
		}		
	}
	
	private void buildRuleViewEntityObject() {
		ruleViewEntity.setWords(contentField.getText());
		ruleViewEntity.setEmailAddresses(fromField.getText());
		ruleViewEntity.setFolderPath(folderChoosed.getSelectedItem());
	}
	
	private void setRuleFields() {
		//id = ruleView.getID();
		contentField.setText(ruleViewEntity.getWords());//(ruleView.getWords());
		fromField.setText(ruleViewEntity.getEmailAddresses());//(ruleView.getEmailAddresses());
	}
	
	
	private void saveRuleAndExit()	{
		if (!validateEmailAndContainFields())
			return;
		
		buildRuleViewEntityObject();
		
		if (null == ruleViewEntity.getID()) {
			ruleService.save(ruleViewEntity);
		}else{
			ruleService.update(ruleViewEntity);
			rs.loadRuleTable();
		}
		exitRule();
	}
	
	
	
	private boolean validateEmailAndContainFields() {
		
		// check that both are not empty
		if (fromField.getText().isEmpty() && contentField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Both fields cannot be empty");
			return false;
		}
		
		// if the from is filled, and is invalid
		if (!fromField.getText().isEmpty() && !emailValidator.isValidSendees(fromField.getText(), fromField.getText())) {
			JOptionPane.showMessageDialog(null, "Address is not properly formulated");
			return false;
		}			
		
		return true;
		
	}
	

	private void exitRule(){
		this.dispose();		
	}
	
	private class ruleSelection implements ItemListener {	
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getItemSelectable();

			if (source == from) {
				if (e.getStateChange() == ItemEvent.DESELECTED){
					fromField.setText("");
					fromField.setEnabled(false);
				}
				else
					fromField.setEnabled(true);	
			}	    	
			else if (source == content) {
				if (e.getStateChange() == ItemEvent.DESELECTED){
					contentField.setText("");
					contentField.setEnabled(false);
				}
				else
					contentField.setEnabled(true);	
			}   
		}
	}
	
}

