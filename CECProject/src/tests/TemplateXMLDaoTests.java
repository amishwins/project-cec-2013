package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.persistence.TemplateDao;
import cec.persistence.TemplateDaoFactory;

public class TemplateXMLDaoTests {
	
	// Dangerous test, which actually creates files in the file system and then deletes them
	TemplateDao emailTemplateDao;
	Map<String,String> templateFromFS;
	UUID templateID;
	String name, to, cc, subject, body;

	@Before
	public void setUp() throws Exception {
		emailTemplateDao = TemplateDaoFactory.getEmailTemplateDaoInstance();
		templateFromFS = new TreeMap<>();
		name = "Birthday";
		to = "a@b.com";
		cc = "c@d.com";
		subject = "Hi buddy";
		body = "Hello there ${name} \n \n You are amazing! \n Bye, \n ${myName}";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void saveActuallySavesFileAndThenDeletesIt() {
		emailTemplateDao.saveAsTemplate(name, to, cc, subject, body);
		templateFromFS = emailTemplateDao.loadTemplate(name);
		assertEquals(templateFromFS.get("Name").toString(), name);
		assertEquals(templateFromFS.get("To").toString(), to);
		assertEquals(templateFromFS.get("CC").toString(), cc);
		assertEquals(templateFromFS.get("Subject").toString(), subject);
		assertEquals(templateFromFS.get("Body").toString(), body);
		//emailTemplateDao.deleteTemplate(name);
	}
	
	@Test
	public void saveAndLoadAll() {
		emailTemplateDao.saveAsTemplate(name, to, cc, subject, body);
		Iterable<Map<String,String>> templates = emailTemplateDao.loadAllTemplates();
		for(Map<String,String> entry: templates) {
			templateFromFS = entry;
			assertNotNull(templateFromFS);
		}
	//	emailTemplateDao.deleteTemplate(name);
	}

}
