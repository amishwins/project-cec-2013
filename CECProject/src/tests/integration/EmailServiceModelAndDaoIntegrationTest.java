package tests.integration;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.service.EmailService;
import cec.service.FolderService;
import cec.view.EmailViewEntity;

public class EmailServiceModelAndDaoIntegrationTest {
	String systemFolderName = "emails/Sent";
	String userFolder1 = "integrationtests";
	String userFolder2 = "integrationtests2";
	String firstPath = systemFolderName+"/"+userFolder1;
	String secondPath = systemFolderName+"/"+userFolder2;

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
		emailViewEntity.setSubject("Subject");
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
		folderService.delete(firstPath);
		folderService.createSubFolder(systemFolderName,userFolder1);
		emailService.sendEmail(emailViewEntity);
		emailService.move(emailViewEntity, firstPath);
		List<EmailViewEntity> eMVEList = (List<EmailViewEntity>) folderService.loadEmails(firstPath);
		assertEquals((eMVEList.get(0).getId()),emailId);
		assertEquals((eMVEList.get(0).getFolder()),firstPath);
		emailService.delete(emailViewEntity);	
		folderService.delete(firstPath);
	}
	
	@Test
	public void DraftMoveLoadAndDeleteEmail() {
		folderService.delete(firstPath);
		folderService.createSubFolder(systemFolderName,userFolder1);
		emailService.draftEmail(emailViewEntity);
		emailService.move(emailViewEntity, firstPath);
		List<EmailViewEntity> eMVEList = (List<EmailViewEntity>) folderService.loadEmails(firstPath);
		assertEquals((eMVEList.get(0).getId()),emailId);
		assertEquals((eMVEList.get(0).getFolder()),firstPath);
		emailService.delete(emailViewEntity);	
		folderService.delete(firstPath);
	}
	
	@Test
	public void MoveTo2DifferentFoldersAndDeleteEmail() {
		folderService.delete(firstPath);
		folderService.createSubFolder(systemFolderName,userFolder1);
		folderService.createSubFolder(systemFolderName,userFolder2);
		emailService.sendEmail(emailViewEntity);
		
		//To move email from defaultPath to firstPath
		emailService.move(emailViewEntity, firstPath);
		List<EmailViewEntity> eMVEList = (List<EmailViewEntity>) folderService.loadEmails(firstPath);
		assertEquals((eMVEList.get(0).getId()),emailId);
		assertEquals((eMVEList.get(0).getFolder()),firstPath);
		
		//To move email from defaultPath to secondPath
		emailService.move(eMVEList.get(0), secondPath);
		eMVEList = (List<EmailViewEntity>) folderService.loadEmails(secondPath);
		assertEquals((eMVEList.get(0).getId()),emailId);
		assertEquals((eMVEList.get(0).getFolder()),secondPath);
		emailService.delete(eMVEList.get(0));	
		folderService.delete(firstPath);
		folderService.delete(secondPath);
	}
	
	


}

