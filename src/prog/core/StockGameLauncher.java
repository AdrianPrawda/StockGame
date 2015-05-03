package prog.core;

import prog.ui.gui.StockTicker;
//import prog.ui.console.StockGameCommandProcessor;
import prog.ui.console.UniversalCommandProcessor;
import prog.core.provider.*;
import prog.interfaces.AccountManager;

public class StockGameLauncher {
//	private static StockPriceProvider provider = new PerlinStockPriceProvider(0.5, 6, 0.455, 2.5, 8);
	private static StockPriceProvider provider = new RandomStockPriceProvider(8);
	
	private static AccountManagerImpl manager = new AccountManagerImpl(provider);
//	private static StockGameCommandProcessor commandProcessor = new StockGameCommandProcessor(manager);
	private static UniversalCommandProcessor commandProcessor = new UniversalCommandProcessor(manager,AccountManager.class);
	
	public static void main(String[] args){
		//Open ticker
		StockTicker ticker = new StockTicker(provider);
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
