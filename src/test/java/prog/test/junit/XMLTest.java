package prog.test.junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import prog.util.XMLTag;

public class XMLTest {
	private static XMLTag xml;
	
	@Before
	public void setUp() throws Exception {
		xml = new XMLTag("Test","a");
		xml.addAttr("test", "simpleAttribute");
	}

	@Test
	public void basicTest() {
		assertEquals("<a test=\"simpleAttribute\">Test</a>\n",xml.generate());
	}
	
	@Test
	public void childTest() {
		XMLTag child = new XMLTag("Child","b");
		child.addAttr("mssg", "I'm your child!");
		
		xml.addChild(child);
		
		String test = "<a test=\"simpleAttribute\">\n" + "\t<b mssg=\"I'm your child!\">Child</b>\n" + "</a>\n";
		assertEquals(test ,xml.generate());
	}

}
