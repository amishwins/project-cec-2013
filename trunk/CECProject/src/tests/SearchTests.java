package tests;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Email;
import cec.model.EmailBuilder;
import cec.model.Search;
import cec.model.SearchImpl;

public class SearchTests {

	String source;
	String searchFor;	
	boolean result;
	
	EmailBuilder emailBuilder = new EmailBuilder();
	Email firstEmail, secondEmail;
	UUID firstEmailId, secondEmailId;
	
	@Before
	public void setUp() throws Exception {
		firstEmailId = UUID.randomUUID();
		secondEmailId = UUID.randomUUID();
				
		firstEmail = emailBuilder.withId(firstEmailId)
				.withFrom("troy@encs.concordia.ca")
				.withTo("deyvid1@gmail.com")
				.withCC("pankaj1@gmail.com")
				.withSubject("TestSubject1")
				.withBody("Body1")
				.withLastModifiedTime("2013.05.12_At_14.07.56.800")
			    .build();

		secondEmail = emailBuilder.withId(secondEmailId)
				.withFrom("pierre1@gmail.com")
				.withTo("romeo2@gmail.com")
				.withCC("amish2@gmail.com")
				.withSubject("TestSubject2")
				.withBody("Body2")
				.withLastModifiedTime("2013.05.12_At_14.07.56.900")
			    .build();		
	}	
	
	@After
	public void tearDown() throws Exception {
	}

	//Search Engine
	//Empty String
	@Test
	public void searchEmptyString() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = "";
		Search searchObj = new SearchImpl(source, searchFor);
		assertFalse(searchObj.isMatch());
	}	
	//Single Space
	@Test
	public void searchSpace() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = " ";
		Search searchObj = new SearchImpl(source, searchFor);
		assertFalse(searchObj.isMatch());
	}
	
	//Single word
	@Test
	public void searchSingleWord() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = "Email";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}
	
	//Single word with case differences
	@Test 
	public void searchSingleWordCaseDiffs() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = "eMaIl";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}
	
	///Exact phrase
	@Test 
	public void searchExactPhrase() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = "content of an email";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}
	
	///Separated words inside the text
	@Test 
	public void searchSeparatedWordsInTheText() {
		this.source = "Email Content - This is the content of Paul's email";
		this.searchFor = "content paul";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}
	
	///Separated words only one inside the text
	@Test 
	public void searchSeparatedWordsOneInTheText() {
		this.source = "Email Content - This is the content of Paul's email";
		this.searchFor = "content deyvid";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}		
	
	///Exact phrase with case differences
	@Test 
	public void searchExactPhrasetWithCaseDiffs() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = "conTent Of aN emAiL";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}

	///Piece of phrase with Regex injection
	@Test 
	public void searchPhrasetWithRegexInj() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = "This.+?content";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}
	
	///Numbers + Dashes
	@Test 
	public void searchNumbers() {
		this.source = "Hi dear, my phone number is 514-547-9877";
		this.searchFor = "514-547";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}
	
	///Special Chars Brackets Part
	@Test 
	public void searchSpecialCharsBrackets() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "[brack";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}	
	
	///Special Chars Brackets Full
	@Test 
	public void searchSpecialCharsBracketsFull() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "[brackets]";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}	
	
	
	///Special Chars Slashes Part
	@Test 
	public void searchSpecialCharsSlashes() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "/slash";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}

	///Special Chars Slashes Full
	@Test 
	public void searchSpecialCharsSlashesFull() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "/slashes/";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}	

	///Special Chars Dashes Part
	@Test 
	public void searchSpecialCharsDashes() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "-dash";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}

	///Special Chars Dashes Full
	@Test 
	public void searchSpecialCharsDashesFull() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "-dashes-";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}		
	
	
	///Special Chars Braces Part
	@Test 
	public void searchSpecialCharsBraces() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "{brace";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}
	
	///Special Chars Braces Full
	@Test 
	public void searchSpecialCharsBracesFull() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "{braces}";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}	
	
	//Special Chars Single Brace 
	@Test 
	public void searchSpecialCharsOneBrace() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "{";
		Search searchObj = new SearchImpl(source, searchFor);
		assertFalse(searchObj.isMatch());
	}
	
	//Special Chars with Text - Exact Search
	@Test 
	public void searchSpecialCharsWithTextExact() {
		this.source = "Hi dear, new formula a+b=c ";
		this.searchFor = "a+b=c";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}

	//Special Chars with Text concatenated, words treated separately
	@Test 
	public void searchSpecialCharsWithText() {
		this.source = "Hi mom, a+b=c ";
		this.searchFor = "c+a";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}	

	//Special Chars with Text concatenated, no matches
	@Test 
	public void searchSpecialCharsWithTextFalse() {
		this.source = "Hi dear, a+b=c";
		this.searchFor = "z+y";
		Search searchObj = new SearchImpl(source, searchFor);
		assertFalse(searchObj.isMatch());
	}	
	
	//Text with @ symbol
	@Test 
	public void searchTextWithAt() {
		this.source = "Hi dear, send the reports to mary@f1.com ";
		this.searchFor = "mary@f1";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}	

	//Text with @ symbol that doesn't match
	@Test 
	public void searchTextWithAtNotMatch() {
		this.source = "Hi dear, send the reports to mary@f1.com ";
		this.searchFor = "mari@f1";
		Search searchObj = new SearchImpl(source, searchFor);
		assertFalse(searchObj.isMatch());
	}	
	
	//Text with @ symbol and dot (.)
	@Test 
	public void searchTextWithAtDot() {
		this.source = "Hi dear, send the reports to mary@f1.com ";
		this.searchFor = "mary@f1.com";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}		
	
	
	//Text with @ symbol and dot (.) that doesn't match
	@Test 
	public void searchTextWithAtDotNotMatch() {
		this.source = "Hi dear, send the reports to mary@f1.com ";
		this.searchFor = "mari@f1.com";
		Search searchObj = new SearchImpl(source, searchFor);
		assertFalse(searchObj.isMatch());
	}		

	//Only @symbol
	@Test 
	public void searchAt() {
		this.source = "Hi dear, send the reports to mary@f1.com ";
		this.searchFor = "@";
		Search searchObj = new SearchImpl(source, searchFor);
		assertTrue(searchObj.isMatch());
	}		
		
	
	//Searching on Email Entity
	@Test
	public void searchEmailFromMatch() {
		assertTrue(firstEmail.isMatch("encs"));
	}
	@Test
	public void searchEmailFromMisMatch() {
		assertFalse(secondEmail.isMatch("encs"));
	}	
	@Test
	public void searchEmailToMatch() {
		assertTrue(secondEmail.isMatch("romeo2"));
	}	
	@Test
	public void searchEmailToMisMatch() {
		assertFalse(firstEmail.isMatch("romeo2"));
	}	
	@Test
	public void searchEmailSubjectMatch() {
		assertTrue(secondEmail.isMatch("TestSubject2"));
	}
	@Test
	public void searchEmailSubjectMisMatch() {
		assertFalse(firstEmail.isMatch("TestSubject2"));
	}
	@Test
	public void searchEmailCCMatch() {
		assertTrue(firstEmail.isMatch("pankaj1"));		
	}
	@Test
	public void searchEmailCCMisMatch() {
		assertFalse(secondEmail.isMatch("pankaj1"));
	}			
	@Test
	public void searchEmailBodyMatch() {
		assertTrue(secondEmail.isMatch("body2"));
	}
	@Test
	public void searchEmailBodyMisMatch() {
		assertFalse(firstEmail.isMatch("body2"));
	}
	@Test
	public void searchEmailBodyMatchBoth() {
		assertTrue(firstEmail.isMatch("body") ==true &&
				   secondEmail.isMatch("body")==true);				
	}				 	
}