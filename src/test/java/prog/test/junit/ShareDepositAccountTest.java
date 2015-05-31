package prog.test.junit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import prog.core.Share;
import prog.core.ShareDepositAccount;
import prog.exception.NotEnoughSharesException;

public class ShareDepositAccountTest {
	private static ShareDepositAccount account;
	private static Share share;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		share = new Share("TEST", 10000);
		account = new ShareDepositAccount("TestAccount");
	}
	
	@Before
	public void setUpBeforeTest(){
		account.addShares(share, 10);
	}

	@Test
	public void sharePriceDiff() {
		Share anotherShare = new Share("TEST", 11000);
//		account.addShares(share, 10);
		assertEquals("Calculate price difference between two shares", 1000, account.sharePriceDifference(anotherShare, share.getName()));
	}
	
	@Test
	public void value() {
		assertEquals("Value of this account", 100000, account.value());
	}
	
	@Test
	public void addShares() {
		account.addShares(share, 10);
		assertEquals("Added 100k worth of shares", 200000, account.value());
	}
	
	@Test
	public void removeShares() throws NotEnoughSharesException {
//		account.addShares(share, 15);
		account.removeShares(share.getName(), 5);
		assertEquals("Added 100k worth of shares, then removed 50k worth of shares", 50000, account.value());
	}
	
	@Test
	public void numberOfShares() {
//		account.addShares(share, 100);
		assertEquals("Added 10 shares", 10, account.numberOfShares(share.getName()));
	}
	
	@Test(expected = prog.exception.NotEnoughSharesException.class)
	public void buyTooManyShares() throws NotEnoughSharesException {
//		account.addShares(share, 100);
		account.removeShares(share.getName(), 11);
	}
	
	@After
	public void cleanup(){
		account = new ShareDepositAccount("TestAccount");
	}

}