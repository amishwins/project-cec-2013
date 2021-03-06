package tests.integration;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.exceptions.SourceAndDestinationFoldersAreSameException;
import cec.service.EmailService;
import cec.service.FolderService;
import cec.view.EmailViewEntity;

public class EmailServiceModelAndDaoIntegrationTests {
	String systemFolderName = "emails/Sent";
	String userFolderLevel1 = "integrationtests";
	String completePath = systemFolderName+"/"+userFolderLevel1;

	FolderService folderService;
	EmailService emailService;
	
	UUID emailId;
	EmailViewEntity emailViewEntity;
	
	
	@Before
	public void setUp() throws Exception {
		emailId = UUID.randomUUID();
		emailViewEntity = new EmailViewEntity();
		emailViewEntity.setId(emailId);
		emailViewEntity.setFrom("user@cec.com");
		emailViewEntity.setTo("Pankaj@yahoo.com");
		emailViewEntity.setCC("Pankaj@yahoo.com");
		emailViewEntity.setSubject("Subject@IntegrationTests"+emailId);
		emailViewEntity.setBody("Body");
		emailViewEntity.setSentTime("2013.05.19_At_05.08.28.457");
		emailViewEntity.setLastModifiedTime("2013.05.19_At_05.08.28.457");
		emailViewEntity.setFolder("emails/Outbox");
		
		emailService = new EmailService();
		folderService = new FolderService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void sendMoveLoadAndDeleteEmail() {
		folderService.delete(completePath);
		folderService.createSubFolder(systemFolderName,userFolderLevel1);
		emailService.sendEmail(emailViewEntity);
		emailService.move(emailViewEntity, completePath);
		List<EmailViewEntity> eMVEList = (List<EmailViewEntity>) folderService.loadEmails(completePath);
		assertEquals((eMVEList.get(0).getId()),emailId);
		assertEquals((eMVEList.get(0).getFolder()),completePath);
		emailService.delete(emailViewEntity);	
		folderService.delete(completePath);
	}
	
	@Test
	public void DraftMoveLoadAndDeleteEmail() {
		folderService.delete(completePath);
		folderService.createSubFolder(systemFolderName,userFolderLevel1);
		emailService.draftEmail(emailViewEntity);
		emailService.move(emailViewEntity, completePath);
		List<EmailViewEntity> eMVEList = (List<EmailViewEntity>) folderService.loadEmails(completePath);
		assertEquals((eMVEList.get(0).getId()),emailId);
		assertEquals((eMVEList.get(0).getFolder()),completePath);
		emailService.delete(emailViewEntity);	
		folderService.delete(completePath);
	}
	
	@Test
	public void DraftMoveToInboxSubfolderSearchAndDeleteEmail() {
		systemFolderName = "emails/Inbox";
		userFolderLevel1 = "integrationtests";
		completePath = systemFolderName+"/"+userFolderLevel1;
		
		folderService.delete(completePath);
		folderService.createSubFolder(systemFolderName,userFolderLevel1);
		emailService.draftEmail(emailViewEntity);
		emailService.move(emailViewEntity, completePath);		
		List<EmailViewEntity> eMVEList = (List<EmailViewEntity>) folderService.searchEmails("Subject@IntegrationTests"+emailId);
		assertEquals((eMVEList.get(0).getId()),emailId);
		assertEquals((eMVEList.get(0).getFolder()),completePath);
		emailService.delete(emailViewEntity);	
		folderService.delete(completePath);
	}	
    
	@Test (expected=SourceAndDestinationFoldersAreSameException.class)
	public void ShouldThrowWhenDestinationFolderIsParentFolder() {
		emailService.move(emailViewEntity,emailViewEntity.getFolder());
		
	}

}

