package cec.service;

import cec.model.Template;
import cec.model.TemplateFolder;
import cec.model.TemplateImpl;
import cec.view.TemplateViewEntity;


/**
 * Template service satisfies the responsibility of enabling the EmailFrame view entity
 * to communicate with lower layers when events are processed. The main features are to 
 * translate the Template View Entity into a consumable model object and then communicate 
 * this down depending on action (delete, load, save).
 *
 */
public class TemplateService {
	private TemplateFolder tf;
	
	public TemplateService() {
		tf = new TemplateFolder();
	}
	
	
	/**
	 *  Calls the model layer Template save method with a well formatted model object
	 *  Precondition: well formed templateInView entity
	 *  Postcondition: a template saved to the file system
	 *  
	 */
	public void saveTemplate(TemplateViewEntity templateInView) {
		Template newTemplate = buildTemplateFromEntity(templateInView);
		newTemplate.save();
	}
	
	/**
	 * Method called by the popup 
	 * @return list of dropdown listbox values 
	 */
	public String [] getTemplateNames() {
		return tf.loadTemplateNames();
	}

	/**
	 * Calls the model layer Template delete methods for a given template name
	 * Postcondition: the file is deleted from the file system
	 * Invariant: the file shouldn't be physically moved in the file system
	 */
	public void deleteTemplate(String templateName) {
		Template template = getTemplateEntityFromName(templateName);
		template.delete();
	}

	/**
	 * For a given template name, build the template view entity object 
	 * by asking the model layer to fetch it from the persistence, and then
	 * setting the appropriate fields. 
	 * @param the name of the template chosen (provided by the caller) 
	 * @return template entity view object
	 */
	public TemplateViewEntity getTemplateEntity(String selectedTemplate) {
		Template template = getTemplateEntityFromName(selectedTemplate);
		TemplateViewEntity templateEntity = new TemplateViewEntity();
		templateEntity.setName(template.getName());
		templateEntity.setTo(template.getTo());
		templateEntity.setCC(template.getCC());
		templateEntity.setSubject(template.getSubject());
		templateEntity.setBody(template.getBody());
		return templateEntity;
	}
	
	protected Template buildTemplateFromEntity(TemplateViewEntity templateInView) {
		Template newTemplate = new TemplateImpl(templateInView.getName(), templateInView.getTo(), 
				templateInView.getCC(), templateInView.getSubject(), templateInView.getBody());
		return newTemplate;
	}
	
	protected Template getTemplateEntityFromName(String templateName) {
		return tf.getTemplate(templateName);
	}
	
}
