package cec.model;

import java.util.ArrayList;

public class TemplateFolder {

	private ArrayList<Template> templates;

	public TemplateFolder() {
		templates = new ArrayList<Template>();
	}

	public String[] loadTemplateNames() {
		String[] templateNames = (String[]) templates.toArray();
		return templateNames;
	}

	public Template getTemplate(String templateName) {
		for (Template t : templates) {
			if (t.getName().equals(templateName))
				return t;
		}

		throw new IllegalArgumentException("Template was not found");
	}

}
