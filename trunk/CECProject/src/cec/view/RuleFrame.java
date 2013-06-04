package cec.view;

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
		/***/
		FolderService folderService = new FolderService();
		ArrayList<String> listOfFolders = (ArrayList<String>) folderService.loadHierarchy();
		
		for (String folder : listOfFolders)
			folderChoosed.add(folder);
		/***/
		
		
		
		botBot.add(move,BorderLayout.WEST);
		botBot.add(folderChoosed,BorderLayout.EAST);
		
		
		
		botPanel.add(botTop, BorderLayout.NORTH);
		botPanel.add(botMid, BorderLayout.CENTER);
		botPanel.add(botBot, BorderLayout.SOUTH);
		add(botPanel, BorderLayout.SOUTH);
		
		
		
	}	
	
}
