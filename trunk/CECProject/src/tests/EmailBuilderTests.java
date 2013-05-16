package tests;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Email;
import cec.model.EmailBuilder;

public class EmailBuilderTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
    public void showEmailSortingWorks() throws Exception {
		EmailBuilder emailBuilder = new EmailBuilder();
		UUID idEmail1 = UUID.randomUUID(); 
		Email firstEmail = emailBuilder.withId(idEmail1)
				                       .withTo("PankajKapania@yahoo.com")
				                       .withCC("PankajKapania@yahoo.com")
		                               .withSubject("TestSubject1")
		                               .withLastModifiedTime("2013.05.12_At_14.07.56.874")
		                               .withSentTime("2013.05.12_At_14.07.56.874")
		                               .withBody("Body1")
		                               .build();
		
		UUID idEmail2 = UUID.randomUUID();
		Email secondEmail = emailBuilder.withId(idEmail2)
                .withTo("PankajKapania@yahoo.com")
                .withCC("PankajKapania@yahoo.com")
                .withSubject("TestSubject2")
                .withLastModifiedTime("2013.05.12_At_14.25.55.474")
                .withSentTime("2013.05.12_At_14.25.55.474")
                .withBody("Body2")
                .build();
		
		UUID idEmail3 = UUID.randomUUID();
		Email thirdEmail = emailBuilder.withId(idEmail3)
                .withTo("PankajKapania@yahoo.com")
                .withCC("PankajKapania@yahoo.com")
                .withSubject("TestSubject3")
                .withLastModifiedTime("2013.05.13_At_23.36.31.603")
                .withSentTime("2013.05.13_At_23.36.31.603")
                .withBody("Body3")
                .build();
		
		
		List<Email> emailList = new ArrayList<Email>();
		emailList.add(secondEmail);
		emailList.add(thirdEmail);
		emailList.add(firstEmail);
		
		Collections.sort(emailList);
		
		assertEquals(idEmail3,emailList.get(0).getId());
		assertEquals(idEmail2,emailList.get(1).getId());
		assertEquals(idEmail1,emailList.get(2).getId());
		
		
    }
}
