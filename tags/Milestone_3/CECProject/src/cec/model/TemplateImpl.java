package cec.model;

import cec.persistence.TemplateDao;
import cec.persistence.TemplateDaoFactory;

/**
 * TemplateImpl is the concrete implementation of the template interface.
 * it is one of the core domain object of our application.
 * The responsibility is during creation and storage. Once the template is
 * applied to an email, it no longer is valid in the runtime objects; however,
 * it remains persisted (and thus reusable) on the file system.
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
	 * Instantiates a new template impl.
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
	 * Sets the template dao. This is mainly used for testing - to be able to stub out the 
	 * dependency to the dao
	 *
	 * @param templateDao the new email template dao
	 */
	protected void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	/**
	 * This method is responsible for communicating the persistence layer that 
	 * saves the template to the specified system template folder.  
	 * it just saves the file assuming that file has reached its destination.
	 * 
	 */
	@Override
	public void save() {
		templateDao.saveAsTemplate(templateName, to, cc, subject, body);
	}

	/**
	 * This method is responsible for communicating the persistence layer that 
	 * delete the  template object from the System.
	 *
	 */
	@Override
	public void delete() {
		templateDao.deleteTemplate(templateName);
	}

	
	/* (non-Javadoc)
	 * All the setters and getters
	 */
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
		// not yet implemented - we shouldn't sort templates
		throw new IllegalAccessError();
	}
}
