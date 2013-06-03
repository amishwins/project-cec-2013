package cec.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class RuleFrame extends JFrame {
	
	public RuleFrame(){
		initialize();
	}
	
	public RuleFrame(String rule){
		initialize();
	}
	
	
	private void initialize() {		
		setSize(460, 380);
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
		
		JPanel botPanel = new JPanel(new BorderLayout());
		JPanel botTop = new JPanel();
		JPanel botMid = new JPanel();
		JPanel botBot = new JPanel();
		
		JLabel when = new JLabel("When receiving Emails");
		botTop.add(when);
		
		JLabel from = new JLabel("From:         ");
		JTextField fromField = new JTextField("", 22);
		JLabel content = new JLabel("Containing whe words: ");
		JTextField contentField = new JTextField("", 22);
		botMid.add(from);
		botMid.add(fromField);
		botMid.add(content);
		botMid.add(contentField);
		
		JLabel move = new JLabel("Move to: ");
		JTextField moveField = new JTextField("", 22);
		botBot.add(move);
		botBot.add(moveField);	

	//	midTop.setPreferredSize(new Dimension(50, 50));


		
		botPanel.add(botTop, BorderLayout.NORTH);
		botPanel.add(botMid, BorderLayout.EAST);
		botPanel.add(botBot, BorderLayout.SOUTH);
		add(botPanel, BorderLayout.SOUTH);
		
	}	
	
}
