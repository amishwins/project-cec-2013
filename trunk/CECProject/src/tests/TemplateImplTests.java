package tests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.TemplateImpl;
import cec.persistence.TemplateDao;

public class TemplateImplTests {
	TemplateImplCUT templateImpl;
	public static final String name = "Unit Test Template";
	public static final String to = "a@b.com";
	public static final String cc = "c@d.com";
	public static final String subject = "Subject";
	public static final String body = "Body";

	@Before
	public void setUp() throws Exception {
		templateImpl = new TemplateImplCUT(name, to, cc, subject, body);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void deleteCallsDaoDelete() {
		templateImpl.delete();
		assertTrue( ((TemplateDaoStub)templateImpl.getTemplateDao()).deleteWasCalled );
	}

	@Test
	public void saveCallsDaoSave() {
		templateImpl.save();
		assertTrue( ((TemplateDaoStub)templateImpl.getTemplateDao()).saveWasCalled );
	}
	
	@Test 
	public void getObjectFields() {
		assertEquals(templateImpl.getName(), name);
		assertEquals(templateImpl.getTo(), to);
		assertEquals(templateImpl.getCC(), cc);
		assertEquals(templateImpl.getSubject(), subject);
		assertEquals(templateImpl.getBody(), body);
	}
	
}

class TemplateImplCUT extends TemplateImpl {

	public TemplateImplCUT(String name, String to, String cc, String subject,
			String body) {
		super(name, to, cc, subject, body);
	}	
	
	@Override
	protected void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = new TemplateDaoStub();
	}
	
	public TemplateDao getTemplateDao() {
		return this.templateDao;
	}
}

class TemplateDaoStub implements TemplateDao {
	
	public boolean saveWasCalled = false;
	public boolean deleteWasCalled = false;	
	
	@Override
	public void saveAsTemplate(String name, String to, String cc,
			String subject, String body) {
		saveWasCalled = true;	
	}

	@Override
	public void deleteTemplate(String fileName) {
		deleteWasCalled = true;
	}

	@Override
	public Map<String, String> loadTemplate(String fileName) {
		return null;
	}

	@Override
	public Iterable<Map<String, String>> loadAllTemplates() {
		return null;
	}
	
}