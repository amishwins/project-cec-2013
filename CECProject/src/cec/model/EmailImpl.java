/**
 * @author Pankaj Kapania
 */

package cec.model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cec.config.CECConfigurator;
import cec.persistence.EmailDao;
import cec.persistence.EmailDaoFactory;

public class EmailImpl implements Email {
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
	
	public Date getLastModifiedTimeUnformatted() {
		Date result = new Date();
		
/*		try {
			result 
		}*/
		
		return new Date();
	}

	public String getSentTime() {
		return sentTime;
	}
	
	public Folder getParentFolder() {
		return parentFolder;
	}	

    public void send() {
		// Assumption that email has been sent successfully..
		emailDao.save(id, from, to, cc, subject, body, lastModifiedTime,
				sentTime, CECConfigurator.getReference().get("Outbox"));
	}

	public void saveToDraftFolder() {
		emailDao.save(id, from, to, cc, subject, body, lastModifiedTime,
				sentTime, CECConfigurator.getReference().get("Drafts"));
	}
	
	public void delete(){
		emailDao.delete(parentFolder.getPath(), id);
	}
	
	public void move(Folder destDir){
		emailDao.move(id, parentFolder.getPath(), destDir.getPath());
	}
	
	
	@Override
	public String toString(){
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
