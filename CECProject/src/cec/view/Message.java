/*
 * NewMessage screen
 */

package cec.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import cec.service.EmailService;

public class Message extends JFrame {
	private static final long serialVersionUID = 6361797821203537189L;

	EmailService newEmailService = new EmailService();

	JTextField toField = new JTextField("", 22);
	JTextField ccField = new JTextField("", 22);
	JTextField subjectField = new JTextField("", 22);
	JTextArea bodyField = new JTextArea("", 15, 20);

	JButton edit = new JButton(" Edit ");
	//JButton forward = new JButton(" Forward ");
	JButton draft = new JButton("Save as Draft ");
	JButton send = new JButton(" Send >>   ");
	JButton delete = new JButton("Delete ");
	JButton discard = new JButton("Discard ");

	JMenuItem editItem = new JMenuItem("Edit", KeyEvent.VK_E);
	//JMenuItem forwardItem = new JMenuItem("Fwd", KeyEvent.VK_F);
	JMenuItem draftItem = new JMenuItem("Save as Draft", KeyEvent.VK_D);
	JMenuItem sendItem = new JMenuItem("Send", KeyEvent.VK_S);
	JMenuItem deleteItem = new JMenuItem("Delete", KeyEvent.VK_D);
	JMenuItem exitItem = new JMenuItem("Exit");           
        
        
	EmailViewEntity emailEntity;

	public Message(EmailViewEntity email) {
		super("Existing Message");
		emailEntity = email;
		
                setOldMessage();
		setMessageFields();
		initialize();
		
	}

	public Message() {
		super("New Message");
		emailEntity = new EmailViewEntity();
		
        setNewMessage();
		setMessageFields();
        initialize();
		
	}
	
	private void initialize()
	{
		setSize(610, 440);
		setLayout(new BorderLayout());
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);

		/*toField.setEditable(false);
		ccField.setEditable(false);
		subjectField.setEditable(false);
		bodyField.setEditable(false);*/
		// Menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenuBarEntry = new JMenu("File");
		menuBar.add(fileMenuBarEntry);

		editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(editItem);

		/*forwardItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(forwardItem);*/

		draftItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(draftItem);
		//draftItem.setVisible(false);

		sendItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(sendItem);
		//sendItem.setVisible(false);

		deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(deleteItem);

		exitItem.setAccelerator(KeyStroke.getKeyStroke("ESCAPE"));

		fileMenuBarEntry.add(exitItem);

		editItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editExistingMessage();
			}
		});

		/*forwardItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				forwardExistingMessage();
			}
		});*/

		draftItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				draftExistingMessage();
			}
		});

		sendItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteExistingMessage();
			}
		});

		deleteItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteExistingMessage();
			}
		});

		exitItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				discardExistingMessage();
			}
		});

		// Swing Components - Top Panel
		ImageIcon sendIcon = new ImageIcon("images/email_send.png");
		ImageIcon draftIcon = new ImageIcon("images/email_draft.png");
		ImageIcon discardIcon = new ImageIcon("images/email_discard.png");
		ImageIcon replyIcon = new ImageIcon("images/email_reply.png");
		ImageIcon forwardIcon = new ImageIcon("images/email_forward.png");

		edit.setIcon(replyIcon);
		//forward.setIcon(forwardIcon);
		draft.setIcon(draftIcon);
		send.setIcon(sendIcon);
		delete.setIcon(discardIcon);
		discard.setIcon(discardIcon);

		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		bar.add(edit);
		//bar.add(forward);
		bar.add(draft);
		
		bar.add(send);
		//send.setVisible(false);
		bar.add(delete);
		bar.add(discard);
		//discard.setVisible(false);

		edit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editExistingMessage();
			}
		});

		/*forward.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				forwardExistingMessage();
			}
		});*/

		draft.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				draftExistingMessage();
			}
		});

		send.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendExistingMessage();
			}
		});

		delete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteExistingMessage();
			}
		});

		discard.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				discardExistingMessage();
			}
		});

		// Swing Components - Middle Panel
		JLabel to = new JLabel("To:         ");
		JLabel cc = new JLabel("Cc:         ");
		JLabel subject = new JLabel("Subject:");

		JPanel mid = new JPanel();
		mid.setPreferredSize(new Dimension(320, 300));
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
	/*private void replyExistingMessage() {
		setNewMessage();
		String auxSubject = subjectField.getText();
		subjectField.setText("Re: " + auxSubject);
	}*/
	
	// Actions - Edit
		private void editExistingMessage() {
			setNewMessage();
			String auxSubject = subjectField.getText();
			subjectField.setText(auxSubject);
		}
	// Actions - Forward
	/*private void forwardExistingMessage() {
		setNewMessage();

		String auxSubject = subjectField.getText();
		subjectField.setText("Fwd: " + auxSubject);

	}*/

	// Actions - Draft
	private void draftExistingMessage() {
		String from = "cec@cec.com";
		String auxTo = toField.getText();
		String auxCc = ccField.getText();
		String auxSubject = subjectField.getText();
		String auxBody = bodyField.getText();
		newEmailService.draftEmail(from, auxTo, auxCc, auxSubject, auxBody);
	}

	private void sendExistingMessage() {
		String from = "cec@cec.com";
		String auxTo = toField.getText();
		String auxCc = ccField.getText();
		String auxSubject = subjectField.getText();
		String auxBody = bodyField.getText();
		newEmailService.sendEmail(from, auxTo, auxCc, auxSubject, auxBody);
	}

	/*
	 * private void SetExistingMessageFields() { String emailbody =
	 * "All the send does is move the email into a folder to be sent later \n Troy"
	 * ;
	 * SetExistingMessageFields("to@encs.xyz","ccField@encs.xyz","Subject",emailbody
	 * ); bodyField.setText(emailbody); }
	 */

	/*private void SetExistingMessageFields(String to, String cc, String subject,
			String emailbody) {
		toField.setText(to);// emailEntity.getTo());
		ccField.setText(cc);// emailEntity.getCC());
		subjectField.setText(subject);// emailEntity.getSubject());
		bodyField.setText(emailbody);// emailEntity.getBody());
	}*/

	private void setMessageFields() {
		toField.setText(emailEntity.getTo());
		ccField.setText(emailEntity.getCC());
		subjectField.setText(emailEntity.getSubject());
		bodyField.setText(emailEntity.getBody());
	}
        
        
        private void setOldMessage()
        {
            // FIELD EDITABLE 
            toField.setEditable(false);
            ccField.setEditable(false);
            subjectField.setEditable(false);
            bodyField.setEditable(false);
            
            // OPTION AVAILABLE
            draft.setVisible(false);
            send.setVisible(false);
            discard.setVisible(false);
            
            sendItem.setVisible(false);
            draftItem.setVisible(false);
        }
	private void setNewMessage() {

		// FIELD EDITABLE          
		toField.setEditable(true);
		ccField.setEditable(true);
		subjectField.setEditable(true);
		bodyField.setEditable(true);

		// OPTION AVAILABLE
		edit.setVisible(false);
		//forward.setVisible(false);
		delete.setVisible(false);
		send.setVisible(true);
		draft.setVisible(true);
		discard.setVisible(true);

		// OPTION AVAILABLE IN MENU
		editItem.setVisible(false);
		//forwardItem.setVisible(false);
		deleteItem.setVisible(false);
		sendItem.setVisible(true);
		draftItem.setVisible(true);
	}

}
