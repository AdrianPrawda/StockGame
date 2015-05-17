package prog.test.junit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import prog.core.CashAccount;
import prog.core.Share;
import prog.exception.FundsExceededException;

public class CashAccountTest {
	private static CashAccount account;
	private static Share share;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		account = new CashAccount("Test");
		share = new Share("TEST", 10000);
	}

	@Test
	public void withdrawTest() throws FundsExceededException {
		account.withdraw(share, 5);
		assertEquals("Withdraw 5000 €", 50000, account.value());
	}
	
	@Test
	public void depositTest() {
		account.deposit(share, 5);
		assertEquals("Deposit 5000 € worth of shares", 150000, account.value());
	}
	
	@Test(expected = prog.exception.FundsExceededException.class)
	public void overbuyTest() throws FundsExceededException{
		account.withdraw(share, 100);
	}
	
	@After
	public void cleanup(){
		account = new CashAccount("Test");
	}

}