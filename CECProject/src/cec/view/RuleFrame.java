/*package cec.view;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import cec.service.FolderService;

public class RuleFrame extends JFrame {
	
	public RuleFrame(){
		initialize();
	}
	
	public RuleFrame(String rule){
		initialize();
	}
	
	
	private void initialize() {		
		setSize(460, 360);
		//setSize(610, 470);
		setLayout(new BorderLayout());
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);

		// Menu
		//setupMenuBar();
		// Button
		setupToolBar();
		// Entry Fields
		setupEntryFields();
	}
	
	private void setupToolBar() {
		JToolBar bar = new JToolBar();
		bar.setPreferredSize(new Dimension(460, 60));
		ImageIcon sendIcon = new ImageIcon("images/email_send.png");
		JButton send = new JButton(" Save >>   ");
		JSeparator hBar = new JSeparator();
		
		send.setIcon(sendIcon);
		bar.setFloatable(false);
		bar.add(send);		
		add(bar, BorderLayout.NORTH);
		add(hBar);
	}
	
	private void setupEntryFields() {
		
		//JPanel botPanel = new JPanel();
		JPanel botPanel = new JPanel(new BorderLayout());
		//botPanel.setLayout(new GridLayout(3,1));
		botPanel.setPreferredSize(new Dimension(460, 270));

		
		
		JPanel botTop = new JPanel();
		botTop.setPreferredSize(new Dimension(460, 90));
		
		JPanel botMid = new JPanel();
		botMid.setPreferredSize(new Dimension(460, 90));
		
		JPanel botBot = new JPanel();
		botBot.setPreferredSize(new Dimension(460, 90));
		
		//JLabel when = new JLabel("When receiving Emails");
		//botTop.add(when);		
		
		JLabel from = new JLabel("From:        ");
		JTextField fromField = new JTextField("", 45);
		botTop.add(from,BorderLayout.WEST);
		botTop.add(fromField,BorderLayout.EAST);

		
		JLabel content = new JLabel("Containing:");
		JTextField contentField = new JTextField("", 45);
			
		botMid.add(content,BorderLayout.WEST);
		botMid.add(contentField,BorderLayout.EAST);
		
	
		
		
		
		JLabel move = new JLabel("Move to:    ");
		//JTextField moveField = new JTextField("", 45);
		Choice folderChoosed=new Choice();
		folderChoosed.setPreferredSize(new Dimension(350, 00));
		
		FolderService folderService = new FolderService();
		ArrayList<String> listOfFolders = (ArrayList<String>) folderService.loadHierarchy();
		
		for (String folder : listOfFolders)
			folderChoosed.add(folder);
		
		
		
		
		botBot.add(move,BorderLayout.WEST);
		botBot.add(folderChoosed,BorderLayout.EAST);
		
		
		
		botPanel.add(botTop, BorderLayout.NORTH);
		botPanel.add(botMid, BorderLayout.CENTER);
		botPanel.add(botBot, BorderLayout.SOUTH);
		add(botPanel, BorderLayout.SOUTH);
		
		
		
	}	
	
}*/
package cec.view;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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
import cec.service.FolderService;

public class RuleFrame extends JFrame {
	
	JCheckBox from = new JCheckBox("From:        ");
	JCheckBox content = new JCheckBox("Containing:");
	JTextField fromField = new JTextField("", 40);
	JTextField contentField = new JTextField("", 40);
	Choice folderChoosed= new Choice();
	
	public RuleFrame(){
		initialize();
	}
	
	public RuleFrame(String rule){
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
		JSeparator hBar = new JSeparator();
		
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
		/***/
		FolderService folderService = new FolderService();
		ArrayList<String> listOfFolders = (ArrayList<String>) folderService.loadHierarchy();
		/*ArrayList<String> listOfFolders = (ArrayList<String>) folderService.loadHierarchy(
				CECConfigurator.getReference().get("Inbox"));*/
		
		for (String folder : listOfFolders)
			folderChoosed.add(folder);
		
		
		/***/
				
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
		
		if ((!from.isSelected()) &&(!content.isSelected())){
			JOptionPane.showMessageDialog(null, "Select at least one of the methods of implementation");
		}
		else
		{
			
			// Building the Rules 
			String choosed =folderChoosed.getSelectedItem();
			JOptionPane.showMessageDialog(null, "From "+fromField.getText()+"\nContaining "+contentField.getText()
													+"\n Selected Folder = "+choosed);							
		}		
	}
	
	//Actions > Exit
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

