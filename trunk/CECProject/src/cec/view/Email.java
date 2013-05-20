/*
 * NewMessage screen
 */

package cec.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.UUID;

import javax.swing.*;

import cec.config.CECConfigurator;
import cec.service.EmailService;

public class Email extends JFrame {
	private static final long serialVersionUID = 6361797821203537189L;
	private UUID id = null;

	EmailService emailService = new EmailService();

	JTextField toField = new JTextField("", 22);
	JTextField ccField = new JTextField("", 22);
	JTextField subjectField = new JTextField("", 22);
	JTextArea bodyField = new JTextArea("", 15, 20);

	JButton reply = new JButton(" Reply ");
	JButton forward = new JButton(" Forward ");
	JButton draft = new JButton("Save as Draft ");
	JButton send = new JButton(" Send >>   ");
	//JButton delete = new JButton("Delete ");
	JButton discard = new JButton("Discard ");
	
	
	

	JMenuItem replyItem = new JMenuItem("Reply", KeyEvent.VK_R);
	JMenuItem forwardItem = new JMenuItem("Fwd", KeyEvent.VK_F);
	JMenuItem draftItem = new JMenuItem("Save as Draft", KeyEvent.VK_D);
	JMenuItem sendItem = new JMenuItem("Send", KeyEvent.VK_S);
	//JMenuItem deleteItem = new JMenuItem("Delete", KeyEvent.VK_D);
	JMenuItem exitItem = new JMenuItem("Exit");

	EmailViewEntity emailView;

	public Email(EmailViewEntity email) {
		//super("");//("Existing Message");
		emailView = email;
		setExistingMessage();
		setMessageFields();
		initialize();

	}

	public Email() {
		//super("New Message");
		emailView = new EmailViewEntity();
		id = UUID.randomUUID();
		setNewMessage();
		//setMessageFields();
		initialize();

	}

	private void initialize() {
		setSize(610, 470);
		setLayout(new BorderLayout());
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);

		/*
		 * toField.setEditable(false); ccField.setEditable(false);
		 * subjectField.setEditable(false); bodyField.setEditable(false);
		 */
		// Menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenuBarEntry = new JMenu("File");
		fileMenuBarEntry.setMnemonic('F');
		menuBar.add(fileMenuBarEntry);

		replyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(replyItem);

		
		forwardItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				InputEvent.CTRL_DOWN_MASK)); 
		fileMenuBarEntry.add(forwardItem);
		 

		draftItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(draftItem);
		// draftItem.setVisible(false);

		sendItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(sendItem);
		// sendItem.setVisible(false);

		/*deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(deleteItem);*/

		exitItem.setAccelerator(KeyStroke.getKeyStroke("ESCAPE"));

		fileMenuBarEntry.add(exitItem);

		replyItem.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		editExistingMessage();}});
			
		

		
		forwardItem.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		editExistingMessage(); } });
		 

		draftItem.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		draftEmail();}});
			
		

		sendItem.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		deleteExistingMessage();}});
			
		

		/*deleteItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteExistingMessage();
			}
		});*/

		exitItem.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		discardExistingMessage();}});
			
		

		// Swing Components - Top Panel
		ImageIcon sendIcon = new ImageIcon("images/email_send.png");
		ImageIcon draftIcon = new ImageIcon("images/email_draft.png");
		ImageIcon discardIcon = new ImageIcon("images/email_discard.png");
		ImageIcon replyIcon = new ImageIcon("images/email_reply.png");
		ImageIcon forwardIcon = new ImageIcon("images/email_forward.png");

		reply.setIcon(replyIcon);
		forward.setIcon(forwardIcon);
		draft.setIcon(draftIcon);
		send.setIcon(sendIcon);
		//delete.setIcon(discardIcon);
		discard.setIcon(discardIcon);

		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		bar.add(send);
		bar.add(draft);
        bar.add(discard);
        bar.add(reply);
		bar.add(forward);
		// send.setVisible(false);
		//bar.add(delete);
		
		// discard.setVisible(false);

		reply.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		editExistingMessage();}});
			
		

		/*
		 * forward.addActionListener(new java.awt.event.ActionListener() {
		 * public void actionPerformed(java.awt.event.ActionEvent evt) {
		 * editExistingMessage(); } });
		 */

		draft.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		draftEmail();}});
				
				
			
		

		send.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		sendEmail();}});
			
		

		/*delete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteExistingMessage();
			}
		});*/

		discard.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		discardExistingMessage();}});
			
		

		// Swing Components - Middle Panel
		JLabel to = new JLabel("To:         ");
		JLabel cc = new JLabel("Cc:         ");
		JLabel subject = new JLabel("Subject: ");

		JPanel mid = new JPanel();
		mid.setPreferredSize(new Dimension(270, 300));
		mid.add(to);
		mid.add(toField);
		mid.add(cc);
		mid.add(ccField);
		mid.add(subject);
		mid.add(subjectField);

		// Swing Components - Bottom Panel
		bodyField.setLineWrap(true);
		bodyField.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(bodyField);

		// Finishing Panels Disposal
		add(bar, BorderLayout.NORTH);
		add(mid, BorderLayout.LINE_START);
		add(scroll, BorderLayout.SOUTH);

	}

	private void discardExistingMessage() {

		this.dispose();
	}

	// Actions - Draft
	private void deleteExistingMessage() {
		// DELETE
	}

	// Actions - Reply
	/*
	 * private void replyExistingMessage() { setNewMessage(); String auxSubject
	 * = subjectField.getText(); subjectField.setText("Re: " + auxSubject); }
	 */

	// Actions - Edit
	private void editExistingMessage() {
		setNewMessage();
		String auxSubject = subjectField.getText();
		subjectField.setText(auxSubject);
	}

	// Actions - Forward
	/*
	 * private void forwardExistingMessage() { setNewMessage();
	 * 
	 * String auxSubject = subjectField.getText(); subjectField.setText("Fwd: "
	 * + auxSubject);
	 * 
	 * }
	 */

	// Actions - Draft
	private void draftEmail() {		
		buildEmailViewObject();
        emailService.draftEmail(emailView);
	}

	private void sendEmail() {
		buildEmailViewObject();
		//System.out.println(emailView.getFolder());
        emailService.sendEmail(emailView);
        this.dispose();
	}
	
	private void buildEmailViewObject(){
		if(null==emailView.getId()){
			emailView.setId(id);
		}
		emailView.setTo(toField.getText());
		emailView.setCC(ccField.getText());
		emailView.setSubject(subjectField.getText());
		emailView.setBody(bodyField.getText());
	}

	/*
	 * private void SetExistingMessageFields() { String emailbody =
	 * "All the send does is move the email into a folder to be sent later \n Troy"
	 * ;
	 * SetExistingMessageFields("to@encs.xyz","ccField@encs.xyz","Subject",emailbody
	 * ); bodyField.setText(emailbody); }
	 */

	/*
	 * private void SetExistingMessageFields(String to, String cc, String
	 * subject, String emailbody) { toField.setText(to);// emailEntity.getTo());
	 * ccField.setText(cc);// emailEntity.getCC());
	 * subjectField.setText(subject);// emailEntity.getSubject());
	 * bodyField.setText(emailbody);// emailEntity.getBody()); }
	 */

	private void setMessageFields() {
		this.id = emailView.getId();
		this.toField.setText(emailView.getTo());
		this.ccField.setText(emailView.getCC());
		this.subjectField.setText(emailView.getSubject());
		this.bodyField.setText(emailView.getBody());
	}

	private void setExistingMessage() {
		
		//Draft Email				
		if (emailView.getFolder().equals(CECConfigurator.getReference().get("Drafts")))		
		{	
			discard.setVisible(false); //remove?
			
			reply.setVisible(false);
			forward.setVisible(false);
			
			send.setVisible(true);
			draft.setVisible(true);
					
			sendItem.setVisible(true);
			draftItem.setVisible(true);		
			replyItem.setVisible(false);
            forwardItem.setVisible(false);
                        
			toField.setEditable(true);
			ccField.setEditable(true);
			subjectField.setEditable(true);
			bodyField.setEditable(true);
			//setNewMessage();			
			
		}
		else
		{
						
			discard.setVisible(false); //remove?
			
            reply.setVisible(true);
			reply.setEnabled(false);			
			forward.setVisible(true);
			forward.setEnabled(false);
			
			replyItem.setVisible(true);
			replyItem.setEnabled(false);	
            forwardItem.setVisible(true);
            forwardItem.setEnabled(false);	
						
			send.setVisible(false);
			draft.setVisible(false);
			discard.setVisible(false);
			
			sendItem.setVisible(false);
			draftItem.setVisible(false);			               
                        
            toField.setEditable(false);
			ccField.setEditable(false);
			subjectField.setEditable(false);
			bodyField.setEditable(false);
			
		}		
	}

	private void setNewMessage() {

		// FIELD EDITABLE
		/*toField.setEditable(true);
		ccField.setEditable(true);
		subjectField.setEditable(true);
		bodyField.setEditable(true);*/		
		// OPTION AVAILABLE
		reply.setVisible(false);
		forward.setVisible(false);
		//delete.setVisible(false);
		send.setVisible(true);
		draft.setVisible(true);
		
		discard.setVisible(false); //remove?

		// OPTION AVAILABLE IN MENU
		replyItem.setVisible(false);
		forwardItem.setVisible(false);
		//deleteItem.setVisible(false);
		sendItem.setVisible(true);
		draftItem.setVisible(true);
	}

}
