package cec.view;

import java.util.UUID;

/**
 * Representation of the Meeting object in the View Layer
 * 
 * */
public class MeetingViewEntity {

	/** The id. */
	private UUID id;

	/** The from. */
	private String from = "";

	/** The to. */
	private String attendees = "";

	/** The startDate. */
	private String startDate = "";

	/** The endDate. */
	private String endDate = "";
	
	/** The startTime. */
	private String startTime = "";

	/** The endTime. */
	private String endTime = "";

	/** The place. */
	private String place = "";

	/** The subject. */
	private String subject = "";

	/** The body. */
	private String body = "";

	/** The last modified time. */
	private String lastModifiedTime = "";

	/** The sent time. */
	private String sentTime = "";

	/** The folder. */
	private String folder = "";

	/**
	 * Gets the from.
	 * 
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Sets the from.
	 * 
	 * @param from
	 *            the new from
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Gets the attendees.
	 * 
	 * @return the attendees.
	 */
	public String getAttendees() {
		return attendees;
	}

	/**
	 * Sets the attendees.
	 * 
	 * @param attendees
	 *            the new attendees
	 */
	public void setAttendees(String attendees) {
		this.attendees = attendees;
	}

	/**
	 * Gets the startDate.
	 * 
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * Sets the startDate.
	 * 
	 * @param startDate
	 *            the new startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the endDate.
	 * 
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * Sets the endDate.
	 * 
	 * @param endDate
	 *            the new endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Gets the startTime.
	 * 
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * Sets the startTime.
	 * 
	 * @param startTime
	 *            the new startTime
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the endTime.
	 * 
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * Sets the endTime.
	 * 
	 * @param endTime
	 *            the new endTime
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the place.
	 * 
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * Sets the place.
	 * 
	 * @param place
	 *            the new place
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * Gets the subject.
	 * 
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 * 
	 * @param subject
	 *            the new subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Gets the body.
	 * 
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Sets the body.
	 * 
	 * @param body
	 *            the new body
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * Gets the last modified time.
	 * 
	 * @return the last modified time
	 */
	public String getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * Sets the last modified time.
	 * 
	 * @param lastModifiedTime
	 *            the new last modified time
	 */
	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	/**
	 * Gets the sent time.
	 * 
	 * @return the sent time
	 */
	public String getSentTime() {
		return sentTime;
	}

	/**
	 * Sets the sent time.
	 * 
	 * @param sentTime
	 *            the new sent time
	 */
	public void setSentTime(String sentTime) {
		this.sentTime = sentTime;
	}

	/**
	 * Gets the folder.
	 * 
	 * @return the folder
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * Sets the folder.
	 * 
	 * @param folder
	 *            the new folder
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(UUID id) {
		this.id = id;
	}

}
