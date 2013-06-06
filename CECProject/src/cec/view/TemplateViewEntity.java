package cec.view;

/**
 * Representation of the Template object in the View Layer
 * 
 * */
public class TemplateViewEntity {
	
	/** The name. */	
	private String name = "";

	/** The to. */	
	private String to = "";

	/** The cc. */	
	private String cc = "";

	/** The subject. */	
	private String subject = "";

	/** The body. */	
	private String body = "";

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */	
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name.
	 */	
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the to.
	 * 
	 * @return the to.
	 */		
	public String getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 * 
	 * @param to
	 *            the new to
	 */	
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Gets the cc.
	 * 
	 * @return the cc.
	 */	
	public String getCC() {
		return cc;
	}

	/**
	 * Sets the cc.
	 * 
	 * @param cc
	 *            the new cc
	 */	
	public void setCC(String cc) {
		this.cc = cc;
	}

	/**
	 * Gets the subject.
	 * 
	 * @return the subject.
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
	 * @return the body.
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
}
