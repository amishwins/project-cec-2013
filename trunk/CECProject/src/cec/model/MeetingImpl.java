package cec.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cec.config.CECConfigurator;
import cec.persistence.MeetingDao;
import cec.persistence.MeetingDaoFactory;

/**
 * MeetingImpl is the concrete implementation of the email interface. it is one of
 * the core domain object of our application.
 * 
 */
public class MeetingImpl implements Meeting {

	public MeetingDao getMeetingDao() {
		return meetingDao;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setAttendees(String attendees) {
		this.attendees = attendees;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public void setSentTime(String sentTime) {
		this.sentTime = sentTime;
	}

	public void setParentFolder(Folder parentFolder) {
		this.parentFolder = parentFolder;
	}

	/** The id field. */
	private final UUID id;

	/** The from field. */
	private String from;

	/** The attendees field. */
	private String attendees;

	/** The start date. */
	private String startDate;

	/** The end date. */
	private String endDate;

	/** The start time. */
	private String startTime;

	/** The end time. */
	private String endTime;

	/** The place. */
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

	/** The meetingDao field. */
	protected MeetingDao meetingDao;

	/**
	 * Instantiates a new meeting impl.
	 *
	 * @param id the id
	 * @param from the from
	 * @param attendees the attendees
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param place the place
	 * @param subject the subject
	 * @param body the body
	 * @param lastModifiedTime the last modified time
	 * @param sentTime the sent time
	 * @param parentFolder the parent folder
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
	 * Sets the meeting dao.
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

	/**
	 * Gets the start or end date nicely formatted.
	 *
	 * @param startOrEndDate the start or end date
	 * @return the start or end date nicely formatted
	 */
	private String getStartOREndDateNicelyFormatted(String startOrEndDate) {
		if (startOrEndDate == null || startOrEndDate.equals(""))
			return "";
		Date sOrEDate = new Date();

		SimpleDateFormat sourceFormat = new SimpleDateFormat(CECConfigurator
				.getReference().get("DateFormatForMeetingFields"));

		try {
			sOrEDate = sourceFormat.parse(startOrEndDate);
		} catch (ParseException e) {
			// if the date is poorly formatted, throw a runtime exception
			throw new RuntimeException();
		}
		SimpleDateFormat targetFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
		return targetFormat.format(sOrEDate);
	}

	
	public String getSentTime() {
		return sentTime;
	}


	public Folder getParentFolder() {
		return parentFolder;
	}

	/**
	 * This method is responsible for communicating the persistence layer that
	 * save the meeting object to Meetings folder. Currently it does not have the
	 * send functionality, it just saves the file assuming that file has reached
	 * its destination.
	 * Precondition: meeting does not exist in the system.
	 * Postcondition: meeting object persists on the system. and email sent to the 
	 * attendees. 
	 * 
	 */
	public void send() {
		saveToPersistence();
		String subjectPrefix = "Meeting Request: ";
		sendEmailToAttendeesForMeeting(subjectPrefix);
	}	
	
	@Override
	public void saveAfterAccept() {
		saveToPersistence();
	}

	private void saveToPersistence() {
		meetingDao.save(id, from, attendees, startDate, endDate, startTime,
				endTime, place, subject, body, lastModifiedTime, sentTime,
				CECConfigurator.getReference().get("Meetings"));
	}
	
	/**
	 * Send email to attendees for inviting into the meeting.
	 *
	 * @param subjectPrefix the subject prefix
	 */
	private void sendEmailToAttendeesForMeeting(String subjectPrefix) {
		Email email = buildEmailFromMeeting(subjectPrefix);
		email.send();
	}

	/**
	 * Builds the email from meeting.
	 *
	 * @param subjectPrefix the subject prefix
	 * @return the email
	 */
	private Email buildEmailFromMeeting(String subjectPrefix) {
		EmailBuilder emailBuilder = new EmailBuilder();
		Email emailToSendForMeeting = emailBuilder.withId(id).withFrom(from)
				.withTo(attendees).withSubject(subjectPrefix + subject)
				.withBody(buildBodyPartOfEmailToBeSentForMeeting(subjectPrefix))
				.computelastModifiedTime().computeSentTime()
				.withIsMeetingEmail(true)
				.withOutboxParentFolder().build();
		return emailToSendForMeeting;
	}

	/**
	 * Builds the body part of email to be sent for meeting.
	 *
	 * @param subjectPrefix the subject prefix
	 * @return the string
	 */
	private String buildBodyPartOfEmailToBeSentForMeeting(String subjectPrefix) {
		StringBuilder buildBody = new StringBuilder();
		String nextLine = "\n";
		buildBody.append(subjectPrefix + nextLine);
		buildBody.append("---------------" + nextLine);		
		buildBody.append("Subject:    " + subject + nextLine);
		buildBody.append("Location:   " + place + nextLine);
		buildBody.append("Start Date: "
				+ getStartOREndDateNicelyFormatted(startDate));
		buildBody.append(" at: " + startTime + nextLine);
		buildBody.append("End Date:   "
				+ getStartOREndDateNicelyFormatted(endDate));
		buildBody.append(" at: " + endTime + nextLine);
		buildBody.append("--------------------------------------------" + nextLine);
		buildBody.append(body + nextLine);
		return buildBody.toString();
	}

	/**
	 * This method is responsible for communicating the persistence layer that
	 * delete the meeting object from the System.
	 * PostCondition: 1. Meeting Object has been deleted from the system. 
	 * 2. Cancellation Meeting Request email has been sent to client.
	 * 
	 */
	public void delete() {
		meetingDao.delete(parentFolder.getPath(), id);
	}

	/**
	 * Returns a string representation for this meeting object.
	 *
	 * @return the string
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
	 * It compares the this meeting object to another meeting object on the basis of
	 * lastModifiedTime field. it provides the basis for sorting the meeting
	 * objects.
	 *
	 * @param anotherMeeting the another meeting
	 * @return the int
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
	 *           
	 */
	protected void handleParseException(Exception e) {
		e.printStackTrace();
	}

	
}
