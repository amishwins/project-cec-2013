package cec.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import cec.config.CECConfigurator;

/**
 * A builder of email instances. 
 * @author Pankaj Kapania
 */
public class EmailBuilder {

	/** The id. */
	UUID id;
	
	/** The from. */
	String from = "";
	
	/** The to. */
	String to = "";
	
	/** The cc. */
	String cc = "";
	
	/** The subject. */
	String subject = "";
	
	/** The body. */
	String body = "";
	
	/** The last modified time. */
	String lastModifiedTime = "";
	
	/** The sent time. */
	String sentTime = "";
	
	/** The parent folder. */
	Folder parentFolder;

	/**
	 * 
	 *
	 * @param id the id
	 * @return the email builder
	 */
	public EmailBuilder withId(UUID id) {
		this.id = id;
		return this;
	}

	/**
	 * With from.
	 *
	 * @param from the from
	 * @return the email builder
	 */
	public EmailBuilder withFrom(String from) {
		this.from = from;
		return this;
	}

	/**
	 * With to.
	 *
	 * @param to the to
	 * @return the email builder
	 */
	public EmailBuilder withTo(String to) {
		this.to = to;
		return this;
	}

	/**
	 * With cc.
	 *
	 * @param cc the cc
	 * @return the email builder
	 */
	public EmailBuilder withCC(String cc) {
		this.cc = cc;
		return this;
	}

	/**
	 * With subject.
	 *
	 * @param subject the subject
	 * @return the email builder
	 */
	public EmailBuilder withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	/**
	 * With body.
	 *
	 * @param body the body
	 * @return the email builder
	 */
	public EmailBuilder withBody(String body) {
		this.body = body;
		return this;
	}

	/**
	 * With last modified time.
	 *
	 * @param lastModifiedTime the last modified time
	 * @return the email builder
	 */
	public EmailBuilder withLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
		return this;
	}

	/**
	 * With sent time.
	 *
	 * @param sentTime the sent time
	 * @return the email builder
	 */
	public EmailBuilder withSentTime(String sentTime) {
		this.sentTime = sentTime;
		return this;
	}

	/**
	 * With parent folder.
	 *
	 * @param parentFolder the parent folder
	 * @return the email builder
	 */
	public EmailBuilder withParentFolder(Folder parentFolder) {
		this.parentFolder = parentFolder;
		return this;
	}

	/**
	 * Compute id.
	 *
	 * @return the email builder
	 */
	public EmailBuilder computeID() {
		this.id = UUID.randomUUID();
		return this;
	}

	/**
	 * Computelast modified time.
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
	 * Current date time.
	 *
	 * @return the string
	 */
	private String currentDateTime() {
		SimpleDateFormat currentDateTime = new SimpleDateFormat(CECConfigurator
				.getReference().get("DateFormat"));
		return currentDateTime.format(new Date());
	}

	/**
	 * Load.
	 *
	 * @param fields the fields
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
	 * With drafts parent folder.
	 *
	 * @return the email builder
	 */
	public EmailBuilder withDraftsParentFolder() {
		this.parentFolder = FolderFactory.getFolder(CECConfigurator
				.getReference().get("Drafts"));
		return this;
	}

	/**
	 * With outbox parent folder.
	 *
	 * @return the email builder
	 */
	public EmailBuilder withOutboxParentFolder() {
		this.parentFolder = FolderFactory.getFolder(CECConfigurator
				.getReference().get("Outbox"));
		return this;
	}

	/**
	 * With from.
	 *
	 * @return the email builder
	 */
	public EmailBuilder withFrom() {
		this.from = CECConfigurator.getReference().get("ClientEmail");
		return this;
	}

	/**
	 * Builds the.
	 *
	 * @return the email
	 */
	public Email build() {
		return new EmailImpl(id, from, to, cc, subject, body, lastModifiedTime,
				sentTime, parentFolder);
	}
}
