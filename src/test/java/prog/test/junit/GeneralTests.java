package prog.test.junit;

import static org.junit.Assert.*;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import prog.core.AccountManagerImpl;
import prog.core.Player;
import prog.core.provider.ConstStockPriceProvider;
import prog.core.provider.RandomStockPriceProvider;
import prog.core.provider.StockPriceProvider;
import prog.exception.FundsExceededException;
import prog.exception.NotEnoughSharesException;
import prog.interfaces.AccountManager;

public class GeneralTests {
	private static AccountManager manager;
	private static StockPriceProvider provider;
	private static String stdPlayerName;
	private static String stdShare;
	private static Player stdPlayer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		stdPlayerName = "Player 1";
		stdShare = "BMW";
		
		provider = new ConstStockPriceProvider();
		provider.createShare(stdShare, 10000);
		provider.startUpdate();
		
		manager = new AccountManagerImpl(provider);
		manager.createPlayer(stdPlayerName);
		
		stdPlayer = (Player)manager.getPlayers().get(stdPlayerName);
	}

	@Test
	public void createPlayer() {
		manager.createPlayer("Player 2");
		assertTrue(manager.getPlayers().containsKey("Player 2"));
	}
	
	@Test
	public void playerBuysShares() throws FundsExceededException {
		long value = stdPlayer.value();
		
		manager.buyShares(stdPlayerName, stdShare, 5);
		
		assertEquals(5, stdPlayer.getShareDepositAccount().numberOfShares(stdShare));
		assertEquals(value, stdPlayer.value());
	}
	
	@Test
	public void playerSellsShares() throws FundsExceededException, NotEnoughSharesException {
		long value = stdPlayer.value();
		
		manager.buyShares(stdPlayerName, stdShare, 5);
		manager.sellShare(stdPlayerName, stdShare, 3);
			
		assertEquals(2, stdPlayer.getShareDepositAccount().numberOfShares(stdShare));
		assertEquals(value, stdPlayer.value());
	}
	
	@Test
	public void checkPlayerWealth() {
		assertEquals(100000, stdPlayer.value());
	}
	
	@Test
	public void getPlayerList() {
		Map<String,Player> map = manager.getPlayers();
		
		for(Map.Entry<String, Player> entry : map.entrySet()){
			assertEquals(stdPlayerName, entry.getKey());
		}
	}
	
	@Test
	public void startAgent() {
		manager.addPlayerAgent(stdPlayerName);
		assertTrue(stdPlayer.getPlayerAgent().isTrading());
	}
	
	@Test
	public void stopAgent() {
		manager.addPlayerAgent(stdPlayerName);
		manager.dismissPlayerAgent(stdPlayerName);
		assertFalse(stdPlayer.getPlayerAgent().isTrading());
	}
	
	@Test
	public void getTransactions() throws FundsExceededException{
		manager.buyShares(stdPlayerName, stdShare, 2);
		Date date = new Date();
		Format format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String s = format.format(date);
		
		String test = "[" + s + "]\t" + "BUY\t" + stdShare + "\t2\t" + "-" + "200.0" + " €\n";
		
		assertEquals(test, manager.getTransactions(stdPlayerName));
	}
	
	@Test
	public void getTransactionsByAmount() throws FundsExceededException{
		manager.buyShares(stdPlayerName, stdShare, 3);
		manager.buyShares(stdPlayerName, stdShare, 2);
		
		Date date = new Date();
		Format format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String s = format.format(date);
		
		String testString1 = "[" + s + "]\t" + "BUY\t" + stdShare + "\t2\t" + "-" + "200.0" + " €\n";
		String testString2 = "[" + s + "]\t" + "BUY\t" + stdShare + "\t3\t" + "-" + "300.0" + " €\n";
		
		String test = testString1 + testString2;
		
		assertEquals(test, manager.getTransactions(stdPlayerName, "amount"));
	}
	
	@Test
	public void getTransactionsByAmountAndShares() throws FundsExceededException, ParseException{
		provider.createShare("A", 5000);
		
		manager.buyShares(stdPlayerName, stdShare, 3);
		manager.buyShares(stdPlayerName, stdShare, 2);
		manager.buyShares(stdPlayerName, "A", 1);
		
		Date date = new Date();
		Format format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String s = format.format(date);
		
		String testString1 = "[" + s + "]\t" + "BUY\t" + "A" + "\t1\t" + "-" + "50.0" + " €\n";
		String testString2 = "[" + s + "]\t" + "BUY\t" + stdShare + "\t2\t" + "-" + "200.0" + " €\n";
		String testString3 = "[" + s + "]\t" + "BUY\t" + stdShare + "\t3\t" + "-" + "300.0" + " €\n";
		
		String test = testString1 + testString2 + testString3;
		
		assertEquals(test, manager.getTransactions(stdPlayerName, "share", "amount"));
	}
	
	@Test(expected = prog.exception.ObjectAlreadyExistsException.class)
	public void createAlreadyExistingPlayer() {
		manager.createPlayer(stdPlayerName);
	}
	
	@After
	public void cleanup(){
		manager = new AccountManagerImpl(provider);
		manager.createPlayer(stdPlayerName);
		stdPlayer = (Player)manager.getPlayers().get(stdPlayerName);
		provider.startUpdate();
	}

}
