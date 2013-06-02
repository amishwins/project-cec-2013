package cec.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.JTextComponent;

import cec.config.CECConfigurator;
import cec.service.EmailService;
import cec.service.MeetingService;
import cec.service.TemplateService;


/**
 * Email Class extends JFRAME <br>
 * show a graphic windows allowing user to read or write an email.<br>
 * uses <code>JTextField</code> and <code>JTextArea</code> components to set editable fields<br>
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



public class MeetingFrame extends JFrame {
	private static final long serialVersionUID = 6361797821203537189L;
	private UUID id = null;
	private EmailClient mainClient;
	private Validator emailValidator = new Validator();

	MeetingService meetingService = new MeetingService();
	
	JTextField toTextField = new JTextField("", 65);
	JTextField subjectTextField = new JTextField("", 65);
	JTextField locationTextField = new JTextField("", 65);
	JTextField startDateTextField = new JTextField("YYYY-MM-DD", 15);
	JTextField endDateTextField = new JTextField("YYYY-MM-DD", 15);
	JComboBox startTimeComboBox;
	JComboBox endTimeComboBox;
	JTextArea bodyTextField = new JTextArea("", 14, 20);
	
	
	List<JTextComponent> componentsToValidate;

	JButton sendButton = new JButton(" Send >>   ");
	
	JMenuItem sendItem = new JMenuItem("Send", KeyEvent.VK_S);
	JMenuItem exitItem = new JMenuItem("Exit");

	MeetingViewEntity meetingView;
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
	 * <code>JTextField</code> with label <b>"Subject:" </b> with a maximum of 250 Characters <br>
	 * <code>JTextArea</code> with label <b>"Body"</b>
	 * <p>
	 * @param  email  an <code>email</code> object that provides the <br>
	 * 				  values used to set the JFrame
	 */
	public MeetingFrame(MeetingViewEntity email) {
		meetingView = email;
		setExistingMessage();
		setMessageFields();
		initialize();
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
	 * <code>JTextField</code> with label <b>"Subject:" </b> with a maximum of 250 characters <br>
	 * <code>JTextArea</code> with label <b>"Body"</b>
	 */

	public MeetingFrame() {
		meetingView = new MeetingViewEntity();
		id = UUID.randomUUID();
		setNewMessage();
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
		
		exitItem.setAccelerator(KeyStroke.getKeyStroke("ESCAPE"));

		fileMenuBarEntry.add(exitItem);

		sendItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendEmail();
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
	
		sendButton.setIcon(sendIcon);
	
		bar.setFloatable(false);
		bar.add(sendButton);
		

		sendButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendEmail();
			}
		});
			
		add(bar, BorderLayout.NORTH);
	}

	private void setupEntryFields() {
		JPanel meetingPanel = new JPanel();
		JLabel toLabel = new JLabel("To:         ");
		JLabel subjectLabel = new JLabel("Subject: ");
		JLabel locationLabel = new JLabel("Location:");
		JLabel startDateLabel = new JLabel("Start time: ");
		JLabel endDateLabel = new JLabel("      End time: ");
		startTimeComboBox = new JComboBox(buildTimeArrayForJComboBoxes());
		endTimeComboBox= new JComboBox(buildTimeArrayForJComboBoxes());
		meetingPanel.setPreferredSize(new Dimension(600, 00));
		
		meetingPanel.add(toLabel);
		meetingPanel.add(toTextField);
		meetingPanel.add(subjectLabel);
		meetingPanel.add(subjectTextField);
		meetingPanel.add(locationLabel);
		meetingPanel.add(locationTextField);
		//meetingPanel.add(new JSeparator(SwingConstants.HORIZONTAL),BorderLayout.LINE_START);
		meetingPanel.add(startDateLabel);
		meetingPanel.add(startDateTextField);
		meetingPanel.add(startTimeComboBox);
		meetingPanel.add(endDateLabel);
		meetingPanel.add(endDateTextField);
		meetingPanel.add(endTimeComboBox);
		
		add(meetingPanel, BorderLayout.LINE_START);				
		
		bodyTextField.setLineWrap(true);
		bodyTextField.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(bodyTextField);

		add(scroll, BorderLayout.SOUTH);
	}
	
	private Vector<String> buildTimeArrayForJComboBoxes(){
		DecimalFormat df = new DecimalFormat("00.00");
		Vector<String> timeArray = new Vector<>();
		String am = " AM";
		String pm = " PM";
		timeArray.add(df.format(new Double(12.00))+am);
		timeArray.add(df.format(new Double(12.30))+am);
		for(double i = 01.00;i<12.00;i=i+1.00){
			timeArray.add(df.format(new Double(i))+am);
			timeArray.add(df.format(new Double(i+ 0.30))+am);
		}
		timeArray.add(df.format(new Double(12.00))+pm);
		timeArray.add(df.format(new Double(12.30))+pm);
		for(double i = 01.00;i<12.00;i=i+1.00){
			timeArray.add(df.format(new Double(i))+pm);
			timeArray.add(df.format(new Double(i+ 0.30))+pm);
		}
		return timeArray;
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
		setNewMessage();
		String auxSubject = subjectTextField.getText();
		subjectTextField.setText(auxSubject);
	}

	// Actions - Send
	/**
	 * Send email.
	 */
	private void sendEmail() {
		buildMeetingViewObject();
		//if (!validateEmailFields())
		//	return;
		meetingService.sendMeeting(meetingView);
		mainClient.updateEmailTable();
		this.dispose();
	}

	private void buildMeetingViewObject() {
		if (null == meetingView.getId()) {
			meetingView.setId(id);
		}
		
		meetingView.setAttendees(toTextField.getText().trim());
		meetingView.setSubject(subjectTextField.getText());
		meetingView.setPlace(locationTextField.getText().trim());
		meetingView.setStartDate(startDateTextField.getText().trim());
		meetingView.setEndDate(endDateTextField.getText().trim());
		meetingView.setStartTime(startTimeComboBox.getSelectedItem().toString());
		meetingView.setEndTime(endTimeComboBox.getSelectedItem().toString());
		meetingView.setBody(bodyTextField.getText());
	}
		
	/**
	 * Validate email fields.
	 * 
	 * @return true, if successful
	 */
	private boolean validateEmailFields() {
		if (!emailValidator.isValidSendees(meetingView.getAttendees(), meetingView.getAttendees())) {
			JOptionPane.showMessageDialog(null, "One address is not properly formulated. Please recheck");
			return false;
		}
		return true;
	}

	
	private void setMessageFields() {
		this.id = meetingView.getId();
		this.toTextField.setText(meetingView.getAttendees());
		this.locationTextField.setText(meetingView.getPlace());
		this.subjectTextField.setText(meetingView.getSubject());
		this.startDateTextField.setText(meetingView.getStartDate());
		this.endDateTextField.setText(meetingView.getEndDate());
		//this.startTimeComboBox.set(meetingView.getStartTime());
		//this.endTimeComboBox.setText(meetingView.getEndTime());
		this.bodyTextField.setText(meetingView.getBody());
	}

	private void setExistingMessage() {
		
		subjectTextField.setDocument(new EntryFieldMaxLength(max_Length));
		// Draft Email
		if (meetingView.getFolder().equals(
				CECConfigurator.getReference().get("Drafts"))) {

			sendButton.setVisible(true);
			
			sendItem.setVisible(true);
		
			toTextField.setEditable(true);
			locationTextField.setEditable(true);
			subjectTextField.setEditable(true);
			bodyTextField.setEditable(true);

		} else {

			sendButton.setVisible(false);
			sendItem.setVisible(false);

			toTextField.setEditable(false);
			locationTextField.setEditable(false);
			subjectTextField.setEditable(false);
			bodyTextField.setEditable(false);

		}
	}

	private void setNewMessage() {
		subjectTextField.setDocument(new EntryFieldMaxLength(max_Length));
		// OPTION AVAILABLE
		sendButton.setVisible(true);
	
		// OPTION AVAILABLE IN MENU
		sendItem.setVisible(true);
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
		    //super();
		    this.maxlength = length;
		  }

		  public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		    if (str == null)
		      return;

		    if ((getLength() + str.length()) <= maxlength) {
		      super.insertString(offs, str, a);
		    }
		  }
		}
}

