package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Template;
import cec.model.TemplateImpl;
import cec.service.TemplateService;
import cec.view.TemplateViewEntity;

public class TemplateServiceTests {
	
	TemplateServiceCUT templateService;
	Template templateModelStub;
	TemplateViewEntity templateEntity; 
	public static final String name = "Unit Test Template";
	public static final String to = "a@b.com";
	public static final String cc = "c@d.com";
	public static final String subject = "Subject";
	public static final String body = "Body";
	

	@Before
	public void setUp() throws Exception {
		templateService = new TemplateServiceCUT();	
		templateEntity = new TemplateViewEntity();
		templateEntity.setName(name);
		templateEntity.setTo(to);
		templateEntity.setCC(cc);
		templateEntity.setSubject(subject);
		templateEntity.setBody(body);		
	
		templateModelStub = new TemplateStub();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void deleteCallsTemplateDelete() {
		templateService.deleteTemplate("Does not matter");
		assertTrue(((TemplateStub)templateService.templateStub).deleteWasCalled);
	}

	@Test
	public void saveCallsTemplateSave() {
		templateService.saveTemplate(new TemplateViewEntity());
		assertTrue(((TemplateStub)templateService.templateStub).saveWasCalled);
	}
	
	@Test
	public void getTemplateEntityGivesGoodEntity() {
		TemplateViewEntity result = templateService.getTemplateEntity("Does not matter");
		assertEquals(result.getName(), templateEntity.getName());
		assertEquals(result.getTo(), templateEntity.getTo());
		assertEquals(result.getCC(), templateEntity.getCC());
		assertEquals(result.getSubject(), templateEntity.getSubject());
		assertEquals(result.getBody(), templateEntity.getBody());
	}
}

class TemplateServiceCUT extends TemplateService {
	public Template templateStub;
	
	@Override
	protected Template buildTemplateFromEntity(TemplateViewEntity templateInView) {
		templateStub = new TemplateStub();
		return templateStub;
	}

	@Override
	protected Template getTemplateEntityFromName(String templateName) {
		templateStub = new TemplateStub();
		return templateStub;
	}
}

class TemplateStub implements Template {
	
	public boolean saveWasCalled = false;
	public boolean deleteWasCalled = false;

	@Override
	public int compareTo(Template arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void save() {
		saveWasCalled = true;
		
	}

	@Override
	public void delete() {
		deleteWasCalled = true;
		
	}

	@Override
	public String getName() {
		return TemplateServiceTests.name;
	}

	@Override
	public String getTo() {
		return TemplateServiceTests.to;
	}

	@Override
	public String getCC() {
		return TemplateServiceTests.cc;
	}

	@Override
	public String getSubject() {
		return TemplateServiceTests.subject;
	}

	@Override
	public String getBody() {
		return TemplateServiceTests.body;
	}
	
}
