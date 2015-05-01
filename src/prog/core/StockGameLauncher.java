package prog.core;

import java.util.Scanner;

import prog.ui.gui.StockTicker;
import prog.exception.GameException;
import prog.exception.GameRuntimeException;
import prog.ui.console.StockGameCommandProcessor;
import prog.core.provider.*;

public class StockGameLauncher {
	private static StockPriceProvider provider = new PerlinStockPriceProvider(0.5, 6, 0.455, 2.5, 8);
	private static AccountManagerImpl manager = new AccountManagerImpl(provider);
	private static boolean debug = false;
	private static StockGameCommandProcessor commandProcessor = new StockGameCommandProcessor(manager, provider);
	
	private static void scanInput(){
		try{
			Scanner scanner = new Scanner(System.in);
			
			do{
				System.out.print("> ");
				String[] args = scanner.nextLine().split(" ");
				
				switch(args[0]){
				case "buy":
					System.out.println("Attempting to buy shares...");
					manager.buyShares(args[1], args[2], Integer.parseInt(args[3]));
					System.out.println("Done");
					break;
					
				case "sell":
					System.out.println("Attempting to sell shares...");
					manager.sellShare(args[1], args[2], Integer.parseInt(args[3]));
					System.out.println("Done");
					break;
				
				case "create_player":
					System.out.println("Attempting to creat player...");
					manager.createPlayer(args[1]);
					System.out.println("Done");
					break;
					
				case "list_players":
					System.out.print(manager.playerList());
					break;
					
				case "list_shares":
					System.out.print(provider.shareList());
					break;
					
				case "toggle_debug":
					debug = !debug;
					System.out.println("Debug active: " + debug);
					break;
					
				case "provider_used":
					System.out.println("Provider used: " + provider.getClass());
					break;
					
				case "help":
					System.out.println("Available Commands:\n");
					System.out.println("buy playerName shareName quantity");
					System.out.println("sell playerName shareName quantity");
					System.out.println("create_player playerName shareName quantity");
					System.out.println("list_players");
					System.out.println("list_shares");
					System.out.println("toggle_debug");
					System.out.println("provider_used");
					System.out.println("exit");
					break;
					
				case "exit":
					System.out.println("Bye");
					scanner.close();
					System.exit(0);
					return;
					
				default:
					System.out.println("Command " + args[0] + " not found");
					break;
				}
			} while(true);
			
		}catch(GameRuntimeException e){
			handleGameRuntimeException(e);
		}catch(GameException e){
			handleGameException(e);
		}
	}
	
	private static void handleGameRuntimeException(RuntimeException e){
		System.out.println(e.getMessage());
		
		if(debug)
			e.printStackTrace();
		
		//Retry
		scanInput();
	}
	
	private static void handleGameException(Exception e){
		System.out.println(e.getMessage());
		
		if(debug)
			e.printStackTrace();
		
		//Retry
		scanInput();
	}
	
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
		
//		//Process player input
//		scanInput();
		
		commandProcessor.process();
	}

}
