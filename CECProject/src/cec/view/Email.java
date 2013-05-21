/*
 * NewMessage screen
 */

package cec.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.JTextComponent;

import cec.config.CECConfigurator;
import cec.service.EmailService;

public class Email extends JFrame {
	private static final long serialVersionUID = 6361797821203537189L;
	private UUID id = null;
	private EmailClient mainClient;
	private Validator emailValidator = new Validator();

	EmailService emailService = new EmailService();

	JTextField toField = new JTextField("", 65);
	JTextField ccField = new JTextField("", 65);
	JTextField subjectField = new JTextField("", 65);
	JTextArea bodyField = new JTextArea("", 15, 20);
	
	List<JTextComponent> componentsToValidate;

	JButton reply = new JButton(" Reply ");
	JButton forward = new JButton(" Forward ");
	JButton draft = new JButton("Save as Draft ");
	JButton send = new JButton(" Send >>   ");

	JMenuItem replyItem = new JMenuItem("Reply", KeyEvent.VK_R);
	JMenuItem forwardItem = new JMenuItem("Fwd", KeyEvent.VK_F);
	JMenuItem draftItem = new JMenuItem("Save as Draft", KeyEvent.VK_D);
	JMenuItem sendItem = new JMenuItem("Send", KeyEvent.VK_S);
	JMenuItem exitItem = new JMenuItem("Exit");

	EmailViewEntity emailView;

	public Email(EmailViewEntity email) {
		emailView = email;
		setExistingMessage();
		setMessageFields();
		initialize();

	}

	public Email() {
		emailView = new EmailViewEntity();
		id = UUID.randomUUID();
		setNewMessage();
		initialize();
	}

	private void initialize() {
		// Is this the correct approach? Should Email get the running
		// EmailClient? I think so!
		mainClient = EmailClient.getReference();
		setSize(610, 470);
		setLayout(new BorderLayout());
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);

		// Menu
		setupMenuBar();
		// Button
		setupToolBar();
		// Entry Fields
		setupEntryFields();

		
	}

	private void setupMenuBar() {

		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenuBarEntry = new JMenu("File");
		fileMenuBarEntry.setMnemonic('F');
		menuBar.add(fileMenuBarEntry);

		sendItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(sendItem);
		draftItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(draftItem);
		replyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(replyItem);
		forwardItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(forwardItem);
		exitItem.setAccelerator(KeyStroke.getKeyStroke("ESCAPE"));

		fileMenuBarEntry.add(exitItem);

		sendItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendEmail();
			}
		});
		draftItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				draftEmail();
			}
		});

		exitItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ExitMessage();
			}
		});
		
		

	}

	private void setupToolBar() {

		JToolBar bar = new JToolBar();
		ImageIcon sendIcon = new ImageIcon("images/email_send.png");
		ImageIcon draftIcon = new ImageIcon("images/email_draft.png");
		ImageIcon replyIcon = new ImageIcon("images/email_reply.png");
		ImageIcon forwardIcon = new ImageIcon("images/email_forward.png");

		send.setIcon(sendIcon);
		draft.setIcon(draftIcon);
		reply.setIcon(replyIcon);
		forward.setIcon(forwardIcon);

		bar.setFloatable(false);
		bar.add(send);
		bar.add(draft);
		bar.add(reply);
		bar.add(forward);

		send.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendEmail();
			}
		});
		draft.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				draftEmail();
			}
		});
		reply.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editExistingMessage();
			}
		});
		forward.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editExistingMessage();
			}
		});

		add(bar, BorderLayout.NORTH);
	}

	private void setupEntryFields() {
		JPanel mid = new JPanel();
		JLabel to = new JLabel("To:         ");
		JLabel cc = new JLabel("Cc:         ");
		JLabel subject = new JLabel("Subject: ");

		mid.setPreferredSize(new Dimension(600, 00));
		mid.add(to);
		mid.add(toField);
		mid.add(cc);
		mid.add(ccField);
		mid.add(subject);
		mid.add(subjectField);
		add(mid, BorderLayout.LINE_START);				
		
		bodyField.setLineWrap(true);
		bodyField.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(bodyField);

		add(scroll, BorderLayout.SOUTH);
	}

	private void ExitMessage() {

		this.dispose();
	}

	// Actions - Edit
	private void editExistingMessage() {
		setNewMessage();
		String auxSubject = subjectField.getText();
		subjectField.setText(auxSubject);
	}

	// Actions - Send
	private void sendEmail() {
		buildEmailViewObject();
		if (!validateEmailFields())
			return;
		emailService.sendEmail(emailView);
		mainClient.updateEmailTable();
		this.dispose();
	}

	// Actions - Draft
	private void draftEmail() {
		buildEmailViewObject();
		emailService.draftEmail(emailView);
		mainClient.updateEmailTable();
	}

	private boolean validateEmailFields() {
		if (!emailValidator.isValidSendees(emailView.getTo(), emailView.getCC())) {
			JOptionPane.showMessageDialog(null, "One address is not properly formulated. Please recheck");
			return false;
		}
		return true;
	}

	
	private void buildEmailViewObject() {
		if (null == emailView.getId()) {
			emailView.setId(id);
		}
		
		emailView.setTo(toField.getText().trim());
		emailView.setCC(ccField.getText().trim());
		emailView.setSubject(subjectField.getText());
		emailView.setBody(bodyField.getText());
	}


	private void setMessageFields() {
		this.id = emailView.getId();
		this.toField.setText(emailView.getTo());
		this.ccField.setText(emailView.getCC());
		this.subjectField.setText(emailView.getSubject());
		this.bodyField.setText(emailView.getBody());
	}

	private void setExistingMessage() {

		// Draft Email
		if (emailView.getFolder().equals(
				CECConfigurator.getReference().get("Drafts"))) {

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

		} else {

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

			sendItem.setVisible(false);
			draftItem.setVisible(false);

			toField.setEditable(false);
			ccField.setEditable(false);
			subjectField.setEditable(false);
			bodyField.setEditable(false);

		}
	}

	private void setNewMessage() {

		// OPTION AVAILABLE
		reply.setVisible(false);
		forward.setVisible(false);
		send.setVisible(true);
		draft.setVisible(true);

		// OPTION AVAILABLE IN MENU
		replyItem.setVisible(false);
		forwardItem.setVisible(false);
		sendItem.setVisible(true);
		draftItem.setVisible(true);
	}

}