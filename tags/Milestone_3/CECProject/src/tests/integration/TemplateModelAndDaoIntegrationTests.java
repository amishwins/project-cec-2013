package tests.integration;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Template;
import cec.model.TemplateFolder;
import cec.model.TemplateImpl;

public class TemplateModelAndDaoIntegrationTests {
	Template template;
	TemplateFolder templateFolder;
	Template templateFromPersistnce;
	UUID templateName;

	@Before
	public void setUp() throws Exception {
		templateName = UUID.randomUUID();
		template = new TemplateImpl(templateName.toString(), "a@b.com", "c@d.com", "This is a subject!", "This is a body");
		templateFolder = new TemplateFolder();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void saveNewTemplateAndThenDeleteIt() {
		template.save();
		templateFromPersistnce = templateFolder.getTemplate(template.getName());
		assertEquals(templateFromPersistnce.getName(), templateName.toString());
		template.delete();
		try {
			templateFromPersistnce = templateFolder.getTemplate(template.getName());
			assertTrue(false); // should not get here
		} catch (IllegalArgumentException e) {
			// this is the correct behavior - an exception should be thrown if the file doesn't exist.
		}
	}

}
