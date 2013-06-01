package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Search;

public class SearchTests {

	String source;
	String searchFor;	
	
	@Before
	public void setUp() throws Exception {
		Search newSearch = new Search(source, searchFor);
	}

	@After
	public void tearDown() throws Exception {
	}


	//Empty Space
	@Test
	public void searchEmptySpace() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = " ";
		Search searchObj = new Search(source, searchFor);
		assertFalse(searchObj.isMatch());
	}	
	
	//Single word
	@Test
	public void searchSingleWord() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = "Email";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}
	
	//Single word with case differences
	@Test 
	public void searchSingleWordCaseDiffs() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = "eMaIl";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}
	
	///Exact phrase
	@Test 
	public void searchExactPhrase() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = "content of an email";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}

	///Exact phrase with case differences
	@Test 
	public void searchExactPhrasetWithCaseDiffs() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = "conTent Of aN emAiL";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}

	///Piece of phrase with Regex injection
	@Test 
	public void searchPhrasetWithRegexInj() {
		this.source = "Email Content - This is the content of an email";
		this.searchFor = "This.+?content";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}
	
	///Numbers + Dashes
	@Test 
	public void searchNumbers() {
		this.source = "Hi dear, my phone number is 514-547-9877";
		this.searchFor = "514-547";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}	
	
	///Special Chars Brackets Part
	@Test 
	public void searchSpecialCharsBrackets() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "[brack";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}	
	
	///Special Chars Brackets Full
	@Test 
	public void searchSpecialCharsBracketsFull() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "[brackets]";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}	
	
	
	///Special Chars Slashes Part
	@Test 
	public void searchSpecialCharsSlashes() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "/slash";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}

	///Special Chars Slashes Full
	@Test 
	public void searchSpecialCharsSlashesFull() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "/slashes/";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}	

	///Special Chars Dashes Part
	@Test 
	public void searchSpecialCharsDashes() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "-dash";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}

	///Special Chars Dashes Full
	@Test 
	public void searchSpecialCharsDashesFull() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "-dashes-";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}		
	
	
	///Special Chars Braces Part
	@Test 
	public void searchSpecialCharsBraces() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "{brace";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}
	
	///Special Chars Braces Full
	@Test 
	public void searchSpecialCharsBracesFull() {
		this.source = "Hi dear, I like [brackets], /slashes/, -dashes- ans {braces} ";
		this.searchFor = "{braces}";
		Search searchObj = new Search(source, searchFor);
		assertTrue(searchObj.isMatch());
	}		
	 	
}



