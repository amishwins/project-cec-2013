package cec.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import cec.config.CECConfigurator;


/**
 * A builder of email instances. 
 * 
 */
public class EmailBuilder {

	/** Unique identifiers for each email. */
	UUID id;
	
	/** The From field */
	String from = "";
	
	/** The To field. */
	String to = "";
	
	/** The CC field */
	String cc = "";
	
	/** The subject field */
	String subject = "";
	
	/** The body field*/
	String body = "";
	
	/** The lastModifiedTime field */
	String lastModifiedTime = "";
	
	/** The sent time field */
	String sentTime = "";
	
	/** The parentFolder field. */
	Folder parentFolder;
	
	Boolean isMeetingEmail = false;

	/**
	 * sets the value of id based on what is 
	 * specified in the argument id.
	 *
	 * @param id the id
	 * @return the email builder
	 */
	public EmailBuilder withId(UUID id) {
		this.id = id;
		return this;
	}

	/**
	 * sets the value of from based on what is 
	 * specified in the argument named from
	 *
	 * @param from the from
	 * @return the email builder
	 */
	public EmailBuilder withFrom(String from) {
		this.from = from;
		return this;
	}

	/**
	 * sets the value of To field based on what is 
	 * specified in the argument named to.
	 *
	 * @param to the to
	 * @return the email builder
	 */
	public EmailBuilder withTo(String to) {
		this.to = to;
		return this;
	}

	/**
	 * sets the value of cc field based on what is 
	 * specified in the argument named cc.
	 *
	 * @param cc the cc
	 * @return the email builder
	 */
	public EmailBuilder withCC(String cc) {
		this.cc = cc;
		return this;
	}

	/**
	 * sets the value of subject field based on what is 
	 * specified in the argument named subject.
	 *
	 * @param subject the subject
	 * @return the email builder
	 */
	public EmailBuilder withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	/**
	 * sets the value of body field based on what is 
	 * specified in the argument named body.
	 * 
	 * @param body the body
	 * @return the email builder
	 */
	public EmailBuilder withBody(String body) {
		this.body = body;
		return this;
	}

	/**
	 * sets the value of lastModifiedTime field based on what is 
	 * specified in the argument named lastModifiedTime
	 * 
	 * @param lastModifiedTime the last modified time
	 * @return the email builder
	 */
	public EmailBuilder withLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
		return this;
	}

	/**
	 * sets the value of sentTime field based on what is 
	 * specified in the argument named sentTime.
	 *
	 * @param sentTime the sent time
	 * @return the email builder
	 */
	public EmailBuilder withSentTime(String sentTime) {
		this.sentTime = sentTime;
		return this;
	}

	/**
	 * sets the value of parentFolder field based on what is 
	 * specified in the argument named parentFolder.
	 *
	 * @param parentFolder the parent folder
	 * @return the email builder
	 */
	public EmailBuilder withParentFolder(Folder parentFolder) {
		this.parentFolder = parentFolder;
		return this;
	}

	public EmailBuilder withIsMeetingEmail(Boolean isMeetingEmail) {
		this.isMeetingEmail = isMeetingEmail;
		return this;
	}

	
	/**
	 * Computes the id of the type UUID type.
	 *
	 * @return the email builder
	 */
	public EmailBuilder computeID() {
		this.id = UUID.randomUUID();
		return this;
	}

	/**
	 * Compute last modified time. 
	 *
	 * @return the email builder
	 */
	public EmailBuilder computelastModifiedTime() {
		this.lastModifiedTime = currentDateTime();
		return this;
	}

	/**
	 * Compute sent time.
	 *
	 * @return the email builder
	 */
	public EmailBuilder computeSentTime() {
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
	 * returns the email builder object.
	 * based on the values in the 
	 * argument named fields.
	 *
	 * @param Map of the email fields. 
	 * @return the email builder
	 */
	public EmailBuilder load(Map<String, String> fields) {
		if (fields.get("Id") == null) {
			throw new RuntimeException();
		}
		return this
				.withId(UUID.fromString(fields.get("Id")))
				.withFrom(fields.get("From"))
				.withTo(fields.get("To"))
				.withCC(fields.get("CC"))
				.withSubject(fields.get("Subject"))
				.withBody(fields.get("Body"))
				.withLastModifiedTime(fields.get("LastModifiedTime"))
				.withSentTime(fields.get("SentTime"))
				.withParentFolder(
						FolderFactory.getFolder(fields.get("ParentFolder")));
	}

	/**
	 * sets the value of PARENTFOLDER to Drafts.
	 *
	 * @return the email builder
	 */
	public EmailBuilder withDraftsParentFolder() {
		this.parentFolder = FolderFactory.getFolder(CECConfigurator
				.getReference().get("Drafts"));
		return this;
	}

	/**
	 * sets the value of PARENTFOLDER to Outbox .
	 *
	 * @return the email builder
	 */
	public EmailBuilder withOutboxParentFolder() {
		this.parentFolder = FolderFactory.getFolder(CECConfigurator
				.getReference().get("Outbox"));
		return this;
	}

	/**
	 * sets the value of FROM field to specified in the configurator object.
	 *
	 * @return the email builder
	 */
	public EmailBuilder withFrom() {
		this.from = CECConfigurator.getReference().get("ClientEmail");
		return this;
	}

	/**
	 * returns the EmailImpl object.
	 *
	 * @return the email
	 */
	public Email build() {
		return new EmailImpl(id, from, to, cc, subject, body, lastModifiedTime,
				sentTime, parentFolder, isMeetingEmail);
	}
}
