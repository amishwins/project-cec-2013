package cec.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cec.config.CECConfigurator;
import cec.persistence.EmailDao;
import cec.persistence.EmailDaoFactory;
import exceptions.SourceAndDestinationFoldersAreSameException;

/**
 * EmailImpl is the concrete implementation of the email interface.
 * it is one of the core domain object of our application.
 * 
 */
public class EmailImpl implements Email {
	
	/** The id. */
	private final UUID id;
	
	/** The from. */
	private String from;
	
	/** The to. */
	private String to;
	
	/** The cc. */
	private String cc;
	
	/** The subject. */
	private String subject;
	
	/** The body. */
	private String body;
	
	/** The last modified time. */
	private String lastModifiedTime;
	
	/** The sent time. */
	private String sentTime;
	
	/** The parent folder. */
	private Folder parentFolder;

	/** The email dao. */
	protected EmailDao emailDao;

	/**
	 * Instantiates a new email impl.
	 *
	 * @param id the id
	 * @param from the from
	 * @param to the to
	 * @param cc the cc
	 * @param subject the subject
	 * @param body the body
	 * @param lastModifiedTime the last modified time
	 * @param sentTime the sent time
	 * @param parentFolder the parent folder
	 */
	public EmailImpl(UUID id, String from, String to, String cc,
			String subject, String body, String lastModifiedTime,
			String sentTime, Folder parentFolder) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.subject = subject;
		this.body = body;
		this.lastModifiedTime = lastModifiedTime;
		this.sentTime = sentTime;
		this.parentFolder = parentFolder;
		setEmailDao(EmailDaoFactory.getEmailDaoInstance());
	}

	/**
	 * Sets the email dao.
	 *
	 * @param emailDao the new email dao
	 */
	protected void setEmailDao(EmailDao emailDao) {
		this.emailDao = emailDao;
	}

	
	public UUID getId() {
		return id;
	}

	
	public String getTo() {
		return to;
	}

	
	public String getFrom() {
		return from;
	}

	
	public String getCC() {
		return cc;
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
	 * returns the last modified time value in a more readable format. 
	 * Converts the LastModifiedTime field value from "yyyy.MM.dd_'At'_HH.mm.ss.SSS" format to 
	 * "EEE, MMM d, yyyy" format.
	 * 	 *
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
	 * save the email object to Outbox folder. Currently it does not have the send functionality, 
	 * it just saves the file assuming that file has reached its destination.
	 *
	 * @param destDir the dest dir
	 */
	public void send() {

		// Assumption that email has been sent successfully..
		emailDao.save(id, from, to, cc, subject, body, lastModifiedTime,
				sentTime, CECConfigurator.getReference().get("Outbox"));
		ifItWasInDraftFolderDeleteThatCopy();
	}

	/**
	 * This method is responsible for deleting the existing copy of an email from the Drafts folder
	 * after the email has been sent to its destination address. it makes sure that there are no two copies of same email
	 * in two different folders. 
	 */
	private void ifItWasInDraftFolderDeleteThatCopy() {
		if (parentFolder.getPath().equals(
				CECConfigurator.getReference().get("Drafts"))) {
			emailDao.delete(parentFolder.getPath(), id);
		}
	}

	/* (non-Javadoc)
	 * @see cec.model.Email#saveToDraftFolder()
	 */
	public void saveToDraftFolder() {
		emailDao.save(id, from, to, cc, subject, body, lastModifiedTime,
				sentTime, CECConfigurator.getReference().get("Drafts"));
	}

	/* (non-Javadoc)
	 * @see cec.model.Email#delete()
	 */
	public void delete() {
		emailDao.delete(parentFolder.getPath(), id);
	}

	/* (non-Javadoc)
	 * @see cec.model.Email#move(cec.model.Folder)
	 */
	public void move(Folder destFolder) {
		checkDestinationFolderIsNotSourceFolder(destFolder);
		emailDao.move(id, parentFolder.getPath(), destFolder.getPath());
	}
	
	/**
	 * Check destination folder is not source folder.
	 *
	 * @param destDir the dest dir
	 */
	private void checkDestinationFolderIsNotSourceFolder(Folder destDir){
		if(parentFolder.getPath().equals(destDir.getPath())){
			throw new SourceAndDestinationFoldersAreSameException();
		}
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder email = new StringBuilder();
		email.append("Email {");
		email.append("\n\tid: " + this.getId());
		email.append(", \n\tto: " + this.getTo());
		email.append(", \n\tcc: " + this.getCC());
		email.append(", \n\tsubject: " + this.getSubject());
		email.append(", \n\tbody: " + this.getBody());
		email.append(", \n\tlastModifiedTime: " + this.getLastModifiedTime());
		email.append(", \n\tsentTime: " + this.getSentTime());
		email.append(", \n\tgetParentFolder: " + getParentFolder().toString());
		email.append("\n}");
		return email.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Email anotherEmail) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(CECConfigurator
				.getReference().get("DateFormat"));
		Date currentEmailDate = new Date();
		Date anotherEmailDate = new Date();

		try {
			currentEmailDate = dateFormat.parse((this.getLastModifiedTime()));
			anotherEmailDate = dateFormat.parse((anotherEmail
					.getLastModifiedTime()));

		} catch (ParseException e) {
			handleParseException(e);
		}

		return anotherEmailDate.compareTo(currentEmailDate);
	}

	/**
	 * Handle parse exception.
	 *
	 * @param e the e
	 */
	protected void handleParseException(Exception e) {
		e.printStackTrace();
	}
}
