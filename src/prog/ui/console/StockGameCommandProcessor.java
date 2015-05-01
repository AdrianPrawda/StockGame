package prog.ui.console;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

import prog.core.AccountManagerImpl;
import prog.ui.CommandScanner;
import prog.core.enumerator.StockGameCommandType;
import prog.ui.CommandDescriptor;
import prog.exception.GameException;
import prog.exception.GameRuntimeException;
import prog.core.provider.*;

public class StockGameCommandProcessor {
	private AccountManagerImpl manager;
	private StockPriceProvider provider;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public StockGameCommandProcessor(AccountManagerImpl accountManager, StockPriceProvider provider){
		manager = accountManager;
		this.provider = provider;
		reader = new BufferedReader(new InputStreamReader(System.in));
		writer = new PrintWriter(System.out);
	}
	
	private void handleGameException(GameException e){
		writer.println(e.getMessage());
		writer.flush();
		process();
	}
	
	private void handleGameRuntimeException(GameRuntimeException e){
		writer.println(e.getMessage());
		writer.flush();
		process();
	}
	
	public void process(){
		CommandScanner scanner = new CommandScanner(StockGameCommandType.values(), reader);
		
		try{
			
			//Main loop
			do{
				CommandDescriptor commandDescriptor = new CommandDescriptor();
				
				commandDescriptor = scanner.commandLine2CommandDescriptor(commandDescriptor);
				
				Object[] params = commandDescriptor.getParams();
				
				StockGameCommandType commandType = (StockGameCommandType)commandDescriptor.getCommandType();
			
				switch(commandType){
				case EXIT:
					writer.println("Bye");
					writer.flush();
					System.exit(0);
					break;
				case HELP:
					writer.println(commandType.getHelpText());
					break;
				case CREATEPLAYER:
					writer.println("Attempting to create player...");
					manager.createPlayer((String)params[0]);
					writer.println("Done");
					break;
				case BUYSHARE:
					writer.println("Attempting to buy share...");
					manager.buyShares((String)params[0], (String)params[1], (int)params[2]);
					writer.println("Done");
					break;
				case SELLSHARE:
					writer.println("Attempting to buy share...");
					manager.sellShare((String)params[0], (String)params[1], (int)params[2]);
					writer.println("Done");
					break;
				case LISTPLAYERS:
					writer.println(manager.playerList().replaceFirst("\n\n", "\n"));
					break;
				case LISTSHARES:
					writer.println(provider.shareInfo());
					break;
				default:
					writer.println("Command not found");
					break;
				}
				writer.flush();
				
			}while(true);
		
		}catch(GameException e){
			handleGameException(e);
		}catch(GameRuntimeException e){
			handleGameRuntimeException(e);
		};
	}
}
