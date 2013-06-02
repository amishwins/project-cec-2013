package cec.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class EmailFrame extends JFrame {
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

	JMenuItem replyItem = new JMenuItem("Reply", KeyEvent.VK_R);
	JMenuItem forwardItem = new JMenuItem("Fwd", KeyEvent.VK_F);
	JMenuItem draftItem = new JMenuItem("Save as Draft", KeyEvent.VK_D);
	JMenuItem sendItem = new JMenuItem("Send", KeyEvent.VK_S);
	JMenuItem saveTemplateItem = new JMenuItem("Save Template", KeyEvent.VK_T);
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
					.getReference().getSelectedTemplateFromDialog().toString());
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
					.getReference().getSelectedTemplateFromDialog().toString());
			setVisibilityOfButtonsAndMenuItemsForExistingTemplate();
			initialize();
			setTemplateFields();
			break;
		default:
			throw new RuntimeException("Cannot handle this type of context");
		}
	}

	private void createHandlerForSpecialFields() {

		// extract the ${} placeholders for each one.
		// add object and positions to linked list<object, start, end>
		// tab will set the selection to the next one?

		// how do we refresh to remove the thingy?

		String toFieldText = toField.getText();
		String ccFieldText = ccField.getText();
		String subjectFieldText = subjectField.getText();
		String bodyFieldText = bodyField.getText();

		PlaceholderHelper ph = new PlaceholderHelper(toFieldText);
		toField.select(ph.getStartPositionOfNextMatch(),
				ph.getEndPositionOfNextMatch());

		ph = new PlaceholderHelper(ccFieldText);
		ccField.select(ph.getStartPositionOfNextMatch(),
				ph.getEndPositionOfNextMatch());

		ph = new PlaceholderHelper(subjectFieldText);
		subjectField.select(ph.getStartPositionOfNextMatch(),
				ph.getEndPositionOfNextMatch());

		ph = new PlaceholderHelper(bodyFieldText);
		bodyField.select(ph.getStartPositionOfNextMatch(),
				ph.getEndPositionOfNextMatch());

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
		saveTemplate.setIcon(draftIcon);
		overwriteTemplate.setIcon(draftIcon);

		bar.setFloatable(false);
		bar.add(send);
		bar.add(draft);
		bar.add(reply);
		bar.add(forward);
		bar.add(saveTemplate);
		bar.add(overwriteTemplate);

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
		overwriteTemplate
				.addActionListener(new java.awt.event.ActionListener() {
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
		buildTemplateViewObject();
		templateService.saveTemplate(templateView);
		this.dispose();
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

	private void buildTemplateViewObject() {
		String name = JOptionPane.showInputDialog(null,
				"Enter name of template");
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

	private void setVisibilityOfButtonsAndMenuItemsForExistingEmail() {

		subjectField.setDocument(new EntryFieldMaxLength(max_Length));

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
		saveTemplate.setVisible(false);
	}

	private void setVisibilityOfButtonsAndMenuItemsForNewEmail() {
		subjectField.setDocument(new EntryFieldMaxLength(max_Length));
		// OPTION AVAILABLE
		reply.setVisible(false);
		forward.setVisible(false);
		send.setVisible(true);
		draft.setVisible(true);
		saveTemplate.setVisible(false);
		overwriteTemplate.setVisible(false);

		// OPTION AVAILABLE IN MENU
		replyItem.setVisible(false);
		forwardItem.setVisible(false);
		sendItem.setVisible(true);
		draftItem.setVisible(true);
		saveTemplateItem.setVisible(false);
	}

	private void setVisibilityOfButtonsAndMenuItemsForNewTemplate() {
		subjectField.setDocument(new EntryFieldMaxLength(max_Length));
		// OPTION AVAILABLE
		reply.setVisible(false);
		forward.setVisible(false);
		send.setVisible(false);
		draft.setVisible(false);
		saveTemplate.setVisible(true);
		overwriteTemplate.setVisible(false);

		// OPTION AVAILABLE IN MENU
		replyItem.setVisible(false);
		forwardItem.setVisible(false);
		sendItem.setVisible(false);
		draftItem.setVisible(false);
		saveTemplateItem.setVisible(true);
	}

	private void setVisibilityOfButtonsAndMenuItemsForExistingTemplate() {
		subjectField.setDocument(new EntryFieldMaxLength(max_Length));
		// OPTION AVAILABLE
		reply.setVisible(false);
		forward.setVisible(false);
		send.setVisible(false);
		draft.setVisible(false);
		saveTemplate.setVisible(false);
		overwriteTemplate.setVisible(true);

		// OPTION AVAILABLE IN MENU
		replyItem.setVisible(false);
		forwardItem.setVisible(false);
		sendItem.setVisible(false);
		draftItem.setVisible(false);
		saveTemplateItem.setVisible(false);
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
}
