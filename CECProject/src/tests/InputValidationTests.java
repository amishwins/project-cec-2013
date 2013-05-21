package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.view.Validator;

public class InputValidationTests {
	
	Validator v;

	@Before
	public void setUp() throws Exception {
		v = new Validator();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void validateWellFormedSingleEmail() {
		String emailString = "a@b.com";
		assertTrue(v.isValidSendees(emailString, ""));
	}
	
	@Test
	public void validatePoorlyFormedSingleEmail() {
		String emailString = "abc";
		assertFalse(v.isValidSendees(emailString, ""));
	}
	
	@Test
	public void validateAnotherPoorlyFormedSingleEmail() {
		String emailString = "abc@.";
		assertFalse(v.isValidSendees(emailString, ""));
	}
	
	@Test
	public void validateWellFormedMultipleEmails() {
		String emailString = "a@b.com; b@d.com";
		assertTrue(v.isValidSendees(emailString, ""));
	}

	@Test
	public void validateWellFormedAndPoorlyFormedEmails() {
		String emailString = "a@b.com; bad.com";
		assertFalse(v.isValidSendees(emailString, ""));
	}

	@Test
	public void validateCCIsFilledAndToIsEmpty() {
		assertTrue(v.isValidSendees("", "a@b.com"));
	}
	
	@Test
	public void validateBothToAndCCAreEmpty() {
		assertFalse(v.isValidSendees("", ""));
	}
	
	@Test
	public void validateBothToAndCCAreFilledAndGood() {
		assertTrue(v.isValidSendees("a@b.com", "c@d.com"));
	}
	

	@Test
	public void onlyAtSymbolFails() {
		String emailString = "@";
		assertFalse(v.isValidSendees(emailString, ""));
	}
	
	@Test
	public void validateFolderName() {
		String folderName = "MyDocs";
		assertTrue(v.isValidFolderName(folderName));
	}
	
	@Test
	public void validateFolderNameWithAllowedSpecialCharacters() {
		String folderName = "azAZ0_9";
		assertTrue(v.isValidFolderName(folderName));
	}
	
	@Test
	public void validateIllegalCharactersFolderName() {
		String folderName = ".,/[];'*&^$";
		assertFalse(v.isValidFolderName(folderName));
	}
	
	@Test
	public void validateIllegalCharactersSandwitchedInFolderName() {
		String folderName = "abc!@#$%^&*()def";
		assertFalse(v.isValidFolderName(folderName));
	}
	
	@Test
	public void validateEmptyFolderName() {
		String folderName = "";
		assertFalse(v.isValidFolderName(folderName));
	}
	

	

}
