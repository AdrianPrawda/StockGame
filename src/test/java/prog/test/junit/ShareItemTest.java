package prog.test.junit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import prog.core.Share;
import prog.core.ShareItem;
import prog.exception.NotEnoughSharesException;

public class ShareItemTest {
	private static ShareItem shareItem;
	private static Share share;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		share = new Share("TEST", 10000);
		shareItem = new ShareItem(share);
	}

	@Test(expected = prog.exception.WrongShareException.class)
	public void wrongShare() {
		Share wrongShare = new Share("DUMMY", 10000);
		shareItem.addShares(wrongShare, 10);
	}
	
	@Test
	public void addShares(){
		shareItem.addShares(share, 10);
		assertEquals("Adding 10 shares", 10, shareItem.getQuantity());
	}
	
	@Test
	public void removeShares() throws NotEnoughSharesException{
		shareItem.addShares(share, 15);
		shareItem.removeShares(10);
		assertEquals("Removing 10 shares", 5, shareItem.getQuantity());
	}
	
	@After
	public void cleanup(){
		shareItem = new ShareItem(share);
	}

}