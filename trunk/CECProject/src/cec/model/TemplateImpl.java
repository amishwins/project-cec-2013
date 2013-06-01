package cec.model;

import cec.persistence.TemplateDao;
import cec.persistence.TemplateDaoFactory;

/**
 * EmailImpl is the concrete implementation of the email interface.
 * it is one of the core domain object of our application.
 * 
 */
public class TemplateImpl implements Template {

	/* The model class attributes */
	private String templateName;
	private String to;	
	private String cc;	
	private String subject;	
	private String body;
	
	protected TemplateDao templateDao;

	/**
	 * Instantiates a new email impl.
	 * @param name the name
	 * @param to the to
	 * @param cc the cc
	 * @param subject the subject
	 * @param body the body
	 */
	public TemplateImpl(String name, String to, String cc, String subject, String body) {
		this.templateName = name;
		this.to = to;
		this.cc = cc;
		this.subject = subject;
		this.body = body;

		setTemplateDao(TemplateDaoFactory.getTemplateDaoInstance());
	}

	/**
	 * Sets the email dao.
	 *
	 * @param templateDao the new email template dao
	 */
	protected void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	/**
	 * This method is responsible for communicating the persistence layer that 
	 * save the email object to Outbox folder. Currently it does not have the send functionality, 
	 * it just saves the file assuming that file has reached its destination.
	 *
	 * 
	 */
	@Override
	public void save() {
		templateDao.saveAsTemplate(templateName, to, cc, subject, body);
	}

	/**
	 * This method is responsible for communicating the persistence layer that 
	 * delete the email template object from the System.
	 *
	 */
	@Override
	public void delete() {
		templateDao.deleteTemplate(templateName);
	}

	@Override
	public String getName() {
		return templateName;
	}

	@Override
	public String getTo() {
		return to;
	}

	@Override
	public String getCC() {
		return cc;
	}

	@Override
	public String getSubject() {
		return subject;
	}

	@Override
	public String getBody() {
		return body;
	}

	@Override
	public int compareTo(Template arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
