package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StringTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String s = "hi hi";
		String y = s + "\n";
		
		String z = y.substring(0, y.length() - 1);
		assertEquals(s, z);
	}

}
