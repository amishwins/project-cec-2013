package tests.integration;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Template;
import cec.model.TemplateImpl;
import cec.persistence.EmailDaoFactory;
import cec.persistence.TemplateDao;
import cec.persistence.TemplateDaoFactory;

public class TemplateModelAndDaoIntegrationTests {
	Template templateModel;

	@Before
	public void setUp() throws Exception {
		templateModel = new TemplateImpl("Birthday!", "a@b.com", "c@d.com", "This is a subject!", "This is a body");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void saveNewEmailFromModel() {
		templateModel.save();
		Template emailTemplateLoadedFromPersistence;
	}

}
