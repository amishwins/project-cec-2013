package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cec.model.Search;

public class SearchTests {

	String source;
	String pattern;	
	
	@Test
	public void searchContentWithValidText() {
		this.source = "Email Content";
		this.pattern = "Email";
		Search searchObj = new Search(source, pattern);
		assertTrue(searchObj.isMatch());
	}
	
	@Test
	public void searchContentWithCaseDiffs() {
		this.source = "Email Content";
		this.pattern = "eMaIl";
		Search searchObj = new Search(source, pattern);
		assertTrue(searchObj.isMatch());
	}
	
	 	
}
