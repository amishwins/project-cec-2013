package tests.integration;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Email;
import cec.model.EmailBuilder;
import cec.persistence.EmailDao;


// (maybe move this comment) more dangerous because it creates objects and deletes objects in the file system
public class EmailDaoIntegrationTests {
	
	Email email;

	@Before
	public void setUp() throws Exception {
		email = (new EmailBuilder()).computeID().build();		
	}

	@After
	public void tearDown() throws Exception {
/*    	try{
   		 
    		File file = new File(email.);
 
    		if(file.delete()) {
    			System.out.println(file.getName() + " is deleted!");
    		} else {
    			System.out.println("Delete operation is failed.");
    		}
 
    	} catch(Exception e) {
    		e.printStackTrace();
     	}*/
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
