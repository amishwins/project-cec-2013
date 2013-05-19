package tests.integration;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Folder;
import cec.model.FolderFactory;
import cec.model.Hierarchy;
import cec.service.FolderService;

public class FolderServiceModelAndDaoIntegrationTests {
	String systemFolderName = "emails/Inbox";
	String userFolderLevel1 = "integrationtests";
	String completePath = systemFolderName+"/"+userFolderLevel1;
	FolderService folderService;
	
	@Before
	public void setUp() throws Exception {
		folderService = new FolderService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createAndDeleteFolder() {
		folderService.createSubFolder(systemFolderName,userFolderLevel1);
		List<String> folder = (List<String>)folderService.loadHierarchy();
		assertTrue(folder.contains(completePath));
		folderService.delete(completePath);
	}
	

}
