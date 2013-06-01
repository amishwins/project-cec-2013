package cec.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

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
	
	/** The id field. */
	private final UUID id;
	
	/** The from field. */
	private String from;
	
	/** The to field. */
	private String to;
	
	/** The cc field. */
	private String cc;
	
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
	 * It returns the last modified time value in a more readable format. 
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
	 * 
	 */
	public void send() {

		// Assumption that email has been sent successfully..
		emailDao.save(id, from, to, cc, subject, body, lastModifiedTime,
				sentTime, CECConfigurator.getReference().get("Outbox"));
		ifItWasInDraftFolderDeleteThatCopy();
	}

	/**
	 * This method is responsible for deleting the existing copy of an email from the Drafts folder
	 * after the email has been sent to its destination address and saved to Outbox folder. 
	 * it method ensures that there are no two copies of same email object
	 * in two different folders(Outbox and Drafts). basically it prevents system to have email objects 
	 * with the same id in 2 different folders.
	 */
	private void ifItWasInDraftFolderDeleteThatCopy() {
		if (parentFolder.getPath().equals(
				CECConfigurator.getReference().get("Drafts"))) {
			emailDao.delete(parentFolder.getPath(), id);
		}
	}
	
	
	/**
	 * This method is responsible for communicating the persistence layer that 
	 * save the email object to Drafts folder. it just saves the file 
	 * to drafts folder.
	 *
	 * 
	 */
	public void saveToDraftFolder() {
		emailDao.save(id, from, to, cc, subject, body, lastModifiedTime,
				sentTime, CECConfigurator.getReference().get("Drafts"));
	}

	/**
	 * This method is responsible for communicating the persistence layer that 
	 * delete the email object from the System.
	 *
	 */
	public void delete() {
		emailDao.delete(parentFolder.getPath(), id);
	}

	
	/**
	 * This method is responsible for communicating the persistence layer that 
	 * move the email object to destination folder.
	 *
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
	
	/**
	 * Returns a string representation for this email object. 
	 * move the email object to destination folder.
	 *
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

	/**
	 * It compares the this email object to another email object on 
	 * the basis of lastModifiedTime field. 
	 * it provides the basis for sorting the email objects. 
	 * 
	 *
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

public boolean isMatch(String pattern) {
	
		//boolean match;// = false;
		StringBuilder entireEmail = new StringBuilder();
		String space = " ";
		entireEmail.append(from);
		entireEmail.append(space+cc);
		entireEmail.append(space+subject);
		entireEmail.append(space+body);

		Search emailSearcher = new Search(entireEmail.toString(),pattern);	
		return emailSearcher.isMatch();
			
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
