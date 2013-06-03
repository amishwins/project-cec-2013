package cec.view;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

public class RuleFrame extends JFrame {
	
	public RuleFrame(){
		initialize();
	}
	
	public RuleFrame(String rule){
		initialize();
	}
	
	
	private void initialize() {		
		setSize(410, 350);
		setLayout(new BorderLayout());
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);

		// Menu
		//setupMenuBar();
		// Button
		setupToolBar();
		// Entry Fields
		//setupEntryFields();
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
}
