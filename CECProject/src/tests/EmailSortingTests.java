package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Email;
import cec.model.EmailBuilder;
import cec.model.EmailImpl;
import cec.model.Folder;

public class EmailSortingTests {
	
	EmailBuilder emailBuilder = new EmailBuilder();
	Email firstEmail, secondEmail, thirdEmail;
	UUID firstEmailId, secondEmailId, thirdEmailId;
	List<Email> emailList = new ArrayList<Email>();

	@Before
	public void setUp() throws Exception {
		firstEmailId = UUID.randomUUID();
		secondEmailId = UUID.randomUUID();
		thirdEmailId = UUID.randomUUID();
		
		Email firstEmail = emailBuilder.withId(firstEmailId).withLastModifiedTime("2013.05.12_At_14.07.56.874").build();
		Email secondEmail = emailBuilder.withId(secondEmailId).withLastModifiedTime("2013.05.13_At_23.36.31.603").build();
		Email thirdEmail = emailBuilder.withId(thirdEmailId).withLastModifiedTime("2013.05.12_At_14.25.55.474").build();
		
		emailList.add(firstEmail);
		emailList.add(secondEmail);
		emailList.add(thirdEmail);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void sortThreeEmails() {
		Collections.sort(emailList);
		
		assertEquals(secondEmailId, emailList.get(0).getId());
		assertEquals(thirdEmailId, emailList.get(1).getId());
		assertEquals(firstEmailId, emailList.get(2).getId());
	}
	
	// We cannot cannot throw a ParseException since it is checked.  
	@Test(expected=RuntimeException.class)
	public void sortThrowsExceptionWhenBadDate() {
		Email badlyFormatedEmail = new EmailImplExceptionCUT(UUID.randomUUID(), "", "", "", "", "", "xyz", "", null);
		emailList.add(badlyFormatedEmail);
		Collections.sort(emailList);
	}

}

class EmailImplExceptionCUT extends EmailImpl {

	public EmailImplExceptionCUT(UUID id, String from, String to, String cc,
			String subject, String body, String lastModifiedTime,
			String sentTime, Folder parentFolder) {
		super(id, from, to, cc, subject, body, lastModifiedTime, sentTime, parentFolder, false);
	}
	
	@Override
	protected void handleParseException(Exception e) {
		if (e.getClass().getName().equals("java.text.ParseException")) {
			throw new RuntimeException();
		}
	}
	
}


