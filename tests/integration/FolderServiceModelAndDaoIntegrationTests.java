package tests.integration;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.service.FolderService;
import exceptions.FolderAlreadyExistsException;
import exceptions.RootFolderSubfolderCreationException;

public class FolderServiceModelAndDaoIntegrationTests {
	String rootFolder = "emails";
	String inboxFolder = "Inbox";
	String systemFolderName = "emails/Inbox";
	String userFolder = "integrationtests";
	String completePath = systemFolderName+"/"+userFolder;
	FolderService folderService;
	
	@Before
	public void setUp() throws Exception {
		folderService = new FolderService();
	}

	@After
	public void tearDown() throws Exception {
		folderService.delete(completePath);
	}

	@Test
	public void createAndDeleteFolder() {
		folderService.delete(completePath);
		folderService.createSubFolder(systemFolderName,userFolder);
		List<String> folder = (List<String>)folderService.loadHierarchy();
		assertTrue(folder.contains(completePath));
		folderService.delete(completePath);
	}
	
	@Test (expected=FolderAlreadyExistsException.class)
	public void ShouldThrowWhenFolderAlreadyExists() {
		folderService.delete(completePath);
		folderService.createSubFolder(systemFolderName,userFolder);
		folderService.createSubFolder(systemFolderName,userFolder);
		folderService.delete(completePath);
	}
	
	@Test (expected=RootFolderSubfolderCreationException.class)
	public void ShouldThrowWhenUserTriesToCreateSystemFolder() {
		folderService.createSubFolder(rootFolder,inboxFolder);		
	}
	
	

}
