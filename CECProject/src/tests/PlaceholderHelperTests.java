package tests;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.service.PlaceholderHelper;

public class PlaceholderHelperTests {
	
	PlaceholderHelper phh;
	String text;
	Integer expectedStart;
	Integer expectedEnd;

	@Before
	public void setUp() throws Exception {
		text = "Hi ${friend} ${feeling}. ";
		phh = new PlaceholderHelper(text);
		phh.findNext();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getIndexOfFirstMatch() {
		expectedStart = 3;
		expectedEnd = 12;
		assertEquals(expectedStart, phh.getStartPositionOfNextMatch());
		assertEquals(expectedEnd, phh.getEndPositionOfNextMatch());
	}

	@Test 
	public void findNextIndeces() {
		phh.findNext();
		expectedStart = 13;
		expectedEnd = 23;
		assertEquals(expectedStart, phh.getStartPositionOfNextMatch());
		assertEquals(expectedEnd, phh.getEndPositionOfNextMatch());		
	}
	
	@Test
	public void noPlaceHoldersReturnsMinusOne() {
		text = "There are simply no placeholders here!";
		phh = new PlaceholderHelper(text);
		phh.findNext();
		expectedStart = -1;
		expectedEnd = -1;
		assertEquals(expectedStart, phh.getStartPositionOfNextMatch());
		assertEquals(expectedEnd, phh.getEndPositionOfNextMatch());
	}
	
	@Test
	public void resetBringsItBack() {
		phh.findNext();
		phh.reset();
		phh.findNext();
		expectedStart = 3;
		expectedEnd = 12;
		assertEquals(expectedStart, phh.getStartPositionOfNextMatch());
		assertEquals(expectedEnd, phh.getEndPositionOfNextMatch());
	}
	
	
	@Test
	public void getPositionsOfAllPlaceHolders() {
		Map<Integer,Integer> results = phh.positionsOfAllPlaceHolders();
		
		for(Map.Entry<Integer, Integer> entry: results.entrySet()) {
			assertNotNull(entry);
		}
	}
}
