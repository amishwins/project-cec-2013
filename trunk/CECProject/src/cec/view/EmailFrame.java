package cec.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.UUID;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.JTextComponent;

import cec.config.CECConfigurator;
import cec.service.EmailService;
import cec.service.PlaceholderHelper;
import cec.service.TemplateService;

/**
 * Email Class extends JFRAME <br>
 * show a graphic windows allowing user to read or write an email.<br>
 * uses <code>JTextField</code> and <code>JTextArea</code> components to set
 * editable fields<br>
 * uses <code>JButtons</code> and <code>JMenuItem </code>to provides options:<br>
 * <b>sendEmail()</b> <br>
 * <b>draftEmail()</b> <br>
 * Provides <br>
 * empty <code>JFrame</code> filled with existing<br>
 * <code>email</code> for the parameterized constructor<br>
 * and empty <code>JFrame</code> for the default constructor<br>
 * used for new <code>email<code>
 * <p>
 */

public class EmailFrame extends JFrame implements DocumentListener {
	private static final long serialVersionUID = 6361797821203537189L;
	private UUID id = null;
	private EmailClient mainClient;
	private Validator emailValidator = new Validator();

	EmailService emailService = new EmailService();
	TemplateService templateService;

	JTextField toField = new JTextField("", 65);
	JTextField ccField = new JTextField("", 65);
	JTextField subjectField = new JTextField("", 65);
	JTextArea bodyField = new JTextArea("", 15, 20);

	List<JTextComponent> componentsToValidate;

	JButton reply = new JButton(" Reply ");
	JButton forward = new JButton(" Forward ");
	JButton draft = new JButton("Save as Draft ");
	JButton send = new JButton(" Send >>   ");
	JButton saveTemplate = new JButton(" Save Template ");
	JButton overwriteTemplate = new JButton(" Overwrite Template ");
	JButton acceptMeeting = new JButton("Accept Meeting");
	JButton declineMeeting = new JButton("Decline Meeting");	

	JMenuItem replyItem = new JMenuItem("Reply", KeyEvent.VK_R);
	JMenuItem forwardItem = new JMenuItem("Fwd", KeyEvent.VK_F);
	JMenuItem draftItem = new JMenuItem("Save as Draft", KeyEvent.VK_D);
	JMenuItem sendItem = new JMenuItem("Send", KeyEvent.VK_S);
	JMenuItem saveTemplateItem = new JMenuItem("Save Template", KeyEvent.VK_T);
	JMenuItem overwriteTemplateItem = new JMenuItem("Overwrite Template", KeyEvent.VK_T);
	JMenuItem exitItem = new JMenuItem("Exit");

	EmailViewEntity emailView;
	TemplateViewEntity templateView;
	int max_Length = 250;

	/**
	 * Parameterized constructor of the Email Class.
	 * <p>
	 * Provides non Empty <code>JFrame</code> filled with existing<br>
	 * email that are editable only if it belongs to <br>
	 * Draft folder.<br>
	 * Available fields are Empty<br>
	 * <code>JTextField</code> with label <b>"To:"</b><br>
	 * <code>JTextField</code> with label <b>"Cc:"</b><br>
	 * <code>JTextField</code> with label <b>"Subject:" </b> with a maximum of
	 * 250 Characters <br>
	 * <code>JTextArea</code> with label <b>"Body"</b>
	 * <p>
	 * 
	 * @param email
	 *            an <code>email</code> object that provides the <br>
	 *            values used to set the JFrame
	 */
	public EmailFrame(EmailViewEntity email) {
		emailView = email;				
		setVisibilityOfButtonsAndMenuItemsForExistingEmail();
		setMessageFields();
		initialize();
	}

	public EmailFrame(TemplateContext context) {
		templateService = new TemplateService();
		switch (context) {
		case APPLY:
			// get the selected template, create an email normally
			templateView = templateService.getTemplateEntity(EmailClient
					.getReference().getSelectedTemplate().toString());
			emailView = new EmailViewEntity();
			id = UUID.randomUUID();
			setVisibilityOfButtonsAndMenuItemsForNewEmail();
			initialize();
			setTemplateFields();
			createHandlerForSpecialFields();
			break;
		case NEW:
			templateView = new TemplateViewEntity();
			setVisibilityOfButtonsAndMenuItemsForNewTemplate();
			initialize();
			break;
		case EDIT:
			templateView = templateService.getTemplateEntity(EmailClient
					.getReference().getSelectedTemplate().toString());
			setVisibilityOfButtonsAndMenuItemsForExistingTemplate();
			initialize();
			setTemplateFields();
			break;
		default:
			throw new RuntimeException("Cannot handle this type of context");
		}
	}

	private void createHandlerForSpecialFields() {
		
		bodyField.getDocument().addDocumentListener(this);
		
		InputMap toIm = toField.getInputMap();
		ActionMap toAm = toField.getActionMap();
		toIm.put(KeyStroke.getKeyStroke("ENTER"), "GoToNextPlaceHolder");
		toAm.put("GoToNextPlaceHolder", new CommitAction(toField));		

		InputMap ccIm = ccField.getInputMap();
		ActionMap ccAm = ccField.getActionMap();
		ccIm.put(KeyStroke.getKeyStroke("ENTER"), "GoToNextPlaceHolder");
		ccAm.put("GoToNextPlaceHolder", new CommitAction(ccField));		
		
		InputMap subjectIm = subjectField.getInputMap();
		ActionMap subjectAm = subjectField.getActionMap();
		subjectIm.put(KeyStroke.getKeyStroke("ENTER"), "GoToNextPlaceHolder");
		subjectAm.put("GoToNextPlaceHolder", new CommitAction(subjectField));		
		
		InputMap bodyIm = bodyField.getInputMap();
		ActionMap bodyAm = bodyField.getActionMap();
		bodyIm.put(KeyStroke.getKeyStroke("TAB"), "GoToNextPlaceHolder");
		bodyAm.put("GoToNextPlaceHolder", new CommitAction(bodyField));	
	}

	/**
	 * Default constructor of the Email Class.
	 * <p>
	 * Provides an Empty <code>JFrame</code> from which an email<br>
	 * can be written.
	 * <p>
	 * Available fields are non empty<br>
	 * <code>JTextField</code> with label <b>"To:"</b><br>
	 * <code>JTextField</code> with label <b>"Cc:"</b><br>
	 * <code>JTextField</code> with label <b>"Subject:" </b> with a maximum of
	 * 250 characters <br>
	 * <code>JTextArea</code> with label <b>"Body"</b>
	 */

	public EmailFrame() {
		emailView = new EmailViewEntity();
		id = UUID.randomUUID();
		setVisibilityOfButtonsAndMenuItemsForNewEmail();
		initialize();
	}

	private void initialize() {
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

		sendItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(sendItem);

		draftItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(draftItem);
		
		replyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(replyItem);
		
		forwardItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(forwardItem);
		
		saveTemplateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(saveTemplateItem);
		
		overwriteTemplateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(overwriteTemplateItem);


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
		
		saveTemplateItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveTemplate();
			}
		});

		overwriteTemplateItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				overwriteTemplate();
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
		ImageIcon acceptMeetingIcon = new ImageIcon("images/met_accept.png");
		ImageIcon declineMeetingIcon = new ImageIcon("images/met_decline.png");		

		send.setIcon(sendIcon);
		draft.setIcon(draftIcon);
		reply.setIcon(replyIcon);
		forward.setIcon(forwardIcon);
		saveTemplate.setIcon(draftIcon);
		overwriteTemplate.setIcon(draftIcon);	
		acceptMeeting.setIcon(acceptMeetingIcon);
		declineMeeting.setIcon(declineMeetingIcon);		

		bar.setFloatable(false);
		bar.add(send);
		bar.add(draft);
		bar.add(reply);
		bar.add(forward);
		bar.add(saveTemplate);
		bar.add(overwriteTemplate);
		bar.add(acceptMeeting);
		bar.add(declineMeeting);

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
		saveTemplate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveTemplate();
			}
		});
		overwriteTemplate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				overwriteTemplate();
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

	/**
	 * Exit message.
	 */
	private void ExitMessage() {

		this.dispose();
	}

	// Actions - Edit
	/**
	 * Edits the existing message.
	 */
	private void editExistingMessage() {
		setVisibilityOfButtonsAndMenuItemsForNewEmail();
		String auxSubject = subjectField.getText();
		subjectField.setText(auxSubject);
	}

	// Actions - Send
	/**
	 * Send email.
	 */
	private void sendEmail() {
		buildEmailViewObject();
		if (!validateEmailFields())
			return;
		emailService.sendEmail(emailView);
		mainClient.updateEmailsTable();
		this.dispose();
	}

	private void saveTemplate() {
		String name = JOptionPane.showInputDialog(null,
				"Enter name of template");
		if (name == null) {
			return;  // cancel
		} else if (name.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Empty name is invalid");
		}
		else {
			buildTemplateViewObject(name);
			templateService.saveTemplate(templateView);
			this.dispose();
		}			
	}

	private void overwriteTemplate() {
		setTemplateViewFields();
		templateService.saveTemplate(templateView);
		this.dispose();
	}

	// Actions - Draft
	/**
	 * Draft email.
	 */
	private void draftEmail() {
		buildEmailViewObject();
		emailService.draftEmail(emailView);
		mainClient.updateEmailsTable();
	}

	/**
	 * Validate email fields.
	 * 
	 * @return true, if successful
	 */
	private boolean validateEmailFields() {
		if (!emailValidator
				.isValidSendees(emailView.getTo(), emailView.getCC())) {
			JOptionPane.showMessageDialog(null,
					"One address is not properly formulated. Please recheck");
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

	private void buildTemplateViewObject(String name) {
		
		templateView.setName(name);
		setTemplateViewFields();
	}

	private void setTemplateViewFields() {
		templateView.setTo(toField.getText().trim());
		templateView.setCC(ccField.getText().trim());
		templateView.setSubject(subjectField.getText());
		templateView.setBody(bodyField.getText());
	}

	private void setMessageFields() {
		this.id = emailView.getId();
		this.toField.setText(emailView.getTo());
		this.ccField.setText(emailView.getCC());
		this.subjectField.setText(emailView.getSubject());
		this.bodyField.setText(emailView.getBody());
	}

	private void setTemplateFields() {
		this.toField.setText(templateView.getTo());
		this.ccField.setText(templateView.getCC());
		this.subjectField.setText(templateView.getSubject());
		this.bodyField.setText(templateView.getBody());
	}
	
	private void hideAllButtonsAndMenuItems() {
		reply.setVisible(false);
		forward.setVisible(false);
		send.setVisible(false);
		draft.setVisible(false);
		saveTemplate.setVisible(false);
		overwriteTemplate.setVisible(false);

		sendItem.setVisible(false);
		draftItem.setVisible(false);
		replyItem.setVisible(false);
		forwardItem.setVisible(false);
		saveTemplateItem.setVisible(false);
		overwriteTemplateItem.setVisible(false);

		toField.setEditable(true);
		ccField.setEditable(true);
		subjectField.setEditable(true);
		bodyField.setEditable(true);
		
		reply.setEnabled(true);
		forward.setEnabled(true);
		replyItem.setEnabled(false);
		forwardItem.setEnabled(false);
		
		acceptMeeting.setVisible(false);
		declineMeeting.setVisible(false);
	}

	private void setVisibilityOfButtonsAndMenuItemsForExistingEmail() {
		hideAllButtonsAndMenuItems();

		subjectField.setDocument(new EntryFieldMaxLength(max_Length));

		// Draft Email
		if (emailView.getFolder().equals(CECConfigurator.getReference().get("Drafts"))) {
			send.setVisible(true);
			draft.setVisible(true);
			sendItem.setVisible(true);
			draftItem.setVisible(true);

			toField.setEditable(true);
			ccField.setEditable(true);
			subjectField.setEditable(true);
			bodyField.setEditable(true);			
		}		
		else {
			reply.setVisible(true);
			forward.setVisible(true);
			replyItem.setVisible(true);
			forwardItem.setVisible(true);
			toField.setEditable(false);
			ccField.setEditable(false);
			subjectField.setEditable(false);
			bodyField.setEditable(false);
		}
		
		//Meeting Invitation 	
		if(emailView.isMeetingEmail()) {
		    acceptMeeting.setVisible(true);
			declineMeeting.setVisible(true);			
		}		
	}

	private void setVisibilityOfButtonsAndMenuItemsForNewEmail() {
		hideAllButtonsAndMenuItems();
		subjectField.setDocument(new EntryFieldMaxLength(max_Length));

		send.setVisible(true);
		draft.setVisible(true);
		sendItem.setVisible(true);
		draftItem.setVisible(true);
	}

	private void setVisibilityOfButtonsAndMenuItemsForNewTemplate() {
		hideAllButtonsAndMenuItems();
		subjectField.setDocument(new EntryFieldMaxLength(max_Length));

		saveTemplate.setVisible(true);
		saveTemplateItem.setVisible(true);
	}

	private void setVisibilityOfButtonsAndMenuItemsForExistingTemplate() {
		hideAllButtonsAndMenuItems();
		subjectField.setDocument(new EntryFieldMaxLength(max_Length));

		overwriteTemplate.setVisible(true);
		overwriteTemplateItem.setVisible(true);
	}

	/**
	 * The only purpose of this private class<br>
	 * is to provide in the JFRAME class <br>
	 * a defined maximum length for the <br>
	 * <code>JTextField</code> <b>SubjectField</b> <br>
	 * <b>Source: </b><br>
	 * "http://www.java-tips.org/java-se-tips/javax.swing/limit-jtextfield-input-to-a-maximum-length-3.html"
	 */

	private class EntryFieldMaxLength extends PlainDocument {
		private static final long serialVersionUID = 1L;
		private int maxlength;

		EntryFieldMaxLength(int length) {
			// super();
			this.maxlength = length;
		}

		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			if (str == null)
				return;

			if ((getLength() + str.length()) <= maxlength) {
				super.insertString(offs, str, a);
			}
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private class CommitAction extends AbstractAction {
		PlaceholderHelper ph;
		JTextComponent field;

		private static final long serialVersionUID = 1L;		

		public CommitAction(JTextComponent field) {
			this.field = field;
			this.field.select(0,0);
			execute();
		}

		public void actionPerformed(ActionEvent ev) {
			// Design decision: always only select the FIRST placeholder. This 
			// is to circumvent the issue of how to reset the placeholderhelper if
			// the user types something.
			execute();
		}
		
		private void execute() {
			ph = new PlaceholderHelper(field.getText());
			if (ph.findNext()) {
				field.select(ph.getStartPositionOfNextMatch(),
						ph.getEndPositionOfNextMatch());
			}
			else {
				ph.reset();
			}
		}
	}
}
