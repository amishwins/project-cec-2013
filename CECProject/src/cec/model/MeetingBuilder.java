package cec.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import cec.config.CECConfigurator;


// TODO: Auto-generated Javadoc
/**
 * A builder of meeting instances. 
 * 
 */
public class MeetingBuilder {

	/** The id field. */
	private UUID id;

	/** The from field. */
	private String from = "";

	/** The attendees field. */
	private String attendees = "";

	/** The start date. */
	private String startDate = "";

	/** The end date. */
	private String endDate = "";

	/** The start time. */
	private String startTime = "";

	/** The end time. */
	private String endTime = "";

	/** The place. */
	private String place = "";

	/** The subject field. */
	private String subject = "";

	/** The body field. */
	private String body = "";

	/** The lastModifiedTime field. */
	private String lastModifiedTime = "";

	/** The sentTime field. */
	private String sentTime = "";

	/** The parentFolder field. */
	private Folder parentFolder;

	
	/**
	 * sets the value of id based on what is 
	 * specified in the argument id.
	 *
	 * @param id the id
	 * @return the meeting builder
	 */
	public MeetingBuilder withId(UUID id) {
		this.id = id;
		return this;
	}

	/**
	 * sets the value of from based on what is
	 * specified in the argument named from.
	 *
	 * @param from the from
	 * @return the meeting builder
	 */
	public MeetingBuilder withFrom(String from) {
		this.from = from;
		return this;
	}

	/**
	 * sets the value of To field based on what is
	 * specified in the argument named to.
	 *
	 * @param attendees the attendees
	 * @return the meeting builder
	 */
	public MeetingBuilder withAttendees(String attendees) {
		this.attendees = attendees;
		return this;
	}

	/**
	 * sets the value of cc field based on what is
	 * specified in the argument named cc.
	 *
	 * @param startDate the start date
	 * @return the meeting builder
	 */
	public MeetingBuilder withStartDate(String startDate) {
		this.startDate = startDate;
		return this;
	}

	/**
	 * sets the value of cc field based on what is
	 * specified in the argument named cc.
	 *
	 * @param endDate the end date
	 * @return the meeting builder
	 */
	public MeetingBuilder withEndDate(String endDate) {
		this.endDate = endDate;
		return this;
	}
	
	/**
	 * sets the value of cc field based on what is
	 * specified in the argument named cc.
	 *
	 * @param startTime the start time
	 * @return the meeting builder
	 */
	public MeetingBuilder withStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}
	
	/**
	 * sets the value of cc field based on what is
	 * specified in the argument named cc.
	 *
	 * @param endTime the end time
	 * @return the meeting builder
	 */
	public MeetingBuilder withEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}
	
	/**
	 * sets the value of cc field based on what is
	 * specified in the argument named cc.
	 *
	 * @param place the place
	 * @return the meeting builder
	 */
	public MeetingBuilder withPlace(String place) {
		this.place = place;
		return this;
	}
	
	/**
	 * sets the value of subject field based on what is 
	 * specified in the argument named subject.
	 *
	 * @param subject the subject
	 * @return the meeting builder
	 */
	public MeetingBuilder withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	/**
	 * sets the value of body field based on what is 
	 * specified in the argument named body.
	 * 
	 * @param body the body
	 * @return the meeting builder
	 */
	public MeetingBuilder withBody(String body) {
		this.body = body;
		return this;
	}

	/**
	 * sets the value of lastModifiedTime field based on what is
	 * specified in the argument named lastModifiedTime.
	 *
	 * @param lastModifiedTime the last modified time
	 * @return the meeting builder
	 */
	public MeetingBuilder withLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
		return this;
	}

	/**
	 * sets the value of sentTime field based on what is 
	 * specified in the argument named sentTime.
	 *
	 * @param sentTime the sent time
	 * @return the meeting builder
	 */
	public MeetingBuilder withSentTime(String sentTime) {
		this.sentTime = sentTime;
		return this;
	}

	/**
	 * sets the value of parentFolder field based on what is 
	 * specified in the argument named parentFolder.
	 *
	 * @param parentFolder the parent folder
	 * @return the meeting builder
	 */
	public MeetingBuilder withParentFolder(Folder parentFolder) {
		this.parentFolder = parentFolder;
		return this;
	}

	/**
	 * Computes the id of the type UUID type.
	 *
	 * @return the meeting builder
	 */
	public MeetingBuilder computeID() {
		this.id = UUID.randomUUID();
		return this;
	}

	/**
	 * Compute last modified time. 
	 *
	 * @return the meeting builder
	 */
	public MeetingBuilder computelastModifiedTime() {
		this.lastModifiedTime = currentDateTime();
		return this;
	}

	/**
	 * Compute sent time.
	 *
	 * @return the meeting builder
	 */
	public MeetingBuilder computeSentTime() {
		this.sentTime = currentDateTime();
		return this;
	}

	
	/**
	 * Returns the current date time according to the 
	 * format specified by the configurator.
	 *
	 * @return the string
	 */
	private String currentDateTime() {
		SimpleDateFormat currentDateTime = new SimpleDateFormat(CECConfigurator
				.getReference().get("DateFormat"));
		return currentDateTime.format(new Date());
	}
	

	/**
	 * returns the meeting builder object.
	 * based on the values in the
	 * argument named fields.
	 *
	 * @param fields the fields
	 * @return the meeting builder
	 */
	public MeetingBuilder load(Map<String, String> fields) {
		if (fields.get("Id") == null) {
			throw new RuntimeException();
		}
		return this
				.withId(UUID.fromString(fields.get("Id")))
				.withFrom(fields.get("From"))
				.withAttendees(fields.get("Attendees"))
				.withStartDate(fields.get("MeetingStartDate"))
				.withEndDate(fields.get("MeetingEndDate"))
				.withStartTime(fields.get("MeetingStartTime"))
				.withEndTime(fields.get("MeetingEndTime"))
				.withPlace(fields.get("MeetingPlace"))
				.withSubject(fields.get("Subject"))
				.withBody(fields.get("Body"))
				.withLastModifiedTime(fields.get("LastModifiedTime"))
				.withSentTime(fields.get("SentTime"))
				.withParentFolder(
						FolderFactory.getFolder(fields.get("ParentFolder")));
	}


	/**
	 * sets the value of FROM field to specified in the configurator object.
	 *
	 * @return the meeting builder
	 */
	public MeetingBuilder withFrom() {
		this.from = CECConfigurator.getReference().get("ClientEmail");
		return this;
	}

	/**
	 * returns the MeetingImpl object.
	 *
	 * @return the meeting
	 */
	public Meeting build() {
		return new MeetingImpl(id, from, attendees, startDate, endDate, startTime,
				endTime, place, subject, body, lastModifiedTime, sentTime, parentFolder);
	}
}
