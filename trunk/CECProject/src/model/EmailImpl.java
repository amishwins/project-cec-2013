/**
 * @author Pankaj Kapania
 */

package model;

import persistence.EmailDao;
import persistence.NewMessageDaoFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class EmailImpl implements Email, Comparable<Email>{
    private final UUID id;
	private String from;
	private String to;
	private String cc;
	private String subject;
	private String body;
	private String lastModifiedTime;
	private String sentTime;
	private Folder parentFolder;
	
	private EmailDao newMessageDao;

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
		return to;
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
		newMessageDao = NewMessageDaoFactory.getNewMessageDao();
		// Assumption that email has been sent successfully..
		newMessageDao.save(to, cc, subject, body, sentTime, "Sent");
	}

	public void saveToDraftFolder() {
		newMessageDao = NewMessageDaoFactory.getNewMessageDao();
		newMessageDao.save(to, cc, subject, body, lastModifiedTime, "Draft");
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
			
			e.printStackTrace();
		}
		
		// this comment has been updated

		return currentEmailDate.compareTo(anotherEmailDate);
	}
	
	

}
