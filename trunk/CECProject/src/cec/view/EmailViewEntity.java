package cec.view;

import java.util.UUID;

/**
 * Representation of the Email object in the View Layer
 * 
 * */
public class EmailViewEntity {
	
	/** The id. */
	private UUID id;
	
	/** The from. */
	private String from = "";
	
	/** The to. */
	private String to = "";
	
	/** The cc. */
	private String cc = "";
	
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
	 * @param from the new from
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Gets the to.
	 *
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 *
	 * @param to the new to
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Gets the cc.
	 *
	 * @return the cc
	 */
	public String getCC() {
		return cc;
	}

	/**
	 * Sets the cc.
	 *
	 * @param cc the new cc
	 */
	public void setCC(String cc) {
		this.cc = cc;
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
	 * @param subject the new subject
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
	 * @param body the new body
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
	 * @param lastModifiedTime the new last modified time
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
	 * @param sentTime the new sent time
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
	 * @param folder the new folder
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
	 * @param id the new id
	 */
	public void setId(UUID id) {
		this.id= id;
	}
	

}
