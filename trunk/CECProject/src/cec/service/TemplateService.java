package cec.service;

import cec.model.Email;
import cec.model.EmailBuilder;
import cec.model.Folder;
import cec.model.FolderFactory;
import cec.model.Template;
import cec.model.TemplateFolder;
import cec.model.TemplateImpl;
import cec.view.EmailViewEntity;
import cec.view.TemplateViewEntity;

public class TemplateService {
	
	public void saveTemplate(TemplateViewEntity templateInView) {
		// TODO: how to get the name? 
		Template newTemplate = new TemplateImpl(templateInView.getName(), templateInView.getTo(), 
				templateInView.getCC(), templateInView.getSubject(), templateInView.getBody());
		newTemplate.save();
	}
	
	public String [] getTemplateNames() {
		TemplateFolder tf = new TemplateFolder();
		return tf.loadTemplateNames();
	}

	public void delete(EmailViewEntity emailInView) {
		Template template = convertEmailInViewToTemplateModel(emailInView);
		template.delete();
	}
	
	private Template convertEmailInViewToTemplateModel(EmailViewEntity emailInView) {
		return null;
	}
}
