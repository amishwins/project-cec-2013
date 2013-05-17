/**
 * @author Pankaj Kapania
 */

package cec.model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cec.persistence.EmailDao;
import cec.persistence.EmailDaoFactory;

public class EmailImpl implements Email{
    private final UUID id;
	private String from;
	private String to;
	private String cc;
	private String subject;
	private String body;
	private String lastModifiedTime;
	private String sentTime;
	private Folder parentFolder;
	
	protected EmailDao emailDao;

	public EmailImpl(UUID id, String from, String to, String cc, String subject, String body, String lastModifiedTime, String sentTime, Folder parentFolder) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.subject = subject;
		this.body = body;
		this.lastModifiedTime=lastModifiedTime;
		this.sentTime = sentTime;
		this.parentFolder = parentFolder;
		setEmailDao(EmailDaoFactory.getNewMessageDao());
	}
	
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

	public String getSentTime() {
		return sentTime;
	}
	
	public Folder getParentFolder() {
		return parentFolder;
	}
	

    public void send() {
		// Assumption that email has been sent successfully..
		emailDao.save(id, from, to, cc, subject, body, lastModifiedTime ,sentTime, "emails/Outbox");
	}

	public void saveToDraftFolder() {
		emailDao.save(id, from, to, cc, subject, body, lastModifiedTime ,sentTime, "emails/Draft");
	}
	
	@Override
	public String toString(){
		StringBuilder email = new StringBuilder();
		email.append("Email {");
		email.append("id: " + this.getId());
		email.append(", to: " + this.getTo());
		email.append(", cc: " + this.getCC());
		email.append(", subject: " + this.getSubject());
		email.append(", body: " + this.getBody());
		email.append(", lastModifiedTime: " + this.getLastModifiedTime());
		email.append(", sentTime: " + this.getSentTime());
		email.append(", getParentFolder: " + getParentFolder().toString());
		email.append("}");
		return email.toString();
	}

	@Override
	public int compareTo(Email anotherEmail) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy.MM.dd_'At'_HH.mm.ss.SSS");
		Date currentEmailDate = new Date();
		Date anotherEmailDate = new Date();
		
		try {
			 currentEmailDate = dateFormat.parse((this.getLastModifiedTime()));
			 anotherEmailDate = dateFormat.parse((anotherEmail.getLastModifiedTime()));
			   
		} catch (ParseException e) {
			handleParseException(e);
		}
		return anotherEmailDate.compareTo(currentEmailDate);
	}
	
	protected void handleParseException(Exception e) {
		e.printStackTrace();
	}

}
