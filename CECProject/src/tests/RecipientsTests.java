package tests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.net.Recipients;

public class RecipientsTests {
	Recipients cut;
	String to;
	String cc;

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void toAndCCAreBothFilled() {
		to = "a@b.com; c@d.com";
		cc = "k@j.com";
		cut = new Recipients(to, cc);
		assertEquals(cut.getListOfAllTargetRecipients().size(), 3);
	}
	
	@Test
	public void onlyToIsFilled() {
		to = "a@b.com";
		cc = "";
		cut = new Recipients(to, cc);
		assertEquals(cut.getListOfAllTargetRecipients().size(), 1);
	}
	
	@Test
	public void multipleInBoth() {
		to = "a@b.com; c@d.com; e@f.com";
		cc = "k@j.com; p@d.com";
		cut = new Recipients(to, cc);
		assertEquals(cut.getListOfAllTargetRecipients().size(), 5);
	}
	
	
	@Test(expected=RuntimeException.class)
	public void noneFilledThrowsException() {
		to = "";
		cc = "";
		cut = new Recipients(to, cc);
		cut.getListOfAllTargetRecipients();
	}

}
