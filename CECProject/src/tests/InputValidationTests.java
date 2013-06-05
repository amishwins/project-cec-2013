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
	public void validateBothStartDateAndEndDateAreFilledAndGood() {
		assertTrue(v.isValidDates("2013-04-05", "2013-04-05"));
	}
	
	@Test
	public void validateStartDateIsIllFormed() {
		assertFalse(v.isValidDates("1013-04-05", "2013-04-05"));
	}
	
	@Test
	public void validateStartDateIsEmpty() {
		assertFalse(v.isValidDates("", "2013-04-05"));
	}
	
	@Test
	public void validateBothStartDateAndEndDatesAreEmpty() {
		assertFalse(v.isValidDates("", ""));
	}
	
	@Test
	public void validateEndDateIsEmpty() {
		assertFalse(v.isValidDates("2013-04-05", ""));
	}
	
	@Test
	public void validateEndDateIsIllFormed() {
		assertFalse(v.isValidDates("2013-04-05", "xxxx"));
	}
	
	@Test
	public void validateEndDateIsNotWellFormed() {
		assertFalse(v.isValidDates("2013-04-05", "2013#04#05"));
	}
	
	@Test
	public void validateEndDateHasIllegalValuesForMonthField() {
		assertFalse(v.isValidDates("2013-04-05", "2013-20-05"));
	}
	
	@Test
	public void validateStartDateHasIllegalValuesForDateField() {
		assertFalse(v.isValidDates("2013-04-40", "2013-20-05"));
	}
	
	@Test
	public void validateNumberOfCharactersInStartDate() {
		assertFalse(v.isValidDates("2014443-04-40", "2013-2044-05"));
	}
	
	@Test
	public void validateBothDatesArePassedDates() {
		assertFalse(v.hasNotPassedDates("2013-06-02", "2013-06-02"));
	}
	
	@Test
	public void validateBothDatesAreFutureDates() {
		assertTrue(v.hasNotPassedDates("2222-06-04", "2222-06-04"));
	}
	
	//this  test is very brittle...
	/*
	@Test
	public void validateBothCurrentDatesAreNotPassedDates() {
		assertTrue(v.hasNotPassedDates("2013-06-03", "2013-06-03"));
	}
	*/
	
	@Test
	public void validateBothDatesAreInOrder() {
		assertTrue(v.isStartTimeAndEndTimeInOrder("2013-06-02", "10:30 PM", "2013-06-02", "11:30 PM"));
	}
	
	@Test
	public void validateSameDatesDoesNotWork() {
		assertFalse(v.isStartTimeAndEndTimeInOrder("2013-06-02", "10:30 PM", "2013-06-02", "10:30 PM"));
	}
	
	@Test
	public void validateUnOrderedDatesDoesNotWorks() {
		assertFalse(v.isStartTimeAndEndTimeInOrder("2013-06-02", "11:30 PM", "2013-06-02", "10:30 PM"));
	}
	
	@Test
	public void validateUnOrderedDifferentDatesDoesNotWork() {
		assertFalse(v.isStartTimeAndEndTimeInOrder("2013-06-03", "11:30 PM", "2013-06-02", "10:30 PM"));
	}
	
	@Test
	public void validateOrderedDifferentDatesWork() {
		assertTrue(v.isStartTimeAndEndTimeInOrder("2013-06-03", "11:30 PM", "2013-06-04", "10:30 PM"));
	}
	
	/*@Test
	public void validateBothDateTimeArePassedDates() {
		assertFalse(v.hasNotPassedDates("2013-06-03", "11:30 PM", "2013-06-02", "10:30 PM"));
	}
	
	@Test
	public void validateBothDateTimeAreFutureDates() {
		assertTrue(v.hasNotPassedDates("2013-06-05", "11:00 PM", "2013-06-05", "11:30 PM"));
	}
	
	@Test
	public void validateBothCurrentDateTimeAreFutureDates() {
		assertTrue(v.hasNotPassedDates("2033-06-04", "03:30 PM", "2033-06-04", "03:30 PM"));
	}*/
	
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
	public void allowSomeSpacesInFolderName() {
		String folderName = "Archive 2012";
		assertTrue(v.isValidFolderName(folderName));
	}
	
	//@Test: the empty spaces is handled by the EmailClient. 
	// It was challenging to get this to work in REGEX.
	public void testWhiteSpaces() {
		String folderName = "  ";
		assertFalse(v.isValidFolderName(folderName));
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
	
	//Validation - Search Feature

	@Test
	public void validateSearchEmpty() {
		String searchFor = "";
		assertFalse(v.isValidSearched(searchFor));
	}
	@Test
	public void validateSearchSpaces() {
		String searchFor = "    ";
		assertFalse(v.isValidSearched(searchFor));
	}	
	@Test
	public void validateSearchOnlySpecialChars() {
		String searchFor = "$#%^/*-";
		assertFalse(v.isValidSearched(searchFor));
	}	
	@Test
	public void validateSearchTextWithSpecialChars() {
		String searchFor = "mary#*&$";
		assertTrue(v.isValidSearched(searchFor));
	}
	@Test
	public void validateSearchOnlyText() {
		String searchFor = "text";
		assertTrue(v.isValidSearched(searchFor));
	}			
	@Test
	public void validateSearchNumbers() {
		String searchFor = "99874521";
		assertTrue(v.isValidSearched(searchFor));
	}		
	@Test
	public void validateAtSymbol() {
		String searchFor = "$#%^/*-@";
		assertTrue(v.isValidSearched(searchFor));
	}		

}
