package cec.service;

import cec.model.Template;
import cec.model.TemplateFolder;
import cec.model.TemplateImpl;
import cec.view.TemplateViewEntity;

public class TemplateService {
	private TemplateFolder tf;
	
	public TemplateService() {
		tf = new TemplateFolder();
	}
	
	public void saveTemplate(TemplateViewEntity templateInView) {
		Template newTemplate = buildTemplateFromEntity(templateInView);
		newTemplate.save();
	}
	
	public String [] getTemplateNames() {
		return tf.loadTemplateNames();
	}

	public void deleteTemplate(String templateName) {
		Template template = getTemplateEntityFromName(templateName);
		template.delete();
	}

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
