package cec.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cec.config.CECConfigurator;
import cec.persistence.MeetingDao;
import cec.persistence.MeetingDaoFactory;

/**
 * EmailImpl is the concrete implementation of the email interface. it is one of
 * the core domain object of our application.
 * 
 */
public class MeetingImpl implements Meeting {

	/** The id field. */
	private final UUID id;

	/** The from field. */
	private String from;

	/** The attendees field. */
	private String attendees;

	private String startDate;

	private String endDate;

	private String startTime;

	private String endTime;

	private String place;

	/** The subject field. */
	private String subject;

	/** The body field. */
	private String body;

	/** The lastModifiedTime field. */
	private String lastModifiedTime;

	/** The sentTime field. */
	private String sentTime;

	/** The parentFolder field. */
	private Folder parentFolder;

	/** The emailDao field. */
	protected MeetingDao meetingDao;

	/**
	 * Instantiates a new email impl.
	 * 
	 * @param id
	 *            the id
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param subject
	 *            the subject
	 * @param body
	 *            the body
	 * @param lastModifiedTime
	 *            the last modified time
	 * @param sentTime
	 *            the sent time
	 * @param parentFolder
	 *            the parent folder
	 */
	public MeetingImpl(UUID id, String from, String attendees,
			String startDate, String endDate, String startTime, String endTime,
			String place, String subject, String body, String lastModifiedTime,
			String sentTime, Folder parentFolder) {
		this.id = id;
		this.from = from;
		this.attendees = attendees;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.place = place;
		this.subject = subject;
		this.body = body;
		this.lastModifiedTime = lastModifiedTime;
		this.sentTime = sentTime;
		this.parentFolder = parentFolder;
		setMeetingDao(MeetingDaoFactory.getMeetingDaoInstance());
	}

	/**
	 * Sets the email dao.
	 * 
	 * @param meetingDao
	 *            the new email dao
	 */
	protected void setMeetingDao(MeetingDao meetingDao) {
		this.meetingDao = meetingDao;
	}

	public UUID getId() {
		return id;
	}

	public String getAttendees() {
		return attendees;
	}

	public String getFrom() {
		return from;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getPlace() {
		return place;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public String getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * It returns the last modified time value in a more readable format.
	 * Converts the LastModifiedTime field value from
	 * "yyyy.MM.dd_'At'_HH.mm.ss.SSS" format to "EEE, MMM d, yyyy" format. *
	 * 
	 * @return the last modified time nicely formatted
	 */

	@Override
	public String getLastModifiedTimeNicelyFormatted() {
		if (this.getLastModifiedTime() == null
				|| this.lastModifiedTime.equals(""))
			return "";

		Date lastModified = new Date();

		SimpleDateFormat sourceFormat = new SimpleDateFormat(CECConfigurator
				.getReference().get("DateFormat"));

		try {
			lastModified = sourceFormat.parse(this.getLastModifiedTime());
		} catch (ParseException e) {
			// if the date is poorly formatted, throw a runtime exception
			throw new RuntimeException();
		}

		SimpleDateFormat targetFormat = new SimpleDateFormat("EEE, MMM d, yyyy");

		return targetFormat.format(lastModified);
	}

	public String getSentTime() {
		return sentTime;
	}

	public Folder getParentFolder() {
		return parentFolder;
	}

	/**
	 * This method is responsible for communicating the persistence layer that
	 * save the email object to Outbox folder. Currently it does not have the
	 * send functionality, it just saves the file assuming that file has reached
	 * its destination.
	 * 
	 * 
	 */
	public void send() {

		// Assumption that meeting email has been sent successfully..
		meetingDao.save(id, from, attendees, startDate, endDate, startTime,
				endTime, place, subject, body, lastModifiedTime, sentTime,
				CECConfigurator.getReference().get("Meetings"));
	}

	/**
	 * This method is responsible for communicating the persistence layer that
	 * delete the email object from the System.
	 * 
	 */
	public void delete() {
		meetingDao.delete(parentFolder.getPath(), id);
	}

	/**
	 * Returns a string representation for this email object. move the email
	 * object to destination folder.
	 * 
	 */

	@Override
	public String toString() {
		StringBuilder meeting = new StringBuilder();
		meeting.append("Meeting {");
		meeting.append("\n\t id: " + this.getId());
		meeting.append(", \n\t attendees: " + this.getAttendees());
		meeting.append(", \n\t startDate: " + this.getStartDate());
		meeting.append(", \n\t endDate: " + this.getEndDate());
		meeting.append(", \n\t startTime: " + this.getStartTime());
		meeting.append(", \n\t endTime: " + this.getEndTime());
		meeting.append(", \n\t place: " + this.getPlace());
		meeting.append(", \n\t subject: " + this.getSubject());
		meeting.append(", \n\t body: " + this.getBody());
		meeting.append(", \n\t lastModifiedTime: " + this.getLastModifiedTime());
		meeting.append(", \n\t sentTime: " + this.getSentTime());
		meeting.append(", \n\t getParentFolder: "
				+ getParentFolder().toString());
		meeting.append("\n}");
		return meeting.toString();
	}

	/**
	 * It compares the this email object to another email object on the basis of
	 * lastModifiedTime field. it provides the basis for sorting the email
	 * objects.
	 * 
	 * 
	 */
	@Override
	public int compareTo(Meeting anotherMeeting) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(CECConfigurator
				.getReference().get("DateFormat"));
		Date currentMeetingDate = new Date();
		Date anotherMeetingDate = new Date();

		try {
			currentMeetingDate = dateFormat.parse((this.getLastModifiedTime()));
			anotherMeetingDate = dateFormat.parse((anotherMeeting
					.getLastModifiedTime()));

		} catch (ParseException e) {
			handleParseException(e);
		}

		return anotherMeetingDate.compareTo(currentMeetingDate);
	}

	/**
	 * Handle parse exception.
	 * 
	 * @param e
	 *            the e
	 */
	protected void handleParseException(Exception e) {
		e.printStackTrace();
	}
}
