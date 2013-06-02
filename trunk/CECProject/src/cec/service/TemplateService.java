package cec.service;

import cec.model.Template;
import cec.model.TemplateFolder;
import cec.model.TemplateImpl;
import cec.view.TemplateViewEntity;

public class TemplateService {
	
	public void saveTemplate(TemplateViewEntity templateInView) {
		Template newTemplate = new TemplateImpl(templateInView.getName(), templateInView.getTo(), 
				templateInView.getCC(), templateInView.getSubject(), templateInView.getBody());
		newTemplate.save();
	}
	
	public String [] getTemplateNames() {
		TemplateFolder tf = new TemplateFolder();
		return tf.loadTemplateNames();
	}

	public void delete(TemplateViewEntity templateInView) {
		Template template = convertEmailInViewToTemplateModel(templateInView);
		template.delete();
	}
	
	public void applyTemplateToEmail(TemplateViewEntity templateInView) {
		// take the selected template, and apply it to a new Email 
		// need to handle all the tabbing and placeholders
	}
	
	private Template convertEmailInViewToTemplateModel(TemplateViewEntity emailInView) {
		return null;
	}

	public TemplateViewEntity getTemplateEntity(String selectedTemplate) {
		TemplateFolder tf = new TemplateFolder();
		Template template = tf.getTemplate(selectedTemplate);
		TemplateViewEntity templateEntity = new TemplateViewEntity();
		templateEntity.setName(template.getName());
		templateEntity.setTo(template.getTo());
		templateEntity.setCC(template.getCC());
		templateEntity.setSubject(template.getSubject());
		templateEntity.setBody(template.getBody());
		return templateEntity;
	}
}
