package prog.core;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import prog.ui.gui.MainWindow;
import prog.ui.gui.StockTicker;
//import prog.ui.console.StockGameCommandProcessor;
import prog.ui.console.UniversalCommandProcessor;
import prog.core.provider.*;
import prog.interfaces.AccountManager;

public class StockGameLauncher extends Application{
	private static StockPriceProvider provider = new HistoricalStockPriceProvider();
	
	private static AccountManagerImpl accountManager = new AccountManagerImpl(provider);
//	private static UniversalCommandProcessor commandProcessor; 
	
	private Locale locale = new Locale("en", "EN");
	
	private Stage window;
	
	public static void main(String[] args) throws SecurityException, IOException{
		Application.launch(args);
		System.out.println("Launched Application");
	}
	
	public void setUp() throws SecurityException, IOException{
		System.out.println("Start Setup...");
		
		System.setProperty("java.util.logging.config.file", "logging.properties");
		System.setProperty("language", "de-DE");
		
		AccountManagerProxy handler = new AccountManagerProxy(accountManager);
		AccountManager manager = (AccountManager) Proxy.newProxyInstance(
			AccountManager.class.getClassLoader(),
            new Class[] { AccountManager.class },
            handler);
		manager.setProxy(manager);
//		commandProcessor = new UniversalCommandProcessor(manager,AccountManager.class);
		
		//Open ticker
		System.out.println("Open Ticker...");
		
		StockTicker ticker = new StockTicker(provider, accountManager);
		ticker.start();
		
		System.out.println("Ticker opened");
		
		//Create Players
		manager.createPlayer("Player1");
		manager.createPlayer("Player2");
		manager.createPlayer("Player3");
		
		System.out.println("Player created");
		
		//Create a few shares
		provider.createShare("BMW", 11075);
		provider.createShare("Siemens", 10050);
		provider.createShare("Volkswagen", 12398);
		provider.createShare("Osram", 8863);
		provider.createShare("Commerzbank", 1226);
		provider.createShare("BASF", 8918);
		
		System.out.println("Shares createdn");
		
//		System.out.println("Start Command Processor...");
//		
//		commandProcessor.process();
//		
//		System.out.println("Command Processor Started");
		
		System.out.println("Finished Setup");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("Initialize Setup");
		
		setUp();
		
		BorderPane root = FXMLLoader.load(StockGameLauncher.class.getResource("MainWindow.fxml"));
		
		window = primaryStage;
		window.setTitle("Stock Game");
		
		Scene mainScene = new Scene(root);
		window.setScene(mainScene);
		window.show();
		
		System.out.println("Drawed Window");
	}

}
