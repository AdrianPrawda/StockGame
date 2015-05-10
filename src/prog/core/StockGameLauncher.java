package prog.core;

import java.io.IOException;
import java.lang.reflect.Proxy;

import prog.ui.gui.StockTicker;
//import prog.ui.console.StockGameCommandProcessor;
import prog.ui.console.UniversalCommandProcessor;
import prog.core.provider.*;
import prog.interfaces.AccountManager;

public class StockGameLauncher {
	private static StockPriceProvider provider = new RandomStockPriceProvider(8);
	
	private static AccountManagerImpl accountManager = new AccountManagerImpl(provider);
	private static UniversalCommandProcessor commandProcessor; 
	
	public static void main(String[] args) throws SecurityException, IOException{
		AccountManagerProxy handler = new AccountManagerProxy(accountManager);
		AccountManager manager = (AccountManager) Proxy.newProxyInstance(
			AccountManager.class.getClassLoader(),
            new Class[] { AccountManager.class },
            handler);
		manager.setProxy(manager);
		commandProcessor = new UniversalCommandProcessor(manager,AccountManager.class);
		//Open ticker
		StockTicker ticker = new StockTicker(provider, accountManager);
		ticker.start();
		
		//Create Players
		manager.createPlayer("Player1");
		manager.createPlayer("Player2");
		manager.createPlayer("Player3");
		
		//Create a few shares
		provider.createShare("BMW", 11075);
		provider.createShare("Siemens", 10050);
		provider.createShare("Audi", 12398);
		provider.createShare("Porsche", 8863);
		provider.createShare("Commerzbank", 1226);
		provider.createShare("BASF", 8918);
		
		commandProcessor.process();
	}

}
