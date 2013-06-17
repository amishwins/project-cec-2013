package cec.model;

import java.util.ArrayList;
import java.util.Map;

import cec.persistence.TemplateDao;
import cec.persistence.TemplateDaoFactory;

/**
 * TemplateFolder represents the runtime model object which is able to store and 
 * retrieve all the templates in the system, and can can communicate with the 
 * persistence to update the data. Currently, it always refreshes its data upon 
 * every operation. 
 *
 */
public class TemplateFolder {

	private ArrayList<Template> templates;
	private TemplateDao templateDao;

	public TemplateFolder() {
		templateDao = TemplateDaoFactory.getTemplateDaoInstance();
		templates = new ArrayList<>();
	}

	/**
	 * Convenience method for the service layer to load the list in String[]
	 * format such that it can be easily displayed on the UI. 
	 */
	public String[] loadTemplateNames() {
		refresh(); 
		String[] templateNames = new String[templates.size()];
		int index = 0;
		for (Template t: templates) {
			templateNames[index++] = t.getName();
		}
		return templateNames;
	}

	/**
	 * Fetch the template object which corresponds to the name selected from the 
	 * UI (propagated down via the service layer)
	 * 
	 * @param templateName
	 * @return
	 */
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
