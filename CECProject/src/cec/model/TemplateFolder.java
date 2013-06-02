package cec.model;

import java.util.ArrayList;
import java.util.Map;

import cec.persistence.TemplateDao;
import cec.persistence.TemplateDaoFactory;

public class TemplateFolder {

	private ArrayList<Template> templates;
	private TemplateDao templateDao;

	public TemplateFolder() {
		templateDao = TemplateDaoFactory.getTemplateDaoInstance();
		templates = new ArrayList<>();
	}

	public String[] loadTemplateNames() {
		refresh(); // TODO: determine if refresh is a good strategy
		String[] templateNames = new String[templates.size()];
		int index = 0;
		for (Template t: templates) {
			templateNames[index++] = t.getName();
		}
		return templateNames;
	}

	public Template getTemplate(String templateName) {
		refresh();
		for (Template t : templates) {
			if (t.getName().equals(templateName))
				return t;
		}

		throw new IllegalArgumentException("Template was not found");
	}

	private void refresh() {
		templates.clear();
		Iterable<Map<String, String>> theTemplates = templateDao.loadAllTemplates();
		for(Map<String, String> entry: theTemplates) {
			templates.add(new TemplateImpl(entry.get("Name"), entry.get("To"), entry.get("CC"), 
					entry.get("Subject"), entry.get("Body")));
		}		
	}

}
