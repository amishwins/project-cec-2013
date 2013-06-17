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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.JTextComponent;

import cec.net.NetworkHelper;
import cec.service.MeetingService;

/**
 * MeetingFrame Class extends JFRAME <br>
 * show a graphic windows allowing user to create or edit meeting.<br>
 * uses <code>JTextField</code> and <code>JTextArea</code> components to set
 * editable fields<br>
 * uses <code>JButtons</code> and <code>JMenuItem </code>to provides options:<br>
 * sendMeeting()<br>
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	JComboBox startTimeComboBox = new JComboBox(buildTimeArrayForJComboBoxes());
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	JComboBox endTimeComboBox = new JComboBox(buildTimeArrayForJComboBoxes());

	JTextArea bodyTextField = new JTextArea("", 14, 20);

	List<JTextComponent> componentsToValidate;

	JButton sendButton = new JButton("Send >>");

	JMenuItem sendItem = new JMenuItem("Send", KeyEvent.VK_U);

	JMenuItem exitItem = new JMenuItem("Exit");

	MeetingViewEntity meetingView;
	int max_Length = 250;

	public MeetingFrame() {
		meetingView = new MeetingViewEntity();
		id = UUID.randomUUID();
		buildMeetingFrame();
		setPropertiesForNewMeetingFrameFields();
	}

	/**
	 * Parameterized constructor of the MeetingFrame Class.
	 * <p>
	 * Provides non Empty <code>JFrame</code> filled with existing<br>
	 * meeting that are editable
	 * <code>JTextField</code> with label "To:"<br>
	 * <code>JTextField</code> with label "Subject:"<br>
	 * <code>JTextField</code> with label "Location:"<br>
	 * <code>JTextArea</code> with label <b>"Body"</b>
	 * <p>
	 * 
	 * @param existingMeeting
	 *            an <code>MeetingViewEntity</code> object that provides the <br>
	 *            values used to set the JFrame
	 */
	public MeetingFrame(MeetingViewEntity existingMeeting) {
		meetingView = existingMeeting;
		buildMeetingFrame();
		setPropertiesForExisitngMeetingFrameFields();
		setExistingValuesToMeetingFrameFields(meetingView);
	}

	private Vector<String> buildTimeArrayForJComboBoxes() {
		DecimalFormat df = new DecimalFormat("00.00");
		Vector<String> timeArray = new Vector<>();
		String am = " AM";
		String pm = " PM";
		timeArray.add(df.format(new Double(12.00)).replace('.',':') + am);
		timeArray.add(df.format(new Double(12.30)).replace('.',':') + am);
		for (double i = 01.00; i < 12.00; i = i + 1.00) {
			timeArray.add(df.format(new Double(i)).replace('.',':') + am);
			timeArray.add(df.format(new Double(i + 0.30)).replace('.',':') + am);
		}
		timeArray.add(df.format(new Double(12.00)).replace('.',':') + pm);
		timeArray.add(df.format(new Double(12.30)).replace('.',':') + pm);
		for (double i = 01.00; i < 12.00; i = i + 1.00) {
			timeArray.add(df.format(new Double(i)).replace('.',':') + pm);
			timeArray.add(df.format(new Double(i + 0.30)).replace('.',':') + pm);
		}
		return timeArray;
	}

	private void buildMeetingFrame() {
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

		sendItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
				InputEvent.CTRL_DOWN_MASK));
		fileMenuBarEntry.add(sendItem);

		exitItem.setAccelerator(KeyStroke.getKeyStroke("ESCAPE"));

		fileMenuBarEntry.add(exitItem);

		sendItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendMeeting();
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
				sendMeeting();
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
		meetingPanel.setPreferredSize(new Dimension(600, 00));

		meetingPanel.add(toLabel);
		meetingPanel.add(toTextField);
		meetingPanel.add(subjectLabel);
		meetingPanel.add(subjectTextField);
		meetingPanel.add(locationLabel);
		meetingPanel.add(locationTextField);

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

	/**
	 * Exit message.
	 */
	private void ExitMessage() {

		this.dispose();
	}

	// Actions - Send
	/**
	 * Postcondition: It sends the meeting to the attendees.
	 * it first validates the fields, sends the meeting,
	 * refreshes the table view with latest data. 
	 * Send email.
	 */
	private void sendMeeting() {
		// if it's a new meeting, allow to be disconnected 
		MeetingViewEntity mve = buildMeetingViewObjectFromUIFields();
		if (!validateEmailFields(mve))
			return;

		if (newMeeting()) {
			meetingService.sendMeeting(mve);
			mainClient.updateMeetingsTable();
			this.dispose();			
		}
		
		// for meeting updates, the client MUST BE CONNECTED		
		else {
			if (!NetworkHelper.isConnectedToServer()) {
				JOptionPane.showMessageDialog(null, "Please connect to the server before sending meeting updates.");
				return;
			}		
			else {
				// build the change set
				
				MeetingViewFieldChanges meetingViewChanges = meetingService.sendUpdate(meetingView, mve);
				
				if (meetingViewChanges.isNoChangesFromUser()) {
					JOptionPane.showMessageDialog(null, "No changes were made");
				}
				
				else if (meetingViewChanges.isNoResponse()) {
					JOptionPane.showMessageDialog(null, "Unable to contact server. Please seek an Administrator.");
				}
				
				else if (meetingViewChanges.isAccepted()) {
					meetingService.updateMeeting(mve);
					mainClient.updateMeetingsTable();
					this.dispose();	
				}
				
				else if (meetingViewChanges.isRejected()) {
					// this is the case where the server has returned conflict. Thus we need to 
					// resolve the currently stored meetingView entity;
					if (!meetingView.getEndDate().equals(meetingViewChanges.valuesFromServer.getEndDate())) {
						JOptionPane.showMessageDialog(null, "Server changed some dates. Please verify. ");	
					}
					meetingView = meetingViewChanges.valuesFromServer; // this contains the data from the server
					meetingView.setId(meetingViewChanges.meetingID);
					MeetingViewEntity merged = meetingService.merge(meetingViewChanges);
					setExistingValuesToMeetingFrameFields(merged);
				}

			}
		}
	}

	private MeetingViewEntity buildMeetingViewObjectFromUIFields() {
		MeetingViewEntity updatedMeetingView = new MeetingViewEntity();
		
		if (newMeeting()) {
			updatedMeetingView.setId(id);
		}
		else {
			updatedMeetingView.setId(meetingView.getId());
		}

		updatedMeetingView.setAttendees(toTextField.getText().trim());
		updatedMeetingView.setSubject(subjectTextField.getText());
		updatedMeetingView.setPlace(locationTextField.getText().trim());
		updatedMeetingView.setStartDate(startDateTextField.getText().trim());
		updatedMeetingView.setEndDate(endDateTextField.getText().trim());
		updatedMeetingView.setStartTime(startTimeComboBox.getSelectedItem().toString());
		updatedMeetingView.setEndTime(endTimeComboBox.getSelectedItem().toString());
		updatedMeetingView.setBody(bodyTextField.getText());
		
		return updatedMeetingView;
	}

	private boolean newMeeting() {
		return (null == meetingView.getId());
	}

	/**
	 * Validate email fields.
	 * @param newMeetingView 
	 * 
	 * @return true, if successful
	 */
	private boolean validateEmailFields(MeetingViewEntity newMeetingView) {
		if (!emailValidator.isValidTo(newMeetingView.getAttendees())) {
			JOptionPane.showMessageDialog(null,
					"One address is not properly formatted. Please recheck");
			return false;
		}

		if (!emailValidator.isValidDates(newMeetingView.getStartDate(),
				newMeetingView.getEndDate())) {
			JOptionPane
					.showMessageDialog(null,
							"One of the Date is not properly formatted. Please recheck");
			return false;
		}

		if (!emailValidator.hasNotPassedDates(newMeetingView.getStartDate(), newMeetingView.getStartTime(),
				newMeetingView.getEndDate(), newMeetingView.getEndTime())) {
			JOptionPane.showMessageDialog(null,
					"One of the Date is past date. Please recheck");
			return false;
		}

		if (!emailValidator.isStartTimeAndEndTimeInOrder(
				newMeetingView.getStartDate(), newMeetingView.getStartTime(),
				newMeetingView.getEndDate(), newMeetingView.getEndTime())) {
			JOptionPane
					.showMessageDialog(null,
							"Start Time should not be less than or equal to End Time. Please recheck");
			return false;
		}
		
		/*if(!emailValidator.isValidLocation(meetingView.getPlace())){
			JOptionPane
			.showMessageDialog(null,
					"Please specify Location for the field.");
			return false;
		}*/

		return true;
	}

	private void setExistingValuesToMeetingFrameFields(MeetingViewEntity mve) { 
		this.id = meetingView.getId();
		this.toTextField.setText(mve.getAttendees());
		this.locationTextField.setText(mve.getPlace());
		this.subjectTextField.setText(mve.getSubject());
		this.startDateTextField.setText(mve.getStartDate());
		this.endDateTextField.setText(mve.getEndDate());
		this.startTimeComboBox.setSelectedItem(mve.getStartTime());
		this.endTimeComboBox.setSelectedItem(mve.getEndTime());
		this.bodyTextField.setText(mve.getBody());
	}

	private void setPropertiesForExisitngMeetingFrameFields() {
		subjectTextField.setDocument(new EntryFieldMaxLength(max_Length));

		sendButton.setText("Send Update>>");
		sendButton.setVisible(true);

		sendItem.setText("Send Update");
		sendItem.setVisible(true);

		toTextField.setEditable(true);
		locationTextField.setEditable(true);
		subjectTextField.setEditable(true);
		bodyTextField.setEditable(true);
		startDateTextField.setEditable(true);
		endDateTextField.setEditable(true);
	}

	private void setPropertiesForNewMeetingFrameFields() {
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
